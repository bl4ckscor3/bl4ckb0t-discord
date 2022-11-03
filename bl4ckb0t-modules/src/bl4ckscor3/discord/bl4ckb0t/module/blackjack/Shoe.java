package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Shoe {
	private final FullDeck deck = new FullDeck();
	private final LinkedList<Card> shoe = new LinkedList<>();

	/**
	 * Randomly selects cards from six full decks to put into the shoe and selects a cutting card
	 */
	public Shoe() {
		reset();
	}

	/**
	 * Pulls the first card out of the shoe and returns it
	 *
	 * @return The first card sitting in the shoe
	 */
	public Card pull() {
		if (shoe.size() == 0)
			reset();

		return shoe.removeFirst();
	}

	/**
	 * Resets this shoe
	 */
	public void reset() {
		for (int i = 0; i < deck.size() * 6; i++) {
			shoe.add(deck.pull());
		}

		shoe.add(ThreadLocalRandom.current().nextInt(shoe.size() / 2, shoe.size()), new Card()); //set the cutting card which will be used when the shoe needs changing
	}
}
