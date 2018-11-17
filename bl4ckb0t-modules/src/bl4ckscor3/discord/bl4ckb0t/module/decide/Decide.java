package bl4ckscor3.discord.bl4ckb0t.module.decide;

import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class Decide extends AbstractModule
{
	public Decide(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		IChannel channel = event.getChannel();

		if(args.length >= 1 && event.getMessage().getContent().endsWith("?"))
		{
			int decision = new Random().nextInt(101);

			if(decision >= 0 && decision <= 49)
				Utilities.react(event.getMessage(), "🚫");
			else if(decision >= 50 && decision <= 100)
				Utilities.react(event.getMessage(), "✅");
			else
				Utilities.sendMessage(channel, "fail: " + decision);
		}
		else
			Utilities.react(event.getMessage(), "❓");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().startsWith("-decide");
	}
}
