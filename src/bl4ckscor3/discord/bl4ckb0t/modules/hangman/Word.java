package bl4ckscor3.discord.bl4ckb0t.modules.hangman;

/**
 * A hangman-style word. Holds the word itself, which letters of it have already been guessed, and the state of the hangman.
 */
public class Word
{
	public char[] word;
	public boolean[] guessed;
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
			guessed[i] = false;
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

	@Override
	public String toString()
	{
		String result = "";

		for(int i = 0; i < word.length; i++)
		{
			if(guessed[i])
				result += word[i] + " ";
			else
				result += "\\_ ";
		}

		return result;
	}
}
