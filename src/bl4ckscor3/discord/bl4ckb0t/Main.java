package bl4ckscor3.discord.bl4ckb0t;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

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
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class Main
{
	private static boolean dev;
	private static IDiscordClient client;
	public static ModuleManager manager;

	public static void main(String[] args)
	{
		dev = args.length >= 1 && args[0].equalsIgnoreCase("-dev");

		try
		{
			ClientBuilder builder = new ClientBuilder().withToken(dev ? Tokens.DISCORD_DEV : Tokens.DISCORD);

			manager = new ModuleManager(builder);
			manager.initPrivate();
			manager.initPublic();
			client = builder.build();
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
			if(event.getChannel().isPrivate() && IRequestDM.AWAITED_DMS.containsKey(event.getAuthor().getLongID()))
			{
				HashMap<String,Object> info = IRequestDM.AWAITED_DMS.get(event.getAuthor().getLongID());

				((IRequestDM)info.get("instance")).onDMReceived(event, info);
				IRequestDM.AWAITED_DMS.remove(event.getAuthor().getLongID());
				return;
			}

			for(AbstractModule m : (ArrayList<AbstractModule>)ModuleManager.MODULES.clone())
			{
				if(m.triggeredBy(event))
				{
					if(!m.requiresPermission() || (m.requiresPermission() && (event.getAuthor().getLongID() == IDs.BL4CKSCOR3 || event.getAuthor().getLongID() == IDs.AKINO_GERMANY)))
					{
						if((dev && m.allowedChannels() != null && event.getChannel().getLongID() == IDs.TESTING) || m.allowedChannels() == null || (m.allowedChannels() != null && Utilities.longArrayContains(m.allowedChannels(), event.getChannel().getLongID())))
							m.exe(event, Utilities.toArgs(event.getMessage().getContent())); //no return to allow for modulesto fire after other modules
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
		event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "with bl4ckscor3");
	}

	@EventSubscriber
	public void onPresenceUpdate(PresenceUpdateEvent event)
	{
		if(event.getUser().getLongID() == client.getOurUser().getLongID())
		{
			if(!event.getOldPresence().getText().equals(event.getNewPresence().getText()))
				event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "with bl4ckscor3");
		}
	}

	@EventSubscriber
	public void onResumed(ResumedEvent event)
	{
		event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "with bl4ckscor3");
	}

	/**
	 *  @return The client
	 */
	public static IDiscordClient client()
	{
		return client;
	}
}
