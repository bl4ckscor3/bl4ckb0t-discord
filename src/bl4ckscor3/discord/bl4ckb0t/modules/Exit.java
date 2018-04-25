package bl4ckscor3.discord.bl4ckb0t.modules;

import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IMessage;

public class Exit extends AbstractModule implements IReactable
{
	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		IMessage msg = event.getChannel().sendMessage("Sure?");

		waitForReaction(msg.getLongID(), event.getAuthor().getLongID());
		Utilities.react(msg, "✅", "❌");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().equals("-exit");
	}

	@Override
	public boolean requiresPermission()
	{
		return true;
	}

	@Override
	public void onReactionAdd(ReactionAddEvent event)
	{
		try
		{
			if(event.getReaction().getEmoji().getName().equals("✅"))
			{
				event.getMessage().delete();
				Main.client().logout();
				System.exit(0);
			}
			else if(event.getReaction().getEmoji().getName().equals("❌"))
				event.getMessage().delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
