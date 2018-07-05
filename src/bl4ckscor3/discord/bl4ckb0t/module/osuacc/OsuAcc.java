package bl4ckscor3.discord.bl4ckb0t.module.osuacc;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class OsuAcc extends AbstractModule
{
	public OsuAcc(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		try
		{
			double num300s = Integer.parseInt(args[0]);
			double num100s = Integer.parseInt(args[1]);
			double num50s = Integer.parseInt(args[2]);
			double numMisses = Integer.parseInt(args[3]);
			double numerator = (num300s * 300) + (num100s * 100) + (num50s * 50) + (numMisses * 0);
			double denominator = (num300s + num100s + num50s + numMisses) * 300;

			Utilities.sendMessage(event, String.format("%.2f", (numerator / denominator) * 100).replace(',', '.') + "%");
		}
		catch(NumberFormatException e)
		{
			Utilities.sendMessage(event, "One or more arguments weren't numbers. Usage: -osuacc num300s num100s num50s numMisses");
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().startsWith("-osuacc") ||
				event.getMessage().getContent().toLowerCase().startsWith("-oa");
	}
}
