package bl4ckscor3.discord.bl4ckb0t.module.weather;

import java.time.ZonedDateTime;
import java.util.Map;

public record WeatherResults(Current current) {
	public record Current(String time, float temperature_2m, float apparent_temperature, int relative_humidity_2m, int weather_code, float wind_speed_10m, int wind_direction_10m) {

		//@formatter:off
		private static final Map<Integer, CodeRepresentation> WEATHER_CODE_REPRESENTATIONS = Map.ofEntries(
		        Map.entry(0, new CodeRepresentation("☀️", "Clear sky")),
		        Map.entry(1, new CodeRepresentation("🌤️", "Mainly clear")),
		        Map.entry(2, new CodeRepresentation("⛅", "Partly cloudy")),
		        Map.entry(3, new CodeRepresentation("☁️", "Overcast")),
		        Map.entry(45, new CodeRepresentation("🌫️", "Fog")),
		        Map.entry(48, new CodeRepresentation("🌫️", "Depositing rime fog")),
		        Map.entry(51, new CodeRepresentation("🌧️", "Light drizzle")),
		        Map.entry(53, new CodeRepresentation("🌧️", "Moderate drizzle")),
		        Map.entry(55, new CodeRepresentation("🌧️", "Dense drizzle")),
		        Map.entry(56, new CodeRepresentation("🌧️", "Light freezing drizzle")),
		        Map.entry(57, new CodeRepresentation("🌧️", "Dense freezing drizzle")),
		        Map.entry(61, new CodeRepresentation("🌧️", "Slight rain")),
		        Map.entry(63, new CodeRepresentation("🌧️", "Moderate rain")),
		        Map.entry(65, new CodeRepresentation("🌧️", "Heavy rain")),
		        Map.entry(66, new CodeRepresentation("🌧️", "Light freezing rain")),
		        Map.entry(67, new CodeRepresentation("🌧️", "Heavy freezing rain")),
		        Map.entry(71, new CodeRepresentation("🌨️", "Slight snowfall")),
		        Map.entry(73, new CodeRepresentation("🌨️", "Moderate snowfall")),
		        Map.entry(75, new CodeRepresentation("🌨️", "Heavy snowfall")),
		        Map.entry(77, new CodeRepresentation("🌨️", "Snow grains")),
		        Map.entry(80, new CodeRepresentation("🌧️", "Slight rain shower")),
		        Map.entry(81, new CodeRepresentation("🌧️", "Moderate rain shower")),
		        Map.entry(82, new CodeRepresentation("🌧️", "Violent rain shower")),
		        Map.entry(85, new CodeRepresentation("🌨️", "Slight snow shower")),
		        Map.entry(86, new CodeRepresentation("🌨️", "Heavy snow shower")),
		        Map.entry(95, new CodeRepresentation("⛈️", "Thunderstorm")),
		        Map.entry(96, new CodeRepresentation("⛈️", "Thunderstorm with slight hail")),
		        Map.entry(99, new CodeRepresentation("⛈️", "Thunderstorm with heavy hail")));
		//@formatter:on

		public ZonedDateTime discordTimestamp() {
			return ZonedDateTime.parse(time + "+00:00[Europe/London]");
		}

		public float tempToFahrenheit() {
			return toFahrenheit(temperature_2m);
		}

		public float apparentTempToFahrenheit() {
			return toFahrenheit(apparent_temperature);
		}

		public float tempToKelvin() {
			return toKelvin(temperature_2m);
		}

		public float apparentTempToKelvin() {
			return toKelvin(apparent_temperature);
		}

		public float windSpeedToMph() {
			return wind_speed_10m * 2.2369362920544F;
		}

		public String weatherCodeEmoji() {
			return WEATHER_CODE_REPRESENTATIONS.get(weather_code).emoji;
		}

		public String weatherCodeDescription() {
			return WEATHER_CODE_REPRESENTATIONS.get(weather_code).description();
		}

		public String windDirectionText() {
			if (wind_direction_10m >= 0 && wind_direction_10m <= 10)
				return "N";
			else if (wind_direction_10m <= 30)
				return "NNE";
			else if (wind_direction_10m <= 50)
				return "NE";
			else if (wind_direction_10m <= 70)
				return "ENE";
			else if (wind_direction_10m <= 100)
				return "E";
			else if (wind_direction_10m <= 120)
				return "ESE";
			else if (wind_direction_10m <= 140)
				return "SE";
			else if (wind_direction_10m <= 160)
				return "SSE";
			else if (wind_direction_10m <= 190)
				return "S";
			else if (wind_direction_10m <= 210)
				return "SSW";
			else if (wind_direction_10m <= 230)
				return "SW";
			else if (wind_direction_10m <= 250)
				return "WSW";
			else if (wind_direction_10m <= 280)
				return "W";
			else if (wind_direction_10m <= 300)
				return "WNW";
			else if (wind_direction_10m <= 320)
				return "NW";
			else if (wind_direction_10m <= 340)
				return "NNW";
			else if (wind_direction_10m <= 360)
				return "N";
			else
				return "?";
		}

		private float toFahrenheit(float value) {
			return value * 1.8F + 32;
		}

		private float toKelvin(float value) {
			return value + 273.15F;
		}
		public record CodeRepresentation(String emoji, String description) {}
	}
}
