package bl4ckscor3.discord.bl4ckb0t.modules.blackjack;

public class Dealer extends Player
{
	/**
	 * Only use this if you want to create the dealer
	 */
	public Dealer()
	{
		super();
	}

	/**
	 * @return true if this player is the dealer, false otherwise
	 */
	public boolean isDealer()
	{
		return true;
	}

	/**
	 * Sets this player's status
	 * @status The {@link Status} to set
	 */
	public void setStatus(Status s)
	{
		if(s != Status.DRAW && s != Status.IDLE && s != Status.STAND && s != Status.WAITING && s != Status.ACTIVE)
			status = s;
	}

	/**
	 * Resets this player's cards and status to make them ready for the next round
	 */
	public void reset()
	{
		cards.clear();
		status = Status.DEALING;
	}

	/**
	 * Sets this user to be active
	 */
	public void setActive()
	{
		active = false;
	}

	/**
	 * If a user is active, they are allowed to play in the current round
	 * @return true if the user is active, false otherwise
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * Will return a string consisting of only the first card if the game is still ongoing and containing all cards if the argument is set accordingly
	 * @param ended true if the game has ended and all cards should be shown, false otherwise
	 * @return The string as described
	 */
	public String toString(boolean ended)
	{
		return cards.amount() == 2 && !ended ? cards.get(0).toString() + "   |   ??" : cards.toString();
	}
}
