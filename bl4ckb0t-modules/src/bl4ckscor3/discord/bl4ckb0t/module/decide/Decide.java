package bl4ckscor3.discord.bl4ckb0t.module.decide;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Decide extends AbstractModule {
	public Decide(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {
		MessageChannel channel = event.getChannel();

		if (args.length >= 1 && event.getMessage().getContentRaw().endsWith("?")) {
			int decision = Main.RANDOM.nextInt(100);

			if (decision >= 0 && decision < 50)
				Utilities.react(event.getMessage(), "🚫");
			else if (decision >= 50 && decision < 100)
				Utilities.react(event.getMessage(), "✅");
			else
				Utilities.sendMessage(channel, "I failed. My decision between 0 and 100 was: " + decision);
		}
		else
			Utilities.react(event.getMessage(), "➕", "❓");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().startsWith("-decide");
	}
}
