package bl4ckscor3.discord.bl4ckb0t.module.mpg;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author akino_germany, original code at
 *         https://github.com/bl4ckscor3/bl4ckb0t/blob/master/MPG/src/bl4ckscor3/module/mpg/ChannelCommand.java
 */
public class MPG extends AbstractModule {
	public MPG(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		MessageChannel channel = event.getChannel();
		String result = "Invalid arguments provided. Check the log for errors.";

		try {
			float toConvert = Float.parseFloat(args[args.length - 1]);

			if (args.length == 2) {
				if (args[0].equals("us"))
					result = String.format("%s mpg (US) are %f l/100km", toConvert, convertUS(toConvert));
				else if (args[0].equals("imp"))
					result = String.format("%s mpg (Imperial) are %f l/100km", toConvert, convertImperial(toConvert));
			}
			else if (args.length == 1)
				result = String.format("%s l/100km are %f mpg (US) and %f mpg (Imperial) ", toConvert, convertUS(toConvert), convertImperial(toConvert));

			Utilities.sendMessage(channel, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Utilities.sendMessage(channel, result);
	}

	private float convertUS(float toConvert) {
		return 235.0F / toConvert;
	}

	private float convertImperial(float toConvert) {
		return 282.0F / toConvert;
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().startsWith("-mpg");
	}
}
