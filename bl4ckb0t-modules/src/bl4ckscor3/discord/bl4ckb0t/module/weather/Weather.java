package bl4ckscor3.discord.bl4ckb0t.module.weather;

import java.text.DecimalFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Weather extends AbstractModule
{
	public Weather(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String city = "";
		MessageChannel channel = event.getChannel();

		for(String s : args)
		{
			city += s + " ";
		}

		if(city.equals(""))
		{
			Utilities.react(event.getMessage(), "⛔");
			return;
		}

		try
		{
			Document doc = Jsoup.connect("http://api.openweathermap.org/data/2.5/weather?q=" + city.trim() + "&mode=xml&APPID=" + Tokens.OPEN_WEATHER_MAP).ignoreContentType(true).get();

			Utilities.sendMessage(channel, new EmbedBuilder().setTitle("__" + doc.select("city").attr("name") + ", " + doc.select("country").text() + "__")
					.addField(":sunny: Temperature", getTemperature(doc), false)
					.addField(":droplet: Humidity", doc.select("humidity").attr("value") + doc.select("humidity").attr("unit"), false)
					.addField(":compression: Pressure", doc.select("pressure").attr("value") + doc.select("pressure").attr("unit"), false)
					.addField(":dash: Wind", getWindSpeed(doc), false)
					.addField(":timer: Last updated", doc.select("lastupdate").attr("value").replace("T", " "), false)
					.setFooter("Powered by OpenWeatherMap").build());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Utilities.react(event.getMessage(), "⛔");
		}
	}

	private String getTemperature(Document doc)
	{
		double kelvin = Double.parseDouble(doc.select("temperature").attr("value"));
		String celsius = formatDouble(kelvin - 273.15D);

		return celsius + "°C | " + formatDouble(Double.parseDouble(celsius) * (9D / 5D) + 32D) + "°F | " + kelvin + "K";
	}

	private String getWindSpeed(Document doc)
	{
		double ms = Double.parseDouble(doc.select("speed").attr("value"));

		return ms + " m/s | " + formatDouble(ms * 2.2369362920544) + " mph " + (doc.select("direction").attr("code"));
	}

	/**
	 * Formats a double to two decimal places
	 * @param d The double to format
	 * @return The formatted double
	 */
	public static String formatDouble(double d)
	{
		return new DecimalFormat("#.00").format(d).replace(",", ".");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String message = event.getMessage().getContentRaw();

		return (message.startsWith("-weather") || message.startsWith("-w")) && Utilities.toArgs(message).length > 0;
	}
}
