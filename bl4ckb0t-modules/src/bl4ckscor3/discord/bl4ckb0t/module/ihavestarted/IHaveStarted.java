package bl4ckscor3.discord.bl4ckb0t.module.ihavestarted;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class IHaveStarted extends AbstractModule {
	public IHaveStarted(String name) {
		super(name);
	}

	@Override
	public void postConnect() {
		Utilities.sendMessage(Main.client().getThreadChannelById(1245719839822184610L), "Ich bin gestartet.");
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return false;
	}
}
