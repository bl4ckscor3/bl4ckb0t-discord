package bl4ckscor3.discord.bl4ckb0t.util;

public class TimeParser {
	//----------------CHANGE STRING TO LONG----------------\\
	/**
	 * Changes a String of the format xdxhxmxs (where x is a positive number) to a timestamp
	 *
	 * @param s The String to convert
	 * @return The converted String in milliseconds, -1 if malformed
	 */
	public static long stringToLong(String s) {
		if (s.contains("d"))
			return days(s);
		else if (s.contains("h"))
			return hours(s);
		else if (s.contains("m"))
			return minutes(s);
		else if (s.contains("s"))
			return seconds(s);
		else
			return -1;
	}

	/**
	 * Sub-method of stringToLong() handling days
	 *
	 * @param s The String to convert
	 * @return The converted String
	 */
	private static long days(String s) {
		long value = Long.parseLong(s.split("d")[0]);

		value *= 24 * 60 * 60;

		if (s.contains("h"))
			return value + hours(s);
		else if (s.contains("m"))
			return value + minutes(s);
		else if (s.contains("s"))
			return value + seconds(s);
		else
			return value;
	}

	/**
	 * Sub-method of stringToLong() handling hours
	 *
	 * @param s The String to convert
	 * @return The converted String
	 */
	private static long hours(String s) {
		long value = 0;

		if (s.contains("d"))
			value = Long.parseLong(s.split("d")[1].split("h")[0]);
		else
			value = Long.parseLong(s.split("h")[0]);

		value *= 60 * 60;

		if (s.contains("m"))
			return value + minutes(s);
		else if (s.contains("s"))
			return value + seconds(s);
		else
			return value;
	}

	/**
	 * Sub-method of stringToLong() handling minutes
	 *
	 * @param s The String to convert
	 * @return The converted String
	 */
	private static long minutes(String s) {
		long value = 0;

		if (s.contains("d")) {
			if (s.contains("h"))
				value = Long.parseLong(s.split("d")[1].split("h")[1].split("m")[0]);
			else
				value = Long.parseLong(s.split("d")[1].split("m")[0]);
		}
		else if (s.contains("h"))
			value = Long.parseLong(s.split("h")[1].split("m")[0]);
		else
			value = Long.parseLong(s.split("m")[0]);

		value *= 60;

		if (s.contains("s"))
			return value + seconds(s);
		else
			return value;
	}

	/**
	 * Sub-method of stringToLong() handling seconds
	 *
	 * @param s The String to convert
	 * @return The converted String
	 */
	private static long seconds(String s) {
		long value = 0;

		if (s.contains("d")) {
			if (s.contains("h")) {
				if (s.contains("m"))
					value = Long.parseLong(s.split("d")[1].split("h")[1].split("m")[1].split("s")[0]);
				else
					value = Long.parseLong(s.split("d")[1].split("h")[1].split("s")[0]);
			}
			else {
				if (s.contains("m"))
					value = Long.parseLong(s.split("d")[1].split("m")[1].split("s")[0]);
				else
					value = Long.parseLong(s.split("d")[1].split("s")[0]);
			}
		}
		else if (s.contains("h")) {
			if (s.contains("m"))
				value = Long.parseLong(s.split("h")[1].split("m")[1].split("s")[0]);
			else
				value = Long.parseLong(s.split("h")[1].split("s")[0]);
		}
		else if (s.contains("m"))
			value = Long.parseLong(s.split("m")[1].split("s")[0]);
		else
			value = Long.parseLong(s.split("s")[0]);

		return value;
	}

	//----------------CHANGE LONG TO STRING----------------\\

	/**
	 * Changes a timestamp into a String with the given format
	 *
	 * @param l The timestamp in milliseconds to convert
	 * @param format The format the String will have. Use %s as a placeholder for days/hours/minutes/seconds
	 * @return The converted timestamp as a String
	 */
	public static String longToString(long l, String format) {
		long s = l / 1000;
		long m = s / 60;
		long h = m / 60;
		long d = h / 24;

		s -= 60 * m;
		m -= 60 * h;
		h -= 24 * d;

		return String.format(format, (d < 10 ? "0" + d : d), (h < 10 ? "0" + h : h), (m < 10 ? "0" + m : m), (s < 10 ? "0" + s : s));
	}
}