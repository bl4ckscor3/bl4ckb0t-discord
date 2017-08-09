package bl4ckscor3.discord.bl4ckb0t;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.github.sheigutn.pushbullet.Pushbullet;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

/**
 * v1.0: - Initial release with CSGO update notifications and -calc for WolframAlpha calculations
 */
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			IDiscordClient client = new ClientBuilder().withToken(Tokens.DISCORD).build();

			client.login();
			client.getDispatcher().registerListener(new Main());
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) throws MalformedURLException, IOException
	{
		String msg = event.getMessage().getContent();
		
		if((msg.startsWith("-calc")) || (msg.startsWith("-eval")) || (msg.startsWith("-calculate")) || (msg.startsWith("-evaluate")))
		{
			String[] args = msg.split(" ");
			String input = "";
			
			for(int i = 1; i < args.length; i++)
			{
				input = input + args[i] + " ";
			}
			
			evaluate(event, input.trim());
		}
		else if ((event.getChannel().getID().equals(IDs.EXTRUDERS)) && (event.getAuthor().getID().equals(IDs.MAUNZ)) && (msg.toLowerCase().contains("was pushed to the steam client!")))
		{
			new Pushbullet(Tokens.PUSHBULLET).pushNote("New CS:GO update!", msg.toLowerCase().contains("beta") ? "Beta" : "Release");

			List<IUser> users = event.getChannel().getUsersHere();
			String bl4uff = "";
			
			for(IUser user : users)
			{
				if(!user.isBot())
					bl4uff = bl4uff + user.mention() + ", ";
			}
			
			event.getChannel().sendMessage(bl4uff + "^");
		}
	}

	/**
	 * Queries WolframAlpha
	 * @param event The MessageEvent the command that triggered this got sent in
	 * @param input The query for WolframAlpha (text to send)
	 */
	private void evaluate(MessageReceivedEvent event, String input) throws MalformedURLException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://api.wolframalpha.com/v2/query?appid=" + Tokens.WOLFRAM_ALPHA + "&input=" + input.trim().replace("+", "%2B").replace(' ', '+').replace(',', '.')).openStream()));
		String line = "";
		IChannel channel = event.getChannel();

		try
		{
			//skipping lines until wanted line is reached
			while(!((line = reader.readLine()).contains("position='200'")))
			{
				if(line.contains("Appid missing"))
				{
					channel.sendMessage("The Appid is missing. This should not happen.");
					reader.close();
					return;
				}
				else if(line.contains("success='false'"))
				{
					channel.sendMessage(String.format("WolframAlpha could not find a solution for \"%s\".", input));
					reader.close();
					return;
				}
			}
		}
		catch(NullPointerException e)
		{
			channel.sendMessage("The line containing the result could not be found, WolframAlpha might have taken too long.");
			reader.close();
			return;
		}
		
		try
		{
			//skipping lines to the line with the result
			while(!((line = reader.readLine()).contains("plaintext"))){}
		}
		catch(NullPointerException e)
		{
			channel.sendMessage("The actual result could not be found, however the line it should be on was there.");
			reader.close();
			return;
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
			return;
		}
		
		if(result.matches("[0-9]+/[0-9]+.*"))
		{
			evaluate(event, input + " in decimal");
			reader.close();
			return;
		}

		channel.sendMessage(result);
	}
}
