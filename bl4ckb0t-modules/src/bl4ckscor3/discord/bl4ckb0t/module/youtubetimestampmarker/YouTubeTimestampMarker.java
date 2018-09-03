package bl4ckscor3.discord.bl4ckb0t.module.youtubetimestampmarker;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class YouTubeTimestampMarker extends AbstractModule
{
	public YouTubeTimestampMarker(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.react(event.getMessage(), "🕒");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String msg = event.getMessage().getContent();

		return (msg.contains("youtube.com") || msg.contains("youtu.be")) && (msg.contains("&t=") || msg.contains("?t="));
	}
}
