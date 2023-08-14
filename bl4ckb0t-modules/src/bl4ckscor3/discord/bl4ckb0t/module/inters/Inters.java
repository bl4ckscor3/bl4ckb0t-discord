package bl4ckscor3.discord.bl4ckb0t.module.inters;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Inters extends AbstractModule {
	public Inters(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		if (event.getMessage().getContentRaw().equals(".interns"))
			Utilities.sendMessage(event.getChannel(), "https://tenor.com/view/patrick-star-idk-hammer-nailed-gif-15254720");
		else
			Utilities.sendMessage(event.getChannel(), "https://imgur.com/7Y3cFKt");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String content = event.getMessage().getContentRaw();

		return content.equals(".interns") || content.equals(".inters");
	}
}
