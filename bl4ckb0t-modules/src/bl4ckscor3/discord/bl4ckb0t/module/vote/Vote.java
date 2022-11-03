package bl4ckscor3.discord.bl4ckb0t.module.vote;

import java.util.Arrays;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Vote extends AbstractModule {
	public Vote(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {
		if (args.length >= 3) {
			try {
				int optionAmount = Integer.parseInt(args[0]);

				if (optionAmount < 1 || optionAmount > 10) {
					Utilities.sendMessage(event.getChannel(), "Can't have no options or more than 10 options.");
					return;
				}

				String commandMessage = event.getMessage().getContentRaw();
				int titleStart = commandMessage.indexOf(" \"");

				if (titleStart == -1) {
					Utilities.sendMessage(event.getChannel(), "Vote title has no start.");
					return;
				}

				String title = commandMessage.substring(titleStart + 2);
				int titleEnd = title.indexOf("\" ");

				if (titleEnd == -1) {
					Utilities.sendMessage(event.getChannel(), "Vote title has no end.");
					return;
				}

				title = title.substring(0, titleEnd);
				event.getChannel().sendMessage("__**" + title + "**__\n" + commandMessage.substring(commandMessage.indexOf(title) + title.length() + 2) + "\n\nVote created by " + event.getAuthor().getAsMention()).queue(msg -> Utilities.react(msg, getNumberedReactions(optionAmount)));
				event.getMessage().delete().queue();
			}
			catch (NumberFormatException e) {
				Utilities.sendMessage(event.getChannel(), "Option amount is not a number.");
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().startsWith("-vote");
	}

	public static String[] getNumberedReactions(int amount) {
		return Arrays.copyOfRange(new String[] {
				"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟"
		}, 0, amount);
	}
}
