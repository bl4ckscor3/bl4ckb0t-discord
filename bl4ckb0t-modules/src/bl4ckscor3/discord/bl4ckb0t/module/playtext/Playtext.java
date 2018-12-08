package bl4ckscor3.discord.bl4ckb0t.module.playtext;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class Playtext extends AbstractModule
{
	public Playtext(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "with bl4ckscor3");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().equals("-playtext");
	}
}
