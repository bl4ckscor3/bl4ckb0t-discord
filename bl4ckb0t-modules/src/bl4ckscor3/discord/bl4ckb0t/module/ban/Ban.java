package bl4ckscor3.discord.bl4ckb0t.module.ban;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class Ban extends AbstractModule implements IReactable {
	private static record BanInfo(String userId, String reason) {}

	private static final long SECURITYCRAFT_ID = 318542314010312715L;
	private static final long ALLOWED_ROLE = 329137158692798464L;
	private static final Map<Long, BanInfo> REACTION_INFO = new HashMap<>();

	public Ban(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		Message message = event.getMessage();
		Guild guild = message.getGuild();

		if (guild.getMembersWithRoles(guild.getRoleById(ALLOWED_ROLE)).stream().anyMatch(member -> member.getIdLong() == message.getAuthor().getIdLong())) {
			Utilities.react(message, "✅", "❌");
			REACTION_INFO.put(message.getIdLong(), new BanInfo(args[0], event.getMessage().getContentRaw().split(args[0] + " ")[1] + " (" + event.getAuthor().getName() + ")"));
			waitForReaction(message.getIdLong(), message.getAuthor().getIdLong());
		}
	}

	@Override
	public void onReactionAdd(MessageReactionAddEvent event) {
		BanInfo banInfo = REACTION_INFO.remove(event.getMessageIdLong());

		if (banInfo != null && event.getEmoji().getName().equals("✅"))
			event.getGuild().ban(UserSnowflake.fromId(banInfo.userId), 0, TimeUnit.DAYS).reason(banInfo.reason).complete();
	}

	@Override
	public long[] allowedChannels() {
		return new long[] {
				692486132700676106L
		};
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return (Main.isDev() || event.getGuild().getIdLong() == SECURITYCRAFT_ID) && event.getMessage().getContentRaw().startsWith("-ban");
	}
}
