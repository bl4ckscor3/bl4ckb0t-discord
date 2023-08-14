package bl4ckscor3.discord.bl4ckb0t.module.playtext;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Playtext extends AbstractModule {
	public Playtext(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		Main.INSTANCE.updatePresence();
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().startsWith("-playtext");
	}
}
