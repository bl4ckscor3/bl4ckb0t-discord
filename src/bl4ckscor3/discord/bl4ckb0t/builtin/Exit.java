package bl4ckscor3.discord.bl4ckb0t.builtin;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class Exit extends AbstractModule implements IReactable, BuiltInModule {
	public Exit(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		if (event.getMessage().getContentRaw().equalsIgnoreCase("-restart")) {
			System.exit(43);
			return;
		}

		event.getChannel().sendMessage("Sure?").queue(msg -> {
			waitForReaction(msg.getIdLong(), event.getAuthor().getIdLong());
			Utilities.react(msg, "✅", "❌");
		});
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String msg = event.getMessage().getContentRaw();
		return msg.equalsIgnoreCase("-exit") || msg.equalsIgnoreCase("-stop") || msg.equalsIgnoreCase("-restart");
	}

	@Override
	public boolean requiresPermission() {
		return true;
	}

	@Override
	public void onReactionAdd(MessageReactionAddEvent event) {
		if (event.getReaction().getEmoji().getName().equals("✅")) {
			Utilities.deleteMessage(event.getChannel(), event.getMessageIdLong());
			Main.client().shutdown();
			System.exit(0);
		}
		else if (event.getReaction().getEmoji().getName().equals("❌"))
			Utilities.deleteMessage(event.getChannel(), event.getMessageIdLong());
	}
}
