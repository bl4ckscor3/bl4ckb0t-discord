package bl4ckscor3.discord.bl4ckb0t.module.hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

/**
 * A hangman game. Holds the word itself, which letters of it have already been guessed, and the state of the hangman.
 */
public class Game
{
	public char[] word;
	public boolean[] guessed;
	public char[] used = new char[26];
	public int hangman = -1;
	public Message message;
	public List<User> guessers = new ArrayList<>();

	/**
	 * Sets up this word
	 * @param w The word
	 */
	public Game(String w)
	{
		word = w.toCharArray();
		guessed = new boolean[word.length];

		for(int i = 0; i < word.length; i++)
		{
			guessed[i] = !Character.isAlphabetic(word[i]);
		}
	}

	/**
	 * Sets the message that this game is being played in
	 * @param message The message to set
	 */
	public void setMessage(Message message)
	{
		this.message = message;
	}

	/**
	 * Checks whether the given character is in the word array and sets all positions x in the guessed array to true if it is at position x in the word array. Does not increase hangman
	 * @param c The character to check
	 * @return true if the character was present in the word array, false otherwise
	 */
	public boolean guess(char c)
	{
		boolean correct = false;

		for(int i = 0; i < word.length; i++)
		{
			if(word[i] == Character.toLowerCase(c))
			{
				guessed[i] = true;
				correct = true;
			}
		}

		return correct;
	}

	/**
	 * @return The word as it was sent in the direct message
	 */
	public String getWord()
	{
		String word = "";

		for(char c : this.word)
		{
			word += c;
		}

		return word;
	}

	/**
	 * toString method for the used array in this class
	 * @return The used array to string, stripped out of all null characters, characters are seperated by spaces
	 */
	public String usedToString()
	{
		String result = "";

		for(int i = 0; i < used.length; i++)
		{
			if(used[i] == '\0')
				continue;

			result += used[i] + " ";
		}

		return result;
	}

	/**
	 * Gets the game's status formatted as a message
	 * @param alreadyGuessed Whether text about a letter already having been guessed should be displayed
	 * @return The message
	 */
	public String getGameMessage(char alreadyGuessedLetter)
	{
		String result = "";

		if(alreadyGuessedLetter != '0')
			result += String.format("You already guessed `%s`!%s", alreadyGuessedLetter, System.lineSeparator());


		result += getBuildProgressString() + System.lineSeparator();

		for(int i = 0; i < word.length; i++)
		{
			if(Character.isAlphabetic(word[i]))
			{
				if(guessed[i])
					result += word[i] + " ";
				else
					result += "\\_ ";
			}
			else if(word[i] == ' ')
				result += "  ";
		}

		String used = usedToString();

		if(!used.equals(""))
			result += System.lineSeparator() + "Guessed letters: " + usedToString();

		return result + System.lineSeparator() + getGuessers();
	}

	/**
	 * Gets a formatted list of people who have guessed in this game
	 * @return The formatted list
	 */
	public String getGuessers()
	{
		return "Guessers: " + guessers.stream().map(u -> u.getAsMention()).collect(Collectors.joining(", "));
	}

	/**
	 * Gets a string saying what part of the hangman structure was last built
	 * @return The string
	 */
	public String getBuildProgressString()
	{
		switch(hangman)
		{
			case 0: return "The hill has been built.";
			case 1: return "The base beam has been built.";
			case 2: return "The top beam has been built.";
			case 3: return "The connecting beam has been built.";
			case 4: return "The rope is now hanging down.";
			case 5: return "Your head appears.";
			case 6: return "Your body appears.";
			case 7: return "Your left arm appears.";
			case 8: return "Your right arm appears.";
			case 9: return "Your left leg appears.";
			default: return "";
		}
	}
}
