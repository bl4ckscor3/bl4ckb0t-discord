package bl4ckscor3.discord.bl4ckb0t.module.afkessen;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AFKEssen extends AbstractModule {
	public static final long IRC_BACKUP_ID = 363693897580544000L;

	public AFKEssen(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		Utilities.react(event.getMessage(), "🇬", "🇭");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().matches("afk[öÖäÄüÜßA-Za-z]*essen");
	}

	@Override
	public long[] allowedChannels() {
		return new long[] {
				IRC_BACKUP_ID
		};
	}
}
