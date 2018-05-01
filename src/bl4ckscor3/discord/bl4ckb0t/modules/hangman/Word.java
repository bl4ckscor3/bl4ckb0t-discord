package bl4ckscor3.discord.bl4ckb0t.modules.hangman;

/**
 * A hangman-style word. Holds the word itself, which letters of it have already been guessed, and the state of the hangman.
 */
public class Word
{
	public char[] word;
	public boolean[] guessed;
	public char[] used = new char[26];
	public int hangman = -1;

	/**
	 * Sets up this word
	 * @param w The word
	 */
	public Word(String w)
	{
		word = w.toCharArray();
		guessed = new boolean[word.length];

		for(int i = 0; i < word.length; i++)
		{
			guessed[i] = word[i] == ' ';
		}
	}

	/**
	 * Checks wether the given character is in the word array and sets all positions x in the guessed array to true if it is at position x in the word array
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

		if(!correct)
			hangman++;

		return correct;
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

	@Override
	public String toString()
	{
		String result = "";

		for(int i = 0; i < word.length; i++)
		{
			if(guessed[i])
				result += word[i] + " ";
			else if(word[i] == ' ')
				result += "  ";
			else
				result += "\\_ ";
		}

		String used = usedToString();

		if(!used.equals(""))
			result += System.lineSeparator() + "Guessed letters: " + usedToString();
		return result;
	}
}
