package bl4ckscor3.discord.bl4ckb0t;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.sheigutn.pushbullet.Pushbullet;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * v1.2.1:	- Updated to Discord4J 2.9
 * 
 * v1.2:	- Mavenized to fix the bot not mentioning and notifying after a CS:GO update
 * 			- Removed playing text timer
 * 			- Added program argument for development (-dev)
 * 			- Added error logging
 * 			- Added command to exit the bot
 * 
 * v1.1.1:	- Added 'funny' playing text
 * 
 * v1.1: 	- Added upgrade counting in #extruders
 * 
 * v1.0: 	- Initial release with CSGO update notifications and -calc for WolframAlpha calculations
 */
public class Main
{
	private static final File UPGRADE_COUNT_FILE = new File(Main.getJarLocation() + File.separator + "upgradecount.txt");
	private static boolean dev;
	private static IDiscordClient client;
	private static IMessage exitVerification;

	public static void main(String[] args)
	{
		dev = args.length >= 1 && args[0].equalsIgnoreCase("-dev");

		try
		{
			client = new ClientBuilder().withToken(dev ? Tokens.DISCORD_DEV : Tokens.DISCORD).build();

			if(!UPGRADE_COUNT_FILE.exists())
			{
				UPGRADE_COUNT_FILE.createNewFile();
				FileUtils.writeLines(UPGRADE_COUNT_FILE, Arrays.asList(new String[] {"received-Vauff:0", "received-bl4ckscor3:0"}));
			}

			client.getDispatcher().registerListener(new Main());
			client.login();
		}
		catch(Throwable t)
		{
			t.printStackTrace();	
		}
	}

	@EventSubscriber
	public void onReady(ReadyEvent event)
	{
		event.getClient().changePlayingText("with bl4ckscor3");
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) throws MalformedURLException, IOException, URISyntaxException
	{
		try
		{
			String msg = event.getMessage().getContent();

			if(msg.equals("-exit") && (event.getAuthor().getLongID() == IDs.BL4CKSCOR3 || event.getAuthor().getLongID() == IDs.VAUFF))
			{
				exitVerification = event.getChannel().sendMessage("Sure?");
				exitVerification.addReaction(ReactionEmoji.of("✅"));
				Thread.sleep(250);
				exitVerification.addReaction(ReactionEmoji.of("❌"));
				return;
			}

			if(event.getChannel().getLongID() == IDs.EXTRUDERS)
			{
				IUser mentioned = null;

				if(event.getMessage().getMentions().size() > 0)
					mentioned = event.getMessage().getMentions().get(0);

				if(mentioned != null && msg.toLowerCase().matches("_upgrades " + mentioned.mention().replace("!", "") + "'s extruder.*_") && (mentioned.getLongID() == IDs.VAUFF || mentioned.getLongID() == IDs.BL4CKSCOR3))
				{
					List<String> contents = FileUtils.readLines(UPGRADE_COUNT_FILE, Charset.defaultCharset());
					int index = mentioned.getLongID() == IDs.VAUFF ? 0 : 1;

					contents.set(index, "received-" + (index == 0 ? "Vauff" : "bl4ckscor3") + ":" + (Integer.parseInt(contents.get(index).split(":")[1]) + 1));
					FileUtils.writeLines(UPGRADE_COUNT_FILE, contents);
					return;
				}
				else if(event.getAuthor().getLongID() == IDs.MAUNZ && msg.toLowerCase().contains("was pushed to the steam client!"))
				{
					new Pushbullet(Tokens.PUSHBULLET).pushNote("New CS:GO update!", msg.toLowerCase().contains("beta") ? "Beta" : "Release");

					List<IUser> users = event.getChannel().getUsersHere();
					String bl4uff = "";

					for(IUser user : users)
					{
						if(user.isBot())
							continue;
						bl4uff += user.mention() + " ";
					}

					event.getChannel().sendMessage((bl4uff + "^"));
					return;
				}
			}

			String[] args = msg.split(" ");
			String input = "";

			for(int i = 1; i < args.length; i++)
			{
				input += args[i] + " ";
			}

			if(msg.startsWith("-calc") || msg.startsWith("-eval") || msg.startsWith("-calculate") || msg.startsWith("-evaluate"))
				evaluate(event, input.trim());
			else if(msg.startsWith("-upgrades")/* && event.getChannel().getName().equals(IDs.EXTRUDERS)*/)
			{
				List<String> contents = FileUtils.readLines(UPGRADE_COUNT_FILE, Charset.defaultCharset());
				int index = event.getAuthor().getLongID() == IDs.VAUFF ? 0 : 1;

				event.getChannel().sendMessage((index == 0 ? "bl4ckscor3" : "Vauff") + " upgraded your extruder " + contents.get(index).split(":")[1] + " times ( ͡° ͜ʖ ͡°)");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@EventSubscriber
	public void onReactionAdd(ReactionAddEvent event)
	{
		if(event.getMessage().getLongID() == exitVerification.getLongID() && (event.getUser().getLongID() == IDs.BL4CKSCOR3 || event.getUser().getLongID() == IDs.VAUFF))
		{
			if(event.getReaction().getEmoji().toString().equals("✅"))
			{
				exitVerification.delete();
				client.logout();
				System.exit(0);
			}
			else if(event.getReaction().getEmoji().toString().equals("❌"))
				exitVerification.delete();
		}
	}
	
	/**
	 * Queries WolframAlpha
	 * @param event The MessageEvent the command that triggered this got sent in
	 * @param input The query for WolframAlpha (text to send)
	 */
	private void evaluate(MessageReceivedEvent event, String input) throws MalformedURLException, IOException
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

	/**
	 * Gets the path of the running jar file
	 */
	public static String getJarLocation()
	{
		String path = "-";

		try
		{
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

			if(path.endsWith(".jar"))
				path = path.substring(0, path.lastIndexOf(File.separator));
		}
		catch(URISyntaxException e){}

		return path;
	}
}
