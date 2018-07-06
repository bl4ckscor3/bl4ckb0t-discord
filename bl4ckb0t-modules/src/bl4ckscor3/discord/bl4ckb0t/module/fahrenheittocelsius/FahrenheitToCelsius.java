package bl4ckscor3.discord.bl4ckb0t.module.fahrenheittocelsius;

import java.text.DecimalFormat;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class FahrenheitToCelsius extends AbstractModule
{
	private static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("#.#");

	public FahrenheitToCelsius(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String msg = event.getMessage().getContent().toLowerCase();
		String result = "```json";
		boolean even = false;

		for(String s : msg.split(" "))
		{
			if(s.matches("-?[0-9]*°?f"))
			{
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + (even ? "" : "") + number + "°F ->" + " " + ONE_DECIMAL_PLACE.format((number - 32) / 1.8D).replace(",", ".") + "°C";
				even = !even;
			}
			else if(s.matches("-?[0-9]*°?c"))
			{
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + (even ? "" : "") + number + "°C ->" + " " + ONE_DECIMAL_PLACE.format(number * 1.8D + 32).replace(",", ".") + "°F";
				even = !even;
			}
		}

		Utilities.sendMessage(event, result + "```");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().matches("(.* +-?[0-9]+°?(f|c) +.*|-?[0-9]+°?(f|c) +.*|.* +-?[0-9]+°?(f|c)|-?[0-9]+°?(f|c))");
	}
}
