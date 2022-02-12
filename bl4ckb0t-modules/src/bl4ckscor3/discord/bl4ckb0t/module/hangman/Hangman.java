package bl4ckscor3.discord.bl4ckb0t.module.hangman;

import java.util.Arrays;
import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Hangman extends AbstractModule implements IRequestDM
{
	public HashMap<MessageChannel,Game> games = new HashMap<>();
	public static final String ALREADY_GUESSING = "This channel already has a phrase being guessed at the moment.";

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
					String alphabeticPhrase = Utilities.filterAlphabetic(game.originalPhrase);
					String alphabeticGuess = Utilities.filterAlphabetic(Arrays.toString(args));

					if(!game.guessers.contains(event.getAuthor()))
						game.guessers.add(event.getAuthor());

					event.getMessage().delete().queue();

					if(alphabeticPhrase.equalsIgnoreCase(alphabeticGuess))
						correctGuess(channel, game, '0', true);
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
			Utilities.sendMessage(channel, "Ok! Send me a word or phrase via DM and I'll take care of everything else. Guesses can be given via `.LETTER`. E.g.: `.A` to guess the letter `A`. To guess a whole word or phrase, use `-hangman guess here`.");
		}
		else if(event.getMessage().getContentRaw().startsWith(".") && event.getMessage().getContentRaw().toCharArray().length == 2)
		{
			if(games.containsKey(channel))
			{
				Game game = games.get(channel);
				char guess = event.getMessage().getContentRaw().charAt(1);
				int letterIndex = Character.toUpperCase(guess) - 65;

				if(!game.guessers.contains(event.getAuthor()))
					game.guessers.add(event.getAuthor());

				event.getMessage().delete().queue();

				if(game.letterStates[letterIndex] != LetterState.UNUSED)
				{
					game.message.editMessage(game.getGameMessage("", guess, true)).queue();
					return;
				}

				if(!game.guess(guess))
				{
					game.letterStates[letterIndex] = LetterState.WRONG;
					wrongGuess(channel, game);
				}
				else
				{
					game.letterStates[letterIndex] = LetterState.CORRECT;
					correctGuess(channel, game, guess, false);
				}
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
		game.lastGuessedLetter = '0';

		if(game.hangman == 10)
		{
			game.message.editMessage(game.getGameMessage(String.format("__You lost!__ The phrase was: **%s**", game.originalPhrase), '0', false) + System.lineSeparator() + "__**A new word or phrase can now be submitted.**__").queue();
			games.remove(channel);
			return;
		}

		game.message.editMessage(game.getGameMessage()).queue();
	}

	/**
	 * Will check wether the correct guess results in a win or not
	 * @param channel The channel
	 * @param game The hangman game in that channel
	 * @param guess The guessed character
	 * @param forceWin Forces a win. Useful if the phrase has been guessed in advance
	 */
	private void correctGuess(MessageChannel channel, Game game, char guess, boolean forceWin)
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
			game.message.editMessage(game.getGameMessage(String.format("__You win!__ The phrase was: **%s**" + System.lineSeparator(), game.originalPhrase), '0', false) + System.lineSeparator() + "__**A new word or phrase can now be submitted.**__").queue();
			games.remove(channel);
		}
		else
		{
			game.lastGuessedLetter = guess;
			game.message.editMessage(game.getGameMessage()).queue();
		}
	}

	@Override
	public void onDMReceived(MessageReceivedEvent event, HashMap<String,Object> info)
	{
		MessageChannel channel = (MessageChannel)info.get("channel");

		if(!games.containsKey(channel))
		{
			Game game = new Game(event.getAuthor().getAsMention(), event.getMessage().getContentRaw());

			channel.sendMessage(game.getGameMessage()).queue(message -> {
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
