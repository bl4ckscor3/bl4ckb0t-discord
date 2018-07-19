package bl4ckscor3.discord.bl4ckb0t.module.afkessen;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class AFKEssen extends AbstractModule
{
	public static final long IRC_BACKUP_ID = 363693897580544000L;

	public AFKEssen(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.react(event.getMessage(), "🇬", "🇭");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().matches("afk[öÖäÄüÜßA-Za-z]*essen");
	}

	@Override
	public long[] allowedChannels()
	{
		return new long[] {IRC_BACKUP_ID};
	}
}
