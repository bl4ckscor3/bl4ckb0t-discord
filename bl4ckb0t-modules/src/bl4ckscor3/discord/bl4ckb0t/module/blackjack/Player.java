package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import net.dv8tion.jda.api.entities.User;

class Player {
	protected User user = null;
	protected final Cards cards = new Cards();
	protected Status status;
	protected boolean active;

	/**
	 * Only use this if you want to create the dealer
	 */
	protected Player() {
		status = Status.DEALING;
	}

	/**
	 * Creates a new player
	 *
	 * @param u The {@link sx.blah.discord.handle.obj.IUser} that is this player
	 * @param s The initial {@link Status} to set
	 */
	protected Player(User u, Status s) {
		user = u;
		status = s;
		active = (s == Status.ACTIVE);
	}

	/**
	 * @return The user that is this player
	 */
	protected User getUser() {
		return user;
	}

	/**
	 * @return true if this player is the dealer, false otherwise
	 */
	protected boolean isDealer() {
		return false;
	}

	/**
	 * Adds a card to this player's open cards
	 *
	 * @param c The card to add
	 */
	protected void addCard(Card c) {
		cards.addCard(c);
	}

	/**
	 * Sets this player's status
	 *
	 * @status The {@link Status} to set
	 */
	protected void setStatus(Status s) {
		status = s;

		if (s == Status.ACTIVE)
			active = true;
	}

	/**
	 * Resets this player's cards and status to make them ready for the next round
	 */
	protected void reset() {
		cards.clear();
		status = isDealer() ? Status.DEALING : Status.IDLE;
	}

	/**
	 * @return This player's open cards
	 */
	protected Cards getCards() {
		return cards;
	}

	/**
	 * @return This player's playing status
	 */
	protected Status getStatus() {
		return status;
	}

	/**
	 * Sets this user to be active
	 */
	protected void setActive() {
		active = true;
		setStatus(Status.ACTIVE);
	}

	/**
	 * If a user is active, they are allowed to play in the current round
	 *
	 * @return true if the user is active, false otherwise
	 */
	protected boolean isActive() {
		return active;
	}

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Player player && player.user.getIdLong() == user.getIdLong() && player.cards.equals(cards) && player.status.equals(status) && player.active == active;
	}
}
