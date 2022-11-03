package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Players extends ArrayList<Player> {
	private Dealer dealer;
	private int currentPlayer = 0;

	/**
	 * Initializes this list with a dealer
	 */
	public Players() {
		dealer = new Dealer();
	}

	/**
	 * @return The dealer for these players
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @return The player to make the next turn, null if no player (round over)
	 */
	public Player getCurrentPlayer() {
		return currentPlayer >= size() ? null : get(currentPlayer);
	}

	/**
	 * Gets the next player to take their turn
	 *
	 * @return The next player that can take action, null if there is no such player (round over)
	 */
	public Player getNextPlayer() {
		for (int i = currentPlayer; i < size(); i++) {
			if (getCurrentPlayer().getStatus().isDone())
				continue;

			return get(i);
		}

		return null;
	}

	/**
	 * Schedules the next player to take their turn
	 *
	 * @return true if there is a next player false if not (aka round is over)
	 */
	public boolean next() {
		if (currentPlayer == size() - 1)
			return false;

		for (int i = currentPlayer; i < size(); i++) {
			if (get(i).getStatus().isDone() || get(i).getStatus() == Status.WAITING)
				continue;

			currentPlayer = i;
			getCurrentPlayer().setStatus(Status.ACTIVE);
			return true;
		}

		return false;
	}

	/**
	 * Prepares an {@link sx.blah.discord.api.internal.json.objects.EmbedObject} which displays the state of the current
	 * round without a title
	 *
	 * @see {@link Players#build}
	 */
	public MessageEmbed build() {
		return build(false);
	}

	/**
	 * Prepares an {@link sx.blah.discord.api.internal.json.objects.EmbedObject} which displays the state of the current
	 * round. This method will also set a player's status to {@link Status.STAND} if they have a blackjack from the beginning
	 *
	 * @param ended Wether the round end title should be shown or not
	 * @return An EmbedObject which contains all card information for the dealer and players including the information on who
	 *         needs to react
	 */
	public MessageEmbed build(boolean ended) {
		EmbedBuilder builder = new EmbedBuilder().setColor(0x000001);
		User user = getCurrentPlayer().getUser();

		if (ended)
			builder.setTitle("Round has ended! End results:");

		int dealerValue = dealer.getCards().value();

		builder.addField("Dealer's cards (" + (dealer.getCards().amount() == 2 && !ended ? dealer.getCards().get(0).getRank().getValue() : (dealerValue == 21 ? "Black Jack!" : dealerValue)) + ") " + dealer.getStatus().toString(), dealer.toString(ended), false);

		for (Player p : this) {
			if (p.getCards().amount() != 0) {
				int count = p.getCards().value();
				String val = count + "";

				if (p.getCards().amount() == 2 && count == 21)
					val = "Black Jack!";

				builder.addField(p.getUser().getName() + (p.getUser().getName().endsWith("s") || p.getUser().getName().endsWith("z") ? "'" : "'s") + " cards (" + val + ") " + p.getStatus().toString(), p.toString(), false);
			}
		}

		if (ended)
			builder.setFooter("Game is done!");
		else
			builder.setFooter("It's " + user.getName() + (user.getName().toLowerCase().endsWith("s") || user.getName().toLowerCase().endsWith("z") ? "'" : "'s") + " turn");

		return builder.build();
	}

	/**
	 * Checks if this list contains an {@link sx.blah.discord.handle.obj.IUser}
	 *
	 * @param u The {@link sx.blah.discord.handle.obj.IUser} to check
	 * @return true if the {@link sx.blah.discord.handle.obj.IUser} is in this list, false otherwise
	 */
	public boolean contains(User u) {
		for (Player p : this) {
			if (p.getUser().equals(u))
				return true;
		}

		return false;
	}

	/**
	 * Checks if this list contains a {@link sx.blah.discord.handle.obj.IUser} and removes the corresponding player
	 *
	 * @param u The {@link sx.blah.discord.handle.obj.IUser} to check and remove
	 * @return true if the {@link sx.blah.discord.handle.obj.IUser} is in this list and got removed, false otherwise
	 */
	public boolean remove(User u) {
		for (Player p : this) {
			if (p.getUser().equals(u))
				return remove(p);
		}

		return false;
	}

	/**
	 * Resets the dealer and all players to make them ready for the next round
	 */
	public void reset() {
		currentPlayer = 0;
		dealer.reset();

		for (Player p : this) {
			p.reset();
		}
	}
}
