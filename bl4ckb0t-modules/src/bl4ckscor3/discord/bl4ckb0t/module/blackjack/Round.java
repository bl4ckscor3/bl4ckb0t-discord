package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

class Round implements IReactable {
	private static final String DIE = "🎲";
	private static final String NO_ENTRY = "⛔";
	protected boolean hasStarted = false;
	protected boolean isStarting = false;
	protected Shoe shoe;
	protected Players players;
	protected Message roundMessage;
	protected MessageChannel channel;
	protected ScheduledFuture<?> removal;

	protected Round(MessageChannel c) {
		if (shoe == null)
			shoe = new Shoe();

		if (players == null)
			players = new Players();

		channel = c;
	}

	/**
	 * Starts this round if players are present and it has not already started
	 */
	protected void start() {
		if (hasStarted || players.isEmpty())
			return;

		isStarting = false;
		hasStarted = true;

		players.get(0).setActive();

		for (int i = 0; i < 2; i++) {
			players.getDealer().addCard(shoe.pull());

			for (Player p : players) {
				p.addCard(shoe.pull());
			}
		}

		boolean dealerHasBlackJack = players.getDealer().getCards().value() == 21;
		int playerBlackJacks = 0;

		for (Player p : players) {
			if (p.getCards().value() == 21) {
				p.setStatus(Status.BJ);
				playerBlackJacks++;
			}
		}

		while (players.getCurrentPlayer().getCards().value() == 21) {
			if (!players.next())
				break;
		}

		if (dealerHasBlackJack) {
			players.getDealer().setStatus(Status.BJ);

			for (Player p : players) {
				if (p.getStatus() == Status.BJ)
					p.setStatus(Status.TIE);
			}

			endGame();
		}
		else if (playerBlackJacks == players.size())
			endGame();
		else
			updateMessage();
	}

	@Override
	public void onReactionAdd(MessageReactionAddEvent event) {
		Player p = players.getCurrentPlayer();
		String name = event.getReaction().getEmoji().getName();

		removal.cancel(false);

		if (name.equals(DIE)) {
			p.addCard(shoe.pull());

			if (p.getCards().value() > 21) {
				p.setStatus(Status.BUST);

				if (!players.next()) {
					endGame();
					return;
				}
			}

			updateMessage();
		}
		else if (name.equals(NO_ENTRY)) {
			if (p.getCards().value() < players.getDealer().getCards().value())
				p.setStatus(Status.BUST);
			else
				p.setStatus(Status.STAND);

			if (!players.next()) {
				try {
					Thread.sleep(250);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				endGame();
			}

			updateMessage();
		}
	}

	/**
	 * Resends the round message with new cards or player states
	 */
	private void updateMessage() {
		try {
			if (roundMessage != null)
				roundMessage.delete().queue();

			Utilities.sendMessage(channel, players.build(), msg -> {
				roundMessage = msg;
				Utilities.react(roundMessage, DIE, NO_ENTRY);
				waitForReaction(roundMessage.getIdLong(), players.getCurrentPlayer().getUser().getIdLong());
				removal = Executors.newScheduledThreadPool(1).schedule(() -> {
					AWAITED_REACTIONS.remove(roundMessage.getIdLong());
					Utilities.sendMessage(channel, "No decision in time. Removing **" + players.getCurrentPlayer().getUser().getName() + "** from the table.");
					leave(players.getCurrentPlayer().getUser());

					if (!players.isEmpty())
						updateMessage();
					else {
						Utilities.sendMessage(channel, "All players have left, the table is empty.");
						reset(false);
					}
				}, 120, TimeUnit.SECONDS);
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			Utilities.sendMessage(channel, "An error occured, see log for details. Round will be reset.");
			reset(true);
		}
	}

	/**
	 * Adds a player to this round
	 *
	 * @param user The player to add
	 * @param status The initial {@link Status} the player should have.
	 * @return true if the player has joined, false otherwise
	 */
	protected boolean join(User user, Status status) {
		if (!players.contains(user)) {
			String msg = user.getAsMention() + " joined the table!";

			players.add(new Player(user, status));

			if (hasStarted)
				msg += " You will be able to start next round.";

			Utilities.sendMessage(channel, msg);
			return true;
		}
		else {
			Utilities.sendMessage(channel, "You are already playing.");
			return false;
		}
	}

	/**
	 * Removes a player from this round
	 *
	 * @param user The player to remove
	 */
	protected void leave(User user) {
		if (players.contains(user)) {
			String msg = user.getAsMention() + " left the table.";

			players.remove(user);

			if (players.isEmpty()) {
				msg += " The table is now empty.";
				reset(false);
			}

			Utilities.sendMessage(channel, msg);
		}
		else
			Utilities.sendMessage(channel, "You are currently not playing.");
	}

	/**
	 * Ends the game. The dealer's cards will get revealed and all statuses will be updated. reset(true) will be called
	 */
	protected void endGame() {
		if (roundMessage != null)
			roundMessage.delete().queue();

		int dealerVal = players.getDealer().getCards().value();

		while (dealerVal < 17) {
			players.getDealer().addCard(shoe.pull());
			dealerVal = players.getDealer().getCards().value();
		}

		if (dealerVal > 21)
			players.getDealer().setStatus(Status.BUST);

		for (Player p : players) {
			if (p.getStatus() != Status.BUST) {
				if (p.getCards().value() < dealerVal && players.getDealer().getStatus() != Status.BUST)
					p.setStatus(Status.BUST);
				else if (p.getCards().value() == dealerVal)
					p.setStatus(Status.TIE);
			}
		}
		Utilities.sendMessage(channel, players.build(true));
		reset(true);
	}

	/**
	 * Resets this round and starts a new one within 10 seconds if so desired
	 *
	 * @param restart true if the round should be restarted, false otherwise
	 */
	protected void reset(boolean restart) {
		players.reset();
		hasStarted = false;
		roundMessage = null;

		if (restart) {
			Utilities.sendMessage(channel, "New round starting in 10 seconds...");
			isStarting = true;
			Executors.newScheduledThreadPool(1).schedule(this::start, 10, TimeUnit.SECONDS);
		}
	}
}
