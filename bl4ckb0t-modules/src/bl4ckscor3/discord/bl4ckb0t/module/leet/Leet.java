package bl4ckscor3.discord.bl4ckb0t.module.leet;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Leet extends AbstractModule {
	public Leet(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		if (args.length >= 1) {
			char[] chars = event.getMessage().getContentRaw().substring(6).toCharArray();
			String result = "";

			for (int i = 0; i < chars.length; i++) {
				result += switch (chars[i]) {
					case 'A', 'a' -> "4";
					case 'E', 'e' -> "3";
					case 'G', 'g' -> "6";
					case 'L', 'l' -> "1";
					case 'O', 'o' -> "0";
					case 'S', 's' -> "5";
					case 'T', 't' -> "7";
					case 'Z', 'z' -> "2";
					default -> chars[i];
				};
			}

			Utilities.sendMessage(event.getChannel(), result);
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String s = event.getMessage().getContentRaw().toLowerCase();

		return s.startsWith("-leet") || s.startsWith("-1337");
	}
}
