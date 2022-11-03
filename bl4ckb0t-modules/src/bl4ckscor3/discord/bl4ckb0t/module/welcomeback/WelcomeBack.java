package bl4ckscor3.discord.bl4ckb0t.module.welcomeback;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WelcomeBack extends AbstractModule {
	public static final long IRC_BACKUP_ID = 363693897580544000L;

	public WelcomeBack(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {
		Utilities.react(event.getMessage(), "🇼", "🇧");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().toLowerCase().startsWith("re ") || event.getMessage().getContentRaw().toLowerCase().equals("re");
	}

	@Override
	public long[] allowedChannels() {
		return new long[] {
				IRC_BACKUP_ID
		};
	}
}
