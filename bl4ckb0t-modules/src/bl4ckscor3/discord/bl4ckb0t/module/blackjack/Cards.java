package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.ArrayList;
import java.util.Arrays;

public class Cards
{
	private ArrayList<Card> cards = new ArrayList<Card>();
	private int score;

	/**
	 * Adds a card to the cards
	 * @param c The card to add
	 */
	public void addCard(Card c)
	{
		cards.add(c);
		score = value();
	}

	/**
	 * @return The summed up score of all cards in {@link Cards#cards}
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * @return The amount of cards in {@link Cards#cards}
	 */
	public int amount()
	{
		return cards.size();
	}

	/**
	 * Clears the cards from the list and resets the score
	 */
	public void clear()
	{
		cards.clear();
		score = 0;
	}

	/**
	 * @see {@link java.util.ArrayList#get(int)}
	 */
	public Card get(int i)
	{
		return cards.get(i);
	}

	/**
	 * Sums up the values of all cards in {@link Cards#cards}. An ace will get a value of 11 if the total is smaller than 22, otherwise it will have a value of 1.
	 * For instance, having a 2, 5 and an ace will yield 18 as a result, but having a 10, 2 and an ace will yield 13.
	 * @param cards The cards to count
	 * @return The value of all cards added together
	 */
	public int value()
	{
		int total = 0;
		boolean ace = false;

		Arrays.sort(cards.toArray(new Card[cards.size()]), (c1, c2) -> {
			return c1.getRank().getValue() < c2.getRank().getValue() ? -1 : (c1.getRank().getValue() == c2.getRank().getValue() ? 0 : 1);
		});

		for(Card c : cards)
		{
			if(c.getRank().getValue() < 11)
				total += c.getRank().getValue();
			else if(c.getRank().getValue() == 11 && total + 11 <= 21)
			{
				total += 11;
				ace = true;;
			}
			else
			{
				if(!ace)
					total += 1;
				else
				{
					ace = false;
					total -= 9; //remove value of previous ace

					if(total + 11 <= 21)
					{
						total += 10;
						ace = true;
					}
				}
			}
		}

		return total;
	}

	@Override
	public String toString()
	{
		String total = "";

		for(Card c : cards)
		{
			total += c.toString() + "   |   ";
		}

		return total.substring(0, total.lastIndexOf('|')).trim();
	}
}
