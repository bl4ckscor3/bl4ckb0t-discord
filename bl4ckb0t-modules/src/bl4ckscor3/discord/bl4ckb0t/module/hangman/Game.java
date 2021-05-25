package bl4ckscor3.discord.bl4ckb0t.module.hangman;

import java.util.ArrayList;
import java.util.Arrays;
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
	public LetterState[] letterStates = new LetterState[26];
	public int hangman = -1;
	public Message message;
	public List<User> guessers = new ArrayList<>();
	public String user;
	public char lastGuessedLetter;
	public String originalWord;

	/**
	 * Sets up this hangman game
	 * @param a The user who started this game
	 * @param w The word to guess
	 */
	public Game(String u, String w)
	{
		user = u;
		originalWord = w;
		word = w.toLowerCase().toCharArray();
		guessed = new boolean[word.length];
		Arrays.fill(letterStates, LetterState.UNUSED);

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
	 * Gets all letters with the given state in a formatted message
	 * @state The state which the letter has to be in to be included in the message
	 * @return The formatted message, characters are seperated by spaces
	 */
	public String getLettersByState(LetterState state)
	{
		String result = "";

		for(int i = 0; i < letterStates.length; i++)
		{
			if(letterStates[i] == LetterState.UNUSED)
				continue;
			else if(letterStates[i] == state)
				result += Character.toString(i + 97);
		}

		return result;
	}

	/**
	 * Gets the game's status formatted as a message without any initial text and with showing the currently unsolved word
	 * @see {@link #getGameMessage(String, char, boolean)}
	 */
	public String getGameMessage()
	{
		return getGameMessage("", '0', true);
	}

	/**
	 * Gets the game's status formatted as a message
	 * @param The text that this message should start with
	 * @param alreadyGuessedLetter If this is not '0', a text about this letter already having been guessed will be displayed
	 * @param showUnsolvedWord Whether the unsolved word should be displayed
	 * @return The message
	 */
	public String getGameMessage(String initialText, char alreadyGuessedLetter, boolean showUnsolvedWord)
	{
		String result = "Started by: " + user + System.lineSeparator() + initialText;

		if(alreadyGuessedLetter != '0')
			result += String.format("You already guessed `%s`!%s", alreadyGuessedLetter, System.lineSeparator());

		if(showUnsolvedWord)
		{
			for(int i = 0; i < word.length; i++)
			{
				if(Character.isAlphabetic(word[i]))
				{
					if(guessed[i])
					{
						if(word[i] == lastGuessedLetter)
							result += "**" + word[i] + "** ";
						else
							result += word[i] + " ";
					}
					else
						result += "\\_ ";
				}
				else if(word[i] == ' ')
					result += "  ";
			}

			result += System.lineSeparator();
		}

		result += getBuildProgressString() + System.lineSeparator();
		result += "Wrong guesses: " + getLettersByState(LetterState.WRONG) + System.lineSeparator();
		result += "Correct guesses: " + getLettersByState(LetterState.CORRECT) + System.lineSeparator();
		result += "Guessers: " + guessers.stream().map(u -> u.getAsMention()).collect(Collectors.joining(", "));
		return result;
	}

	/**
	 * Gets a string saying what part of the hangman structure was last built
	 * @return The string
	 */
	public String getBuildProgressString()
	{
		if(hangman == -1)
			return "";
		else return "```" + switch(hangman) {
			//text blocks don't format the message nicely
			case 0 ->
			" _____\n" +
			"/     \\";
			case 1 ->
			"   |\n" +
			"   |\n" +
			"   |\n" +
			" __|__\n" +
			"/     \\";
			case 2 ->
			"   | /\n" +
			"   |/\n" +
			"   |\n" +
			" __|__\n" +
			"/     \\";
			case 3 ->
			"   ________\n" +
			"   | /\n" +
			"   |/\n" +
			"   |\n" +
			" __|__\n" +
			"/     \\";
			case 4 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/\n" +
			"   |\n" +
			" __|__\n" +
			"/     \\";
			case 5 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |\n" +
			" __|__\n" +
			"/     \\";
			case 6 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |      |\n" +
			" __|__\n" +
			"/     \\";
			case 7 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |     \\|\n" +
			" __|__\n" +
			"/     \\";
			case 8 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |     \\|/\n" +
			" __|__\n" +
			"/     \\";
			case 9 ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |     \\|/\n" +
			" __|__   /\n" +
			"/     \\";
			default ->
			"   ________\n" +
			"   | /    |\n" +
			"   |/     O\n" +
			"   |     \\|/\n" +
			" __|__   / \\\n" +
			"/     \\";
		} + "```";
	}
}
