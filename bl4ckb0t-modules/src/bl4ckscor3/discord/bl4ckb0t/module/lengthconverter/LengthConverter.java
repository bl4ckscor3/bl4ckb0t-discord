package bl4ckscor3.discord.bl4ckb0t.module.lengthconverter;

import java.text.DecimalFormat;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The code in here is an utter abomination
 */
public class LengthConverter extends AbstractModule {
	private static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("#.##");
	private static final double KM_TO_MI_FACTOR = 0.6213711922D;
	private static final double M_TO_FT_FACTOR = 3.280839895D;
	private static final double CM_TO_IN_FACTOR = 0.393701D;

	public LengthConverter(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		String msg = event.getMessage().getContentRaw().toLowerCase();
		String result = "```json";

		for (String s : msg.split(" ")) {
			if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)mi")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "mi -> " + ONE_DECIMAL_PLACE.format(number / KM_TO_MI_FACTOR).replace(",", ".") + "km";
			}
			else if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)km")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "km -> " + ONE_DECIMAL_PLACE.format(number * KM_TO_MI_FACTOR).replace(",", ".") + "mi";
			}
			else if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)ft")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "ft -> " + ONE_DECIMAL_PLACE.format(number / M_TO_FT_FACTOR).replace(",", ".") + "m";
			}
			else if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)m")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "m -> " + ONE_DECIMAL_PLACE.format(number * M_TO_FT_FACTOR).replace(",", ".") + "ft";
			}
			else if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)in")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "in -> " + ONE_DECIMAL_PLACE.format(number / CM_TO_IN_FACTOR).replace(",", ".") + "cm";
			}
			else if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)cm")) {
				double number = Double.parseDouble(s.replaceAll("[^\\.0123456789-]", ""));

				result += System.lineSeparator() + number + "cm -> " + ONE_DECIMAL_PLACE.format(number * CM_TO_IN_FACTOR).replace(",", ".") + "in";
			}
		}

		Utilities.sendMessage(event, result + "```");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		if (event.getGuild().getIdLong() != 499306032489824256L)
			return false;

		for (String s : event.getMessage().getContentRaw().toLowerCase().split(" ")) {
			if (s.matches("-?(\\d+|\\d+(\\.\\d+)?)(km|mi)") || s.matches("-?(\\d+|\\d+(\\.\\d+)?)(m|ft)") || s.matches("-?(\\d+|\\d+(\\.\\d+)?)(cm|in)"))
				return true;
		}

		return event.getMessage().getContentRaw().toLowerCase().matches("(.* +-?\\d+(\\.\\d+)(km|mi) +.*|-?\\d+(\\.\\d+)(km|mi) +.*|.* +-?\\d+(\\.\\d+)(km|mi)|-?\\d+(\\.\\d+)(km|mi))") || event.getMessage().getContentRaw().toLowerCase().matches("(.* +-?\\d+(\\.\\d+)(m|ft) +.*|-?\\d+(\\.\\d+)(m|ft) +.*|.* +-?\\d+(\\.\\d+)(m|ft)|-?\\d+(\\.\\d+)(m|ft))") || event.getMessage().getContentRaw().toLowerCase().matches("(.* +-?\\d+(\\.\\d+)(cm|in) +.*|-?\\d+(\\.\\d+)(cm|in) +.*|.* +-?\\d+(\\.\\d+)(m|ft)|-?\\d+(\\.\\d+)(cm|in))");
	}
}
