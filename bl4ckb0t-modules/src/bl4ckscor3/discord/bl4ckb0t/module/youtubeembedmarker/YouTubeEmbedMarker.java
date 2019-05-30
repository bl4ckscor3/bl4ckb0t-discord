package bl4ckscor3.discord.bl4ckb0t.module.youtubeembedmarker;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class YouTubeEmbedMarker extends AbstractModule
{
	public YouTubeEmbedMarker(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String msg = event.getMessage().getContent();
		String react = "";

		if(msg.contains("&t=") || msg.contains("?t="))
			react += "🕒";

		if(msg.contains("&lc=") || msg.contains("?lc="))
			react += ",🖊";

		Utilities.react(event.getMessage(), react.split(","));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String msg = event.getMessage().getContent();

		return (msg.contains("youtube.com") || msg.contains("youtu.be"));
	}
}
