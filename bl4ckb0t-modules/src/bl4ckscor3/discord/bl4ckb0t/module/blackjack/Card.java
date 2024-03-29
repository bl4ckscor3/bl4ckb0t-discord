package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

class Card {
	private Rank rank;
	private Suit suit;
	private boolean cuttingCard = false;

	/**
	 * Sets this card to be a cutting card
	 */
	protected Card() {
		cuttingCard = true;
	}

	/**
	 * Initializes a new playing card
	 *
	 * @param r The rank of the card
	 * @param s The suit of the card
	 */
	protected Card(Rank r, Suit s) {
		rank = r;
		suit = s;
	}

	/**
	 * @return The card's rank
	 */
	protected Rank getRank() {
		return rank;
	}

	/**
	 * @return The card's suit
	 */
	protected Suit getSuit() {
		return suit;
	}

	/**
	 * @return True if this card is a cutting card, false otherwise
	 */
	protected boolean isCuttingCard() {
		return cuttingCard;
	}

	/**
	 * @return This card's value
	 */
	protected int value() {
		return rank.value;
	}

	@Override
	public String toString() {
		return !isCuttingCard() ? suit.toString() + " " + rank.toString() : "Cutting Card";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Card card && card.rank == rank && card.suit == suit && card.cuttingCard == cuttingCard;
	}

	protected enum Rank {
		TWO(2, "2"),
		THREE(3, "3"),
		FOUR(4, "4"),
		FIVE(5, "5"),
		SIX(6, "6"),
		SEVEN(7, "7"),
		EIGHT(8, "8"),
		NINE(9, "9"),
		TEN(10, "10"),
		JACK(10, "J"),
		QUEEN(10, "Q"),
		KING(10, "K"),
		ACE(11, "A"); //ace can also be 1, but that is handled in the game logic

		private int value;
		private String string;

		Rank(int v, String r) {
			value = v;
			string = r;
		}

		protected int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return string;
		}
	}

	protected enum Suit {
		HEARTS("♥️"),
		CLUBS("♣️"),
		DIAMONDS("♦️"),
		SPADES("♠️");

		private String string;

		Suit(String r) {
			string = r;
		}

		@Override
		public String toString() {
			return string;
		}
	}
}
