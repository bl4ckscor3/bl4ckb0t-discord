package bl4ckscor3.discord.bl4ckb0t.module.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

/**
 * A hangman game. Holds the phrase itself, which letters of it have already been guessed, and the state of the hangman.
 */
class Game {
	protected char[] phrase;
	protected boolean[] guessed;
	protected LetterState[] letterStates = new LetterState[26];
	protected int hangman = -1;
	protected Message message;
	protected List<User> guessers = new ArrayList<>();
	protected String user;
	protected char lastGuessedLetter;
	protected String originalPhrase;

	/**
	 * Sets up this hangman game
	 *
	 * @param a The user who started this game
	 * @param p The phrase to guess
	 */
	protected Game(String u, String p) {
		user = u;
		originalPhrase = p;
		phrase = p.toLowerCase().toCharArray();
		guessed = new boolean[phrase.length];
		Arrays.fill(letterStates, LetterState.UNUSED);

		for (int i = 0; i < phrase.length; i++) {
			guessed[i] = !Character.isAlphabetic(phrase[i]);
		}
	}

	/**
	 * Sets the message that this game is being played in
	 *
	 * @param message The message to set
	 */
	protected void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * Checks whether the given character is in the phrase array and sets all positions x in the guessed array to true if it is
	 * at position x in the phrase array. Does not increase hangman
	 *
	 * @param c The character to check
	 * @return true if the character was present in the phrase array, false otherwise
	 */
	protected boolean guess(char c) {
		boolean correct = false;

		for (int i = 0; i < phrase.length; i++) {
			if (phrase[i] == Character.toLowerCase(c)) {
				guessed[i] = true;
				correct = true;
			}
		}

		return correct;
	}

	/**
	 * @return The phrase as it was sent in the direct message
	 */
	protected String getFullPhrase() {
		String fullPhrase = "";

		for (char c : phrase) {
			fullPhrase += c;
		}

		return fullPhrase;
	}

	/**
	 * Gets all letters with the given state in a formatted message
	 *
	 * @state The state which the letter has to be in to be included in the message
	 * @return The formatted message, characters are seperated by spaces
	 */
	protected String getLettersByState(LetterState state) {
		String result = "";

		for (int i = 0; i < letterStates.length; i++) {
			if (letterStates[i] != LetterState.UNUSED && letterStates[i] == state)
				result += Character.toString(i + 97);
		}

		return result;
	}

	/**
	 * Gets the game's status formatted as a message without any initial text and with showing the currently unsolved phrase
	 *
	 * @see {@link #getGameMessage(String, char, boolean)}
	 */
	protected String getGameMessage() {
		return getGameMessage("", '0', true);
	}

	/**
	 * Gets the game's status formatted as a message
	 *
	 * @param The text that this message should start with
	 * @param alreadyGuessedLetter If this is not '0', a text about this letter already having been guessed will be displayed
	 * @param showUnsolvedPhrase Whether the unsolved phrase should be displayed
	 * @return The message
	 */
	protected String getGameMessage(String initialText, char alreadyGuessedLetter, boolean showUnsolvedPhrase) {
		String result = "Started by: " + user + System.lineSeparator() + initialText;

		if (alreadyGuessedLetter != '0')
			result += String.format("You already guessed `%s`!%s", alreadyGuessedLetter, System.lineSeparator());

		if (showUnsolvedPhrase) {
			for (int i = 0; i < phrase.length; i++) {
				if (Character.isAlphabetic(phrase[i])) {
					if (guessed[i]) {
						if (phrase[i] == lastGuessedLetter)
							result += "**" + phrase[i] + "** ";
						else
							result += phrase[i] + " ";
					}
					else
						result += "\\_ ";
				}
				else if (phrase[i] == ' ')
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
	 *
	 * @return The string
	 */
	protected String getBuildProgressString() {
		if (hangman == -1)
			return "";
		else
			return "```" + switch (hangman) {
				//text blocks don't format the message nicely
				case 0 -> " _____\n" + "/     \\";
				case 1 -> "   |\n" + "   |\n" + "   |\n" + " __|__\n" + "/     \\";
				case 2 -> "   | /\n" + "   |/\n" + "   |\n" + " __|__\n" + "/     \\";
				case 3 -> "   ________\n" + "   | /\n" + "   |/\n" + "   |\n" + " __|__\n" + "/     \\";
				case 4 -> "   ________\n" + "   | /    |\n" + "   |/\n" + "   |\n" + " __|__\n" + "/     \\";
				case 5 -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |\n" + " __|__\n" + "/     \\";
				case 6 -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |      |\n" + " __|__\n" + "/     \\";
				case 7 -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |     \\|\n" + " __|__\n" + "/     \\";
				case 8 -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |     \\|/\n" + " __|__\n" + "/     \\";
				case 9 -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |     \\|/\n" + " __|__   /\n" + "/     \\";
				default -> "   ________\n" + "   | /    |\n" + "   |/     O\n" + "   |     \\|/\n" + " __|__   / \\\n" + "/     \\";
			} + "```";
	}
}
