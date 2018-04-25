package bl4ckscor3.discord.bl4ckb0t.modules.blackjack;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import bl4ckscor3.discord.bl4ckb0t.modules.blackjack.Card.Ranks;
import bl4ckscor3.discord.bl4ckb0t.modules.blackjack.Card.Suits;

public class FullDeck
{
	private final ArrayList<Card> deck = new ArrayList<Card>();

	/**
	 * Initializes a full deck of all possible cards and puts them into {@link FullDeck#deck}
	 */
	public FullDeck()
	{
		newDeck();
	}

	/**
	 * Initializes a full deck of all possible cards and puts them into {@link FullDeck#deck}
	 */
	public void newDeck()
	{
		deck.clear(); //just to be safe

		for(Suits s : Suits.values())
		{
			for(Ranks r : Ranks.values())
			{
				deck.add(new Card(r, s));
			}
		}
	}

	/**
	 * Pulls a random card from the {@link FullDeck#deck} and removes it. Will create a new {@link FullDeck#deck} if the current one is empty
	 * @return The random card
	 */
	public Card pull()
	{
		if(deck.size() == 0)
			newDeck();

		return deck.remove(ThreadLocalRandom.current().nextInt(deck.size()));
	}

	/**
	 * @return The size of this deck
	 */
	public int size()
	{
		return deck.size();
	}
}
