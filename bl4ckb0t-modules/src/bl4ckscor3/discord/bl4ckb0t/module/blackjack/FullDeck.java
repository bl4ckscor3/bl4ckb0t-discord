package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import bl4ckscor3.discord.bl4ckb0t.module.blackjack.Card.Rank;
import bl4ckscor3.discord.bl4ckb0t.module.blackjack.Card.Suit;

class FullDeck {
	private final ArrayList<Card> deck = new ArrayList<>();

	/**
	 * Initializes a full deck of all possible cards and puts them into {@link FullDeck#deck}
	 */
	protected FullDeck() {
		newDeck();
	}

	/**
	 * Initializes a full deck of all possible cards and puts them into {@link FullDeck#deck}
	 */
	protected void newDeck() {
		deck.clear(); //just to be safe

		for (Suit s : Suit.values()) {
			for (Rank r : Rank.values()) {
				deck.add(new Card(r, s));
			}
		}
	}

	/**
	 * Pulls a random card from the {@link FullDeck#deck} and removes it. Will create a new {@link FullDeck#deck} if the current
	 * one is empty
	 *
	 * @return The random card
	 */
	protected Card pull() {
		if (deck.isEmpty())
			newDeck();

		return deck.remove(ThreadLocalRandom.current().nextInt(deck.size()));
	}

	/**
	 * @return The size of this deck
	 */
	protected int size() {
		return deck.size();
	}
}
