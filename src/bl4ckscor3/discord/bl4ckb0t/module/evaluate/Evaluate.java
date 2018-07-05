package bl4ckscor3.discord.bl4ckb0t.module.evaluate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Tokens;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class Evaluate extends AbstractModule
{
	public Evaluate(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws MalformedURLException, IOException
	{
		String input = "";

		for(int i = 0; i < args.length; i++)
		{
			input += args[i] + " ";
		}

		Utilities.sendMessage(event, evaluate(event, input.trim()));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().startsWith("-calc") ||
				event.getMessage().getContent().toLowerCase().startsWith("-eval") ||
				event.getMessage().getContent().toLowerCase().startsWith("-evaluate") ||
				event.getMessage().getContent().toLowerCase().startsWith("-calculate");
	}

	/**
	 * Queries WolframAlpha
	 * @param event The MessageEvent the command that triggered this got sent in
	 * @param input The query for WolframAlpha (text to send)
	 */
	private String evaluate(MessageReceivedEvent event, String input) throws MalformedURLException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://api.wolframalpha.com/v2/query?appid=" + Tokens.WOLFRAM_ALPHA +"&input=" + input.trim().replace("+", "%2B").replace(' ', '+').replace(',', '.')).openStream()));
		String line = "";
		IChannel channel = event.getChannel();

		try
		{
			//skipping lines until wanted line is reached
			while(!((line = reader.readLine()).contains("position='200'")))
			{
				if(line.contains("Appid missing"))
				{
					reader.close();
					return "Error: The Appid is missing. This should not happen.";
				}
				else if(line.contains("success='false'"))
				{
					reader.close();
					return String.format("Error: WolframAlpha could not find a solution for \"%s\".", input);
				}
			}
		}
		catch(NullPointerException e)
		{
			reader.close();
			return "Error: The line containing the result could not be found, WolframAlpha might have taken too long.";
		}

		try
		{
			//skipping lines to the line with the result
			while(!((line = reader.readLine()).contains("plaintext"))){}
		}
		catch(NullPointerException e)
		{
			reader.close();
			return "Error: The actual result could not be found, however the line it should be on was there.";
		}

		reader.close();

		String result;

		try
		{
			result = line.split(">")[1].split("<")[0];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			channel.sendMessage(String.format("WolframAlpha could not find a solution for \"%s\".", input));
			reader.close();
			return "Error: WolframAlpha could not find a solution for \\\"%s\\\".";
		}

		if(result.matches("[0-9]+/[0-9]+.*"))
		{
			String decimalResult = evaluate(event, input + " in decimal");

			reader.close();

			if(!decimalResult.startsWith("Error: "))
				return decimalResult;
		}

		return result;
	}
}
