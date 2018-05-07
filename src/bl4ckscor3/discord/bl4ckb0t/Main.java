package bl4ckscor3.discord.bl4ckb0t;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.modules.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.modules.Evaluate;
import bl4ckscor3.discord.bl4ckb0t.modules.Exit;
import bl4ckscor3.discord.bl4ckb0t.modules.OsuAcc;
import bl4ckscor3.discord.bl4ckb0t.modules.Prick;
import bl4ckscor3.discord.bl4ckb0t.modules.blackjack.BlackJack;
import bl4ckscor3.discord.bl4ckb0t.modules.hangman.Hangman;
import bl4ckscor3.discord.bl4ckb0t.modules.upgrading.UpgradeCounter;
import bl4ckscor3.discord.bl4ckb0t.modules.upgrading.Upgrades;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
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
 * v1.8.3	- Fixed black jack not working
 * 			- Fixed players with a blackjack not getting tied with the dealer if he has a blackjack
 *
 * v1.8.2	- Added support for multiple words in hangman
 * 			- Increased rate limit retry delay
 * 			- Fixed black jack round not ending properly when all players have a blackjack
 * 			- Small black jack display changes
 *
 * v1.8.1	- Added restriction to hangman words so only letters are allowed
 * 			- Hangman messages now show already guessed letters
 * 			- Already guessed letters can no longer be guessed
 *
 * v1.8		- Added -hangman
 * 			- Added interface to allow waiting for DMs
 * 			- Messages get resent after being rate limited
 * 			- Potential fix for Black Jack soft locking
 *
 * v1.7		- Added -blackjack (-bj) command
 *			- Removed CSGO update notifier as it depended on Maunz, who no longer has that feature
 *			- Internal changes
 *
 * v1.6		- Added a ~~5%~~1% chance to add an automatic reaction to Raqbit's messages
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
			new BlackJack(),
			new Evaluate(),
			new Exit(),
			new Hangman(),
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
						{
							m.exe(event, Utilities.toArgs(event.getMessage().getContent()));
							return;
						}
					}
				}
			}

			if(event.getChannel().isPrivate() && IRequestDM.AWAITED_DMS.containsKey(event.getAuthor().getLongID()))
			{
				HashMap<String,Object> info = IRequestDM.AWAITED_DMS.get(event.getAuthor().getLongID());

				((IRequestDM)info.get("instance")).onDMReceived(event, info);
				IRequestDM.AWAITED_DMS.remove(event.getAuthor().getLongID());
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
			if(IReactable.AWAITED_REACTIONS.containsKey(event.getMessage().getLongID()) && event.getUser().getLongID() == IReactable.AWAITED_REACTIONS.get(event.getMessage().getLongID()).getUserID())
			{
				IReactable.AWAITED_REACTIONS.get(event.getMessage().getLongID()).getReactable().onReactionAdd(event);
				IReactable.AWAITED_REACTIONS.remove(event.getMessage().getLongID());
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
