package bl4ckscor3.discord.bl4ckb0t.module.select;

import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
			String[] options = event.getMessage().getContentRaw().substring(8).split(",");

			Utilities.sendMessage(event.getChannel(), options[new Random().nextInt(options.length)]);
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().startsWith("-choose") || event.getMessage().getContentRaw().startsWith("-select");
	}
}
