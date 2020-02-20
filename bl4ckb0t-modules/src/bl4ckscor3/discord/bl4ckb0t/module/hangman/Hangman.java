package bl4ckscor3.discord.bl4ckb0t.module.hangman;

import java.util.Arrays;
import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class Hangman extends AbstractModule implements IRequestDM
{
	public HashMap<MessageChannel,Word> words = new HashMap<>();
	public static final String ALREADY_GUESSING = "This channel already has a word being guessed at the moment.";

	public Hangman(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		MessageChannel channel = event.getChannel();

		if(event.getMessage().getContentRaw().startsWith("-hangman"))
		{
			if(words.containsKey(channel))
			{
				if(args.length != 0)
				{
					Word word = words.get(channel);
					String alphabeticWord = Utilities.filterAlphabetic(word.getWord());
					String alphabeticGuess = Utilities.filterAlphabetic(Arrays.toString(args).replace(",", "").replace("[", "").replace("]", "")).toLowerCase();

					if(alphabeticWord.length() == alphabeticGuess.length())
					{
						for(int i = 0; i < alphabeticWord.length(); i++)
						{
							if(alphabeticWord.charAt(i) != alphabeticGuess.charAt(i))
							{
								wrongGuess(channel, word);
								return;
							}
						}

						correctGuess(channel, word, true);
					}
					else
						wrongGuess(channel, word);

					return;
				}

				Utilities.sendMessage(channel, ALREADY_GUESSING);
				return;
			}

			HashMap<String,Object> info = new HashMap<>();

			info.put("channel", channel);
			waitForDM(event.getAuthor().getIdLong(), info);
			Utilities.sendMessage(channel, "Ok! Send me a word via DM and I'll take care of everything else. Guesses can be given via `.LETTER`. E.g.: `.A` to guess the letter `A`. To guess a whole word, use `-hangman word`.");
		}
		else if(event.getMessage().getContentRaw().startsWith(".") && event.getMessage().getContentRaw().toCharArray().length == 2)
		{
			if(words.containsKey(channel))
			{
				Word word = words.get(channel);
				char guess = event.getMessage().getContentRaw().charAt(1);

				for(char c : word.used)
				{
					if(c == guess)
					{
						Utilities.sendMessage(channel, "You already guessed this!" + System.lineSeparator() + word.toString());
						return;
					}
				}

				word.used[Character.toUpperCase(guess) - 65] = guess;

				if(!word.guess(guess))
					wrongGuess(channel, word);
				else
					correctGuess(channel, word, false);
			}
		}
	}

	/**
	 * Will increase the hangman state and send a message with the current hangman state to the given channel
	 * @param channel The channel
	 * @param word The word being guessed in that channel
	 */
	private void wrongGuess(MessageChannel channel, Word word)
	{
		String sentence = "";

		word.hangman++;

		switch(word.hangman)
		{
			case 0: sentence = "The hill has been built."; break;
			case 1: sentence = "The base beam has been built."; break;
			case 2: sentence = "The top beam has been built."; break;
			case 3: sentence = "The connecting beam has been built."; break;
			case 4: sentence = "The rope is now hanging down."; break;
			case 5: sentence = "Your head appears."; break;
			case 6: sentence = "Your body appears."; break;
			case 7: sentence = "Your left arm appears."; break;
			case 8: sentence = "Your right arm appears."; break;
			case 9: sentence = "Your left leg appears."; break;
			case 10:
			{
				Utilities.sendMessage(channel, String.format("Your right leg appears. You lost! The word was: **%s**%sA new word can now be submitted.", word.getWord(), System.lineSeparator()));
				words.remove(channel);
				return;
			}
		}

		sentence += System.lineSeparator() + word.toString();
		Utilities.sendMessage(channel, sentence);
	}

	/**
	 * Will check wether the correct guess results in a win or not
	 * @param channel The channel
	 * @param word The word being guessed in that channel
	 * @param forceWin Forces a win. Useful if the word has been guessed in advance
	 */
	private void correctGuess(MessageChannel channel, Word word, boolean forceWin)
	{
		boolean won = true;

		for(boolean b : word.guessed)
		{
			if(!b)
			{
				won = false;
				break;
			}
		}

		if(won || forceWin)
		{
			words.remove(channel);
			Utilities.sendMessage(channel, String.format("You win! The word was: **%s**%sA new word can now be submitted.", word.getWord(), System.lineSeparator()));
		}
		else
			Utilities.sendMessage(channel, word.toString());
	}

	@Override
	public void onDMReceived(PrivateMessageReceivedEvent event, HashMap<String,Object> info)
	{
		MessageChannel channel = (MessageChannel)info.get("channel");

		if(!words.containsKey(channel))
		{
			Word word = new Word(event.getMessage().getContentRaw().toLowerCase());

			words.put(channel, word);
			Utilities.sendMessage(channel, "New hangman game started by " + event.getAuthor().getAsMention() + ": " + word.toString());
		}
		else
			Utilities.sendMessage(channel, ALREADY_GUESSING);
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().startsWith("-hangman") ||
				(event.getMessage().getContentRaw().startsWith(".") && event.getMessage().getContentRaw().toCharArray().length == 2 && Character.isAlphabetic(event.getMessage().getContentRaw().charAt(1)));
	}
}
