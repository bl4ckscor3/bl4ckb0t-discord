package bl4ckscor3.discord.bl4ckb0t.modules.blackjack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RateLimitException;

public class Round implements IReactable
{
	public boolean hasStarted = false;
	public boolean isStarting = false;
	public Shoe shoe;
	public Players players;
	public IMessage roundMessage;
	public IChannel channel;
	public ScheduledFuture<?> removal;

	public Round(IChannel c)
	{
		if(shoe == null)
			shoe = new Shoe();

		if(players == null)
			players = new Players();

		channel = c;
	}

	/**
	 * Starts this round if players are present and it has not already started
	 */
	public void start()
	{
		if(hasStarted || players.isEmpty())
			return;

		isStarting = false;
		hasStarted = true;

		players.get(0).setActive();

		for(int i = 0; i < 2; i++)
		{
			players.getDealer().addCard(shoe.pull());

			for(Player p : players)
			{
				p.addCard(shoe.pull());
			}
		}

		boolean dealerHasBlackJack = players.getDealer().getCards().value() == 21;
		int playerBlackJacks = 0;

		for(Player p : players)
		{
			if(p.getCards().value() == 21)
			{
				p.setStatus(Status.BJ);
				playerBlackJacks++;
			}
		}

		while(players.getCurrentPlayer().getCards().value() == 21)
		{
			if(!players.next())
				break;
		}

		if(dealerHasBlackJack)
		{
			players.getDealer().setStatus(Status.BJ);

			for(Player p : players)
			{
				if(p.getStatus() == Status.BJ)
					p.setStatus(Status.TIE);
			}

			endGame();
		}
		else if(playerBlackJacks == players.size())
			endGame();
		else
			updateMessage();
	}

	@EventSubscriber
	public void onReactionAdd(ReactionAddEvent event)
	{
		removal.cancel(false);

		Player p = players.getCurrentPlayer();

		switch(event.getReaction().getEmoji().toString())
		{
			case "🎲":
				p.addCard(shoe.pull());

				if(p.getCards().value() > 21)
				{
					p.setStatus(Status.BUST);

					if(!players.next())
					{
						endGame();
						return;
					}
				}

				updateMessage();
				break;
			case "⛔":
				if(p.getCards().value() < players.getDealer().getCards().value())
					p.setStatus(Status.BUST);
				else
					p.setStatus(Status.STAND);

				if(!players.next())
				{
					try
					{
						Thread.sleep(250);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}

					endGame();
					break;
				}

				updateMessage();
				break;
		}
	}

	/**
	 * Resends the round message with new cards or player states
	 */
	private void updateMessage()
	{
		try
		{
			if(roundMessage != null)
				roundMessage.delete();

			roundMessage = Utilities.sendMessage(channel, players.build());
			Thread.sleep(250);
			Utilities.react(roundMessage, "🎲", "⛔");
			waitForReaction(roundMessage.getLongID(), players.getCurrentPlayer().getUser().getLongID());
			removal = Executors.newScheduledThreadPool(1).schedule(() -> {
				AWAITED_REACTIONS.remove(roundMessage.getLongID());
				Utilities.sendMessage(channel, "No decision in time. Removing **" + players.getCurrentPlayer().getUser().getName() + "** from the table.");
				leave(players.getCurrentPlayer().getUser());

				if(!players.isEmpty())
					updateMessage();
				else
				{
					Utilities.sendMessage(channel, "All players have left, the table is empty.");
					reset(false);
				}
			}, 120, TimeUnit.SECONDS);
		}
		catch(Exception e)
		{
			e.printStackTrace();

			try
			{
				Utilities.sendMessage(channel, "An error occured, see log for details. Round will be reset.");
			}
			catch(RateLimitException e2)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e3) {}

				Utilities.sendMessage(channel, "An error occured, see log for details. Round will be reset.");
			}

			reset(true);
			return;
		}
	}

	/**
	 * Adds a player to this round
	 * @param user The player to add
	 * @param status The initial {@link Status} the player should have.
	 * @return true if the player has joined, false otherwise
	 */
	public boolean join(IUser user, Status status)
	{
		if(!players.contains(user))
		{
			String msg = user.mention() + " joined the table!";

			players.add(new Player(user, status));

			if(hasStarted)
				msg += " You will be able to start next round.";

			Utilities.sendMessage(channel, msg);
			return true;
		}
		else
		{
			Utilities.sendMessage(channel, "You are already playing.");
			return false;
		}
	}

	/**
	 * Removes a player from this round
	 * @param user The player to remove
	 */
	public void leave(IUser user)
	{
		if(players.contains(user))
		{
			String msg = user.mention() + " left the table.";

			players.remove(user);

			if(players.isEmpty())
			{
				msg += " The table is now empty.";
				reset(false);
			}

			Utilities.sendMessage(channel, msg);
		}
		else Utilities.sendMessage(channel, "You are currently not playing.");
	}

	/**
	 * Ends the game. The dealer's cards will get revealed and all statuses will be updated. reset(true) will be called
	 */
	public void endGame()
	{
		if(roundMessage != null)
			roundMessage.delete();

		int dealerVal = players.getDealer().getCards().value();

		while(dealerVal < 17)
		{
			players.getDealer().addCard(shoe.pull());
			dealerVal = players.getDealer().getCards().value();
		}

		if(dealerVal > 21)
			players.getDealer().setStatus(Status.BUST);

		for(Player p : players)
		{
			if(p.getStatus() == Status.BUST)
				continue;
			else if(p.getCards().value() < dealerVal && players.getDealer().getStatus() != Status.BUST)
				p.setStatus(Status.BUST);
			else if(p.getCards().value() == dealerVal)
				p.setStatus(Status.TIE);
		}

		Utilities.sendMessage(channel, players.build(true));
		reset(true);
	}

	/**
	 * Resets this round and starts a new one within 10 seconds if so desired
	 * @param restart true if the round should be restarted, false otherwise
	 */
	public void reset(boolean restart)
	{
		players.reset();
		hasStarted = false;
		roundMessage = null;

		if(restart)
		{
			Utilities.sendMessage(channel, "New round starting in 10 seconds...");
			isStarting = true;
			Executors.newScheduledThreadPool(1).schedule(() -> {
				start();
			}, 10, TimeUnit.SECONDS);
		}
	}
}
