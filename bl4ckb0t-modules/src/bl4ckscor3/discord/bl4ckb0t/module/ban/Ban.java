package bl4ckscor3.discord.bl4ckb0t.module.ban;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ban extends AbstractModule
{
	public Ban(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args)
	{
		event.getGuild().ban(args[0], 0, event.getMessage().getContentRaw().split(args[0] + " ")[1]).complete();
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getAuthor().getIdLong() == IDs.BL4CKSCOR3 && event.getMessage().getContentRaw().startsWith("-ban");
	}
}
