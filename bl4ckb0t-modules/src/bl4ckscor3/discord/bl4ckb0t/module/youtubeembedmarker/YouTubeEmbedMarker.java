package bl4ckscor3.discord.bl4ckb0t.module.youtubeembedmarker;

import java.util.ArrayList;
import java.util.List;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class YouTubeEmbedMarker extends AbstractModule
{
	public YouTubeEmbedMarker(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String msg = event.getMessage().getContentRaw();
		List<String> react = new ArrayList<>();

		if(msg.contains("&t=") || msg.contains("?t="))
			react.add("🕒"); //clock3

		if(msg.contains("&lc=") || msg.contains("?lc="))
			react.add("🖊"); //pen_ballpoint

		Utilities.react(event.getMessage(), react.toArray(new String[react.size()]));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String msg = event.getMessage().getContentRaw();

		return (msg.contains("youtube.com") || msg.contains("youtu.be"));
	}
}
