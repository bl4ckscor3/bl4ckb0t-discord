package bl4ckscor3.discord.bl4ckb0t.module.select;

import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Select extends AbstractModule
{
	public final Random rand = new Random();

	public Select(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		if(args.length != 0)
		{
			String[] options = event.getMessage().getContent().substring(8).split(",");

			Utilities.sendMessage(event.getChannel(), options[new Random().nextInt(options.length)]);
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().startsWith("-choose") || event.getMessage().getContent().startsWith("-select");
	}
}
