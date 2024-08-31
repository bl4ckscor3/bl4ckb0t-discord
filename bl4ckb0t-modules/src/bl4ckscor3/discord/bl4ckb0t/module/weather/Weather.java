package bl4ckscor3.discord.bl4ckb0t.module.weather;

import java.text.DecimalFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.module.weather.GeoResults.Result;
import bl4ckscor3.discord.bl4ckb0t.module.weather.WeatherResults.Current;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Weather extends AbstractModule {
	public Weather(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		String city = "";
		MessageChannel channel = event.getChannel();

		for (String s : args) {
			city += s + " ";
		}

		if (city.equals("")) {
			Utilities.react(event.getMessage(), "⛔");
			return;
		}

		try {
			Gson gson = new Gson();
			Document doc = Jsoup.connect("https://geocoding-api.open-meteo.com/v1/search?name=" + city.trim() + "&count=1&language=en&format=json").ignoreContentType(true).get();
			Result geoResult = gson.fromJson(doc.text(), GeoResults.class).results().get(0);
			Current weatherResults;

			doc = Jsoup.connect("https://api.open-meteo.com/v1/forecast?latitude=" + geoResult.latitude() + "&longitude=" + geoResult.longitude() + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,weather_code,wind_speed_10m,wind_direction_10m").ignoreContentType(true).get();
			weatherResults = gson.fromJson(doc.text(), WeatherResults.class).current();

			//@formatter:off
			Utilities.sendMessage(channel, new EmbedBuilder()
					.setTitle("__" + geoResult.name() + ", " + geoResult.country() + "__")
					.addField(":thermometer: Temperature", String.format("%s°C | %s°F | %sK", formatFloat(weatherResults.temperature_2m()), formatFloat(weatherResults.tempToFahrenheit()), formatFloat(weatherResults.tempToKelvin())), true)
					.addField(":hot_face: Feels like", String.format("%s°C | %s°F | %sK", formatFloat(weatherResults.apparent_temperature()), formatFloat(weatherResults.apparentTempToFahrenheit()), formatFloat(weatherResults.apparentTempToKelvin())), true)
					.addField(String.format("%s Weather", weatherResults.weatherCodeEmoji()), weatherResults.weatherCodeDescription(), false)
					.addField(":droplet: Humidity", weatherResults.relative_humidity_2m() + "%", true)
					.addField(":dash: Wind", String.format("%s m/s | %s mph %s", formatFloat(weatherResults.wind_speed_10m()), formatFloat(weatherResults.windSpeedToMph()), weatherResults.windDirectionText()), true)
					.setFooter("Powered by Open-Meteo")
					.setTimestamp(weatherResults.discordTimestamp()) //Open-Meteo supplies UTC time, convert to local time for discord embed timestamp
					.build());
			//@formatter:on
		}
		catch (Exception e) {
			e.printStackTrace();
			Utilities.react(event.getMessage(), "⛔");
		}
	}

	/**
	 * Formats a float to two decimal places
	 *
	 * @param f The float to format
	 * @return The formatted float
	 */
	private String formatFloat(float f) {
		return new DecimalFormat("#.00").format(f).replace(",", ".");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();

		return (message.startsWith("-weather") || message.startsWith("-w")) && Utilities.toArgs(message).length > 0;
	}
}
