package bl4ckscor3.discord.bl4ckb0t.modules.blackjack;

public enum Status
{
	WAITING(""), //waiting for new round
	IDLE("⏰"), //waiting for turn
	ACTIVE("◀"), //has to take turn
	STAND("✅"), //done, no more card change or has won
	BJ("👑"), //blackjack!!
	BUST("❌"), //lost the game
	TIE("🤝"), // tied the game
	DEALING("🎰"); //dealer

	private String emoji;

	/**
	 * @param e The emoji that represents this status
	 */
	Status(String e)
	{
		emoji = e;
	}

	/**
	 * @return true if this status does not require any further action, false otherwhise
	 */
	public boolean isDone()
	{
		return this == Status.STAND || this == Status.BUST;
	}

	@Override
	public String toString()
	{
		return emoji;
	}
}
