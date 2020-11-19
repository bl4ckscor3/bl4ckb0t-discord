package bl4ckscor3.discord.bl4ckb0t.privatemodules;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class Exit extends AbstractModule implements IReactable, BuiltInModule
{
	public Exit(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		event.getChannel().sendMessage("Sure?").queue(msg -> {
			waitForReaction(msg.getIdLong(), event.getAuthor().getIdLong());
			Utilities.react(msg, "✅", "❌");
		});
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().toLowerCase().equals("-exit") || event.getMessage().getContentRaw().toLowerCase().equals("-stop");
	}

	@Override
	public boolean requiresPermission()
	{
		return true;
	}

	@Override
	public void onReactionAdd(MessageReactionAddEvent event)
	{
		if(event.getReaction().getReactionEmote().getName().equals("✅"))
		{
			Utilities.deleteMessage(event.getChannel(), event.getMessageIdLong());
			Main.client().shutdown();
			System.exit(0);
		}
		else if(event.getReaction().getReactionEmote().getName().equals("❌"))
			Utilities.deleteMessage(event.getChannel(), event.getMessageIdLong());
	}
}
