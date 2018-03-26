package bl4ckscor3.discord.bl4ckb0t;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import bl4ckscor3.discord.bl4ckb0t.modules.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.modules.CSGONotification;
import bl4ckscor3.discord.bl4ckb0t.modules.Evaluate;
import bl4ckscor3.discord.bl4ckb0t.modules.Exit;
import bl4ckscor3.discord.bl4ckb0t.modules.OsuAcc;
import bl4ckscor3.discord.bl4ckb0t.modules.Prick;
import bl4ckscor3.discord.bl4ckb0t.modules.upgrading.UpgradeCounter;
import bl4ckscor3.discord.bl4ckb0t.modules.upgrading.Upgrades;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Tokens;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.shard.ResumedEvent;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.obj.StatusType;

/**
 * v1.6		- Added a 5% chance to add an automatic reaction to Raqbit's messages
 * 			- Added -prick to turn that on and off
 * v1.5:	- Added -osuacc (-oa) to calculate the accuracy with a given amount of 300s/100s/50s and misses
 *
 * v1.4:	- Added notification when the SecurityCraft server is down
 *
 * v1.3.2:  - Added fallback to WolframAlpha query
 *
 * v1.3.1:	- Potentially fixed disappearing playing text
 *
 * v1.3:	- Added logging
 * 			- Backend rewrite
 * 			- Fixed -upgrades being usable in all channels
 * 			- Fixed rare case where upgrades would not count towards the total
 *
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
	private static boolean dev;
	private static IDiscordClient client;
	private static final AbstractModule[] MODULES = {
			new CSGONotification(),
			new Evaluate(),
			new Exit(),
			new OsuAcc(),
			new Prick(),
			new UpgradeCounter(),
			new Upgrades()
	};

	public static void main(String[] args)
	{
		dev = args.length >= 1 && args[0].equalsIgnoreCase("-dev");

		try
		{
			client = new ClientBuilder().withToken(dev ? Tokens.DISCORD_DEV : Tokens.DISCORD).build();

			for(AbstractModule m : MODULES)
			{
				m.init();
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
	public void onMessageReceived(MessageReceivedEvent event) throws MalformedURLException, IOException, URISyntaxException
	{
		try
		{
			for(AbstractModule m : MODULES)
			{
				if(m.triggeredBy(event))
				{
					if(!m.requiresPermission() || (event.getAuthor().getLongID() == IDs.BL4CKSCOR3 || event.getAuthor().getLongID() == IDs.VAUFF))
					{
						if((dev && m.allowedChannels() != null && event.getChannel().getLongID() == IDs.TESTING) || m.allowedChannels() == null || (m.allowedChannels() != null && Utilities.longArrayContains(m.allowedChannels(), event.getChannel().getLongID())))
							m.exe(event, Utilities.toArgs(event.getMessage().getContent()));
					}
				}
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
		try
		{
			if(AbstractModule.AWAITED.containsKey(event.getMessage().getLongID()) && event.getUser().getLongID() == AbstractModule.AWAITED.get(event.getMessage().getLongID()).getUserID())
			{
				AbstractModule.AWAITED.get(event.getMessage().getLongID()).getCommand().onReactionAdd(event);
				AbstractModule.AWAITED.remove(event.getMessage().getLongID());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@EventSubscriber
	public void onReady(ReadyEvent event)
	{
		event.getClient().changePlayingText("with bl4ckscor3");
	}

	@EventSubscriber
	public void onPresenceUpdate(PresenceUpdateEvent event)
	{
		if(event.getUser().getLongID() == client.getOurUser().getLongID())
		{
			if(!event.getOldPresence().getPlayingText().equals(event.getNewPresence().getPlayingText()))
				event.getClient().changePlayingText("with bl4ckscor3");
		}
		else if(event.getUser().getLongID() == IDs.SCSERVERBOT && event.getNewPresence().getStatus() == StatusType.OFFLINE)
			client.getChannelByID(IDs.SERVER_STAFF).sendMessage("@everyone - Server went down again!");
	}

	@EventSubscriber
	public void onResumed(ResumedEvent event)
	{
		event.getClient().changePlayingText("with bl4ckscor3");
	}

	/**
	 *  @return the client
	 */
	public static IDiscordClient client()
	{
		return client;
	}
}
