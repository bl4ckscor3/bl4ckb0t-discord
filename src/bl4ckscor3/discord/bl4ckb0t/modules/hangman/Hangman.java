package bl4ckscor3.discord.bl4ckb0t.modules.hangman;

import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import bl4ckscor3.discord.bl4ckb0t.modules.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class Hangman extends AbstractModule implements IRequestDM
{
	public HashMap<IChannel,Word> words = new HashMap<IChannel,Word>();

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		IChannel channel = event.getChannel();

		if(event.getMessage().getContent().startsWith("-hangman"))
		{
			if(words.containsKey(channel))
			{
				Utilities.sendMessage(channel, "This channel already has a word being guessed at the moment.");
				return;
			}

			HashMap<String,Object> info = new HashMap<String,Object>();

			info.put("instance", this);
			info.put("channel", channel);

			waitForDM(event.getAuthor().getLongID(), info);
			Utilities.sendMessage(channel, "Ok! Send me a word via DM and I'll take care of everything else. Guesses can be given via `.LETTER`. E.g.: `.A` to guess the letter `A`");
		}
		else if(event.getMessage().getContent().startsWith(".") && event.getMessage().getContent().toCharArray().length == 2)
		{
			if(words.containsKey(channel))
			{
				Word word = words.get(channel);
				char guess = event.getMessage().getContent().charAt(1);
				String sentence = "";

				if(ArrayUtils.contains(word.used, guess))
				{
					Utilities.sendMessage(channel, "You already guessed this!" + System.lineSeparator() + word.toString());
					return;
				}
				else
					word.used[Character.toUpperCase(guess) - 65] = guess;

				if(!word.guess(guess))
				{
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
							String solution = "";

							for(char c : word.word)
							{
								solution += c;
							}

							sentence = "Your right leg appears. You lost. The word was: " + solution;
							words.remove(channel);
							break;
						}
					}

					sentence += System.lineSeparator() + word.toString();
					Utilities.sendMessage(channel, sentence);
				}
				else
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

					sentence += word.toString();

					if(won)
					{
						words.remove(channel);
						sentence += System.lineSeparator() + "You win! A new word can now be submitted.";
					}

					Utilities.sendMessage(channel, sentence);
				}
			}
		}
	}

	@Override
	public void onDMReceived(MessageReceivedEvent event, HashMap<String,Object> info)
	{
		for(char c : event.getMessage().getContent().toLowerCase().toCharArray())
		{
			if(!Character.isAlphabetic(c) && c != ' ')
			{
				Utilities.sendMessage(event.getChannel(), "Only letters are allowed.");
				return;
			}
		}

		IChannel channel = (IChannel)info.get("channel");
		Word word = new Word(event.getMessage().getContent().toLowerCase());

		words.put(channel, word);
		Utilities.sendMessage(channel, "New hangman game started by " + event.getAuthor().mention() + ": " + word.toString());
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().equals("-hangman") ||
				(event.getMessage().getContent().startsWith(".") && event.getMessage().getContent().toCharArray().length == 2 && Character.isAlphabetic(event.getMessage().getContent().charAt(1)));
	}
}
