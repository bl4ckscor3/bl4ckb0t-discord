package bl4ckscor3.discord.bl4ckb0t.module.thumbnail;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class Thumbnail extends AbstractModule
{
	public Thumbnail(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		IChannel channel = event.getChannel();

		if(!args[0].matches("(https:\\/\\/|http:\\/\\/|)(www\\.youtube\\.com\\/watch\\?v=|youtu\\.be\\/)([A-Za-z0-9-_]{11})(&[A-Za-z0-9=&]*|)") && !args[0].matches("[A-Za-z0-9-_]{11}")) //"https://" or "http://" or nothing, followed by www.youtube.com/watch?v= followed by 11 interchangeable upper or lower cased letters, or numbers or - or _
			Utilities.react(event.getMessage(), "⛔");
		else
		{
			String link = "https://i.ytimg.com/vi/" + (args[0].matches("[A-Za-z0-9-_]{11}") ? args[0] : args[0].split("v=")[1].substring(0, 11)) + "/";

			Utilities.sendMessage(channel, "<" + link + "maxresdefault.jpg> " + link + "hqdefault.jpg");
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String message = event.getMessage().getContent();

		return (message.startsWith("-thumbnail") || message.startsWith("-tn")) && Utilities.toArgs(message).length > 0;
	}
}
