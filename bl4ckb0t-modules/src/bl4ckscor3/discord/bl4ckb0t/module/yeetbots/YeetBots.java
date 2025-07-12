package bl4ckscor3.discord.bl4ckb0t.module.yeetbots;

import java.util.concurrent.TimeUnit;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class YeetBots extends AbstractModule {
	private static final long YEET_CHANNEL = 1393518453020823625L;
	private static final long STAFF_CHANNEL = 692486132700676106L;
	private static final long LOG_CHANNEL = 1203273797889695744L;
	private static final long ALLOWED_ROLE = 329137158692798464L;

	public YeetBots(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		User author = event.getAuthor();
		Guild guild = event.getGuild();

		if (!guild.getMembersWithRoles(guild.getRoleById(ALLOWED_ROLE)).stream().anyMatch(member -> member.getIdLong() == author.getIdLong())) {
			guild.timeoutFor(author, 1, TimeUnit.DAYS).reason("Stepped into the honeypot.").queue($1 -> {
				Message message = event.getMessage();

				message.delete().queue($2 -> {
					String mention = author.getAsMention();
					String attachments = message.getAttachments().stream().map(a -> a.getProxyUrl()).reduce("", (s1, s2) -> s1 + "\n" + s2);

					Utilities.sendMessage(guild.getTextChannelById(LOG_CHANNEL), "Deleted a message by " + mention + ":\n" + message.getContentRaw() + "\n" + attachments);
					Utilities.sendMessage(guild.getTextChannelById(STAFF_CHANNEL), mention + " stepped into the honeypot and was timed out for one day.");
				}, Throwable::printStackTrace);
			}, Throwable::printStackTrace);
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return true;
	}

	@Override
	public long[] allowedChannels() {
		return new long[] {
				YEET_CHANNEL
		};
	}
}
