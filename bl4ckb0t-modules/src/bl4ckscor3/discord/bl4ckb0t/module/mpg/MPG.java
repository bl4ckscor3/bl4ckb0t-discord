package bl4ckscor3.discord.bl4ckb0t.module.mpg;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author akino_germany, original code at https://github.com/bl4ckscor3/bl4ckb0t/blob/master/MPG/src/bl4ckscor3/module/mpg/ChannelCommand.java
 */
public class MPG extends AbstractModule
{
	public MPG(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		MessageChannel channel = event.getChannel();
		String result = "";
		float calc = 1.0f;
		float calc2 = 2.0f;
		if(args.length == 2) {
			if(args[0].equals("us")) {
				calc = 235/(Float.parseFloat(args[1]));
				result = String.format("%s mpg (US) sind %f l/100km", args[1], calc);
				Utilities.sendMessage(channel, result);
			} else if(args[0].equals("imp")) {
				calc = 282/(Float.parseFloat(args[1]));
				result = String.format("%s mpg (Imperial) sind %f l/100km", args[1], calc);
				Utilities.sendMessage(channel, result);
			}
		} else if(args.length == 1) {
			calc = 235/(Float.parseFloat(args[0]));
			calc2 = 282/(Float.parseFloat(args[0]));
			result = String.format("%s l/100km sind %f mpg (US) und %f mpg (Imperial) ", args[0], calc, calc2);
			Utilities.sendMessage(channel, result);
		}

	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().startsWith("-mpg");
	}
}
