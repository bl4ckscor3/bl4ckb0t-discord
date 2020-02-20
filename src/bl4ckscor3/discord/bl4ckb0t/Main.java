package bl4ckscor3.discord.bl4ckb0t;

import java.util.ArrayList;
import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
import bl4ckscor3.discord.bl4ckb0t.util.Tokens;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.GenericUserPresenceEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter
{
	private static boolean dev;
	private static JDA client;
	public static ModuleManager manager;
	public static final String VERSION = "v3.1";
	public static final Main INSTANCE = new Main();

	public static void main(String[] args)
	{
		dev = args.length >= 1 && args[0].equalsIgnoreCase("-dev");

		try
		{
			JDABuilder builder = new JDABuilder(dev ? Tokens.DISCORD_DEV : Tokens.DISCORD);

			manager = new ModuleManager(builder);
			manager.initPrivate();
			manager.initPublic();
			builder.addEventListeners(INSTANCE);
			client = builder.build();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
	{
		if(IRequestDM.AWAITED_DMS.containsKey(event.getAuthor().getIdLong()))
		{
			HashMap<String,Object> info = IRequestDM.AWAITED_DMS.get(event.getAuthor().getIdLong());

			((IRequestDM)info.get("instance")).onDMReceived(event, info);
			IRequestDM.AWAITED_DMS.remove(event.getAuthor().getIdLong());
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		try
		{
			if(event.getChannel().getType() == ChannelType.PRIVATE)
				return;

			for(AbstractModule m : (ArrayList<AbstractModule>)ModuleManager.MODULES.clone()) //.clone to counteract ConcurrentModificationException
			{
				if(m.triggeredBy(event))
				{
					if(!m.requiresPermission() || (m.requiresPermission() && (event.getAuthor().getIdLong() == IDs.BL4CKSCOR3 || event.getAuthor().getIdLong() == IDs.AKINO_GERMANY)))
					{
						if((dev && event.getChannel().getIdLong() == IDs.TESTING) || m.allowedChannels() == null || (m.allowedChannels() != null && Utilities.longArrayContains(m.allowedChannels(), event.getChannel().getIdLong())))
							m.exe(event, Utilities.toArgs(event.getMessage().getContentRaw())); //no return to allow for modulesto fire after other modules
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		try
		{
			if(IReactable.AWAITED_REACTIONS.containsKey(event.getMessageIdLong()) && event.getUser().getIdLong() == IReactable.AWAITED_REACTIONS.get(event.getMessageIdLong()).getUserID())
			{
				IReactable.AWAITED_REACTIONS.get(event.getMessageIdLong()).getReactable().onReactionAdd(event);
				IReactable.AWAITED_REACTIONS.remove(event.getMessageIdLong());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



	@Override
	public void onReady(ReadyEvent event)
	{
		updatePresence();
	}

	@Override
	public void onGenericUserPresence(GenericUserPresenceEvent event)
	{
		if(event.getMember().getIdLong() == client.getSelfUser().getIdLong())
			updatePresence();
	}

	@Override
	public void onResume(ResumedEvent event)
	{
		updatePresence();
	}

	public void updatePresence()
	{
		client.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("with bl4ckscor3"));
	}

	/**
	 *  @return The client
	 */
	public static JDA client()
	{
		return client;
	}
}
