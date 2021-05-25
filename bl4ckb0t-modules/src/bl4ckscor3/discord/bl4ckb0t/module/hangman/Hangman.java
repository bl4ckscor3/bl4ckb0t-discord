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
	public HashMap<MessageChannel,Game> games = new HashMap<>();
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
			if(games.containsKey(channel))
			{
				if(args.length != 0)
				{
					Game game = games.get(channel);
					String alphabeticWord = Utilities.filterAlphabetic(game.getWord());
					String alphabeticGuess = Utilities.filterAlphabetic(Arrays.toString(args).replace(",", "").replace("[", "").replace("]", "")).toLowerCase();

					if(!game.guessers.contains(event.getAuthor()))
						game.guessers.add(event.getAuthor());

					event.getMessage().delete().queue();

					if(alphabeticWord.length() == alphabeticGuess.length())
					{
						for(int i = 0; i < alphabeticWord.length(); i++)
						{
							if(alphabeticWord.charAt(i) != alphabeticGuess.charAt(i))
							{
								wrongGuess(channel, game);
								return;
							}
						}

						correctGuess(channel, game, true);
					}
					else
						wrongGuess(channel, game);

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
			if(games.containsKey(channel))
			{
				Game game = games.get(channel);
				char guess = event.getMessage().getContentRaw().charAt(1);

				if(!game.guessers.contains(event.getAuthor()))
					game.guessers.add(event.getAuthor());

				event.getMessage().delete().queue();

				for(char c : game.used)
				{
					if(c == guess)
					{
						game.message.editMessage(game.getGameMessage(c)).queue();
						return;
					}
				}

				game.used[Character.toUpperCase(guess) - 65] = guess;

				if(!game.guess(guess))
					wrongGuess(channel, game);
				else
					correctGuess(channel, game, false);
			}
		}
	}

	/**
	 * Will increase the hangman state and send a message with the current hangman state to the given channel
	 * @param channel The channel
	 * @param game The hangman game that channel
	 */
	private void wrongGuess(MessageChannel channel, Game game)
	{
		game.hangman++;

		if(game.hangman == 10)
		{
			game.message.editMessage(String.format("Your right leg appears. You lost! The word was: **%s**%s%s%sA new word can now be submitted.",
					game.getWord() + System.lineSeparator(),
					game.getBuildProgressString() + System.lineSeparator(),
					"Guessed letters: " + game.usedToString() + System.lineSeparator(),
					game.getGuessers() + System.lineSeparator())).queue();
			games.remove(channel);
			return;
		}

		game.message.editMessage(game.getGameMessage('0')).queue();
	}

	/**
	 * Will check wether the correct guess results in a win or not
	 * @param channel The channel
	 * @param game The hangman game in that channel
	 * @param forceWin Forces a win. Useful if the word has been guessed in advance
	 */
	private void correctGuess(MessageChannel channel, Game game, boolean forceWin)
	{
		boolean won = true;

		for(boolean b : game.guessed)
		{
			if(!b)
			{
				won = false;
				break;
			}
		}

		if(won || forceWin)
		{
			games.remove(channel);
			game.message.editMessage(String.format("You win! The word was: **%s**%s%s%sA new word can now be submitted.",
					game.getWord() + System.lineSeparator(),
					game.getBuildProgressString() + System.lineSeparator(),
					"Guessed letters: " + game.usedToString() + System.lineSeparator(),
					game.getGuessers() + System.lineSeparator())).queue();
		}
		else
			game.message.editMessage(game.getGameMessage('0')).queue();
	}

	@Override
	public void onDMReceived(PrivateMessageReceivedEvent event, HashMap<String,Object> info)
	{
		MessageChannel channel = (MessageChannel)info.get("channel");

		if(!games.containsKey(channel))
		{
			Game game = new Game(event.getMessage().getContentRaw().toLowerCase());

			channel.sendMessage("New hangman game started by " + event.getAuthor().getAsMention() + ": " + game.getGameMessage('0')).queue(message -> {
				game.setMessage(message);
				games.put(channel, game);
			});
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
