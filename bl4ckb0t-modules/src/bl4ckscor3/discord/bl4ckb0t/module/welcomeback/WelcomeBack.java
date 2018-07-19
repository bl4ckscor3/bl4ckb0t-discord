package bl4ckscor3.discord.bl4ckb0t.module.welcomeback;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class WelcomeBack extends AbstractModule
{
	public static final long IRC_BACKUP_ID = 363693897580544000L;

	public WelcomeBack(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.react(event.getMessage(), "🇼", "🇧");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().startsWith("re ") || event.getMessage().getContent().toLowerCase().equals("re");
	}

	@Override
	public long[] allowedChannels()
	{
		return new long[] {IRC_BACKUP_ID};
	}
}
