package bl4ckscor3.discord.bl4ckb0t.module.embedmessage;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EmbedMessage extends AbstractModule {
	private static final String DISCORD_LINK_REGEX = "(\\r\\n|\\r|\\n|.)*(discordapp\\.com|discord\\.com)\\/channels\\/\\d{18}\\/\\d{18}\\/\\d{18}(\\r\\n|\\r|\\n|.)*";

	public EmbedMessage(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		String msg = event.getMessage().getContentRaw().replace("\r\n", " ").replace("\r", " ").replace("\n", " ");

		for (String arg : msg.split(" ")) {
			if (arg.matches(DISCORD_LINK_REGEX)) {
				String[] split = arg.trim().split("/");
				String stringMessageId;
				long messageId;
				long channelId = Long.parseLong(split[split.length - 2]);
				long guildId = Long.parseLong(split[split.length - 3]);
				Guild guild = Main.client().getGuildById(guildId);

				try {
					stringMessageId = split[split.length - 1].substring(0, 19);
					messageId = Long.parseLong(stringMessageId);
					split[split.length - 1] = stringMessageId;
				}
				catch (StringIndexOutOfBoundsException | NumberFormatException e) {
					stringMessageId = split[split.length - 1].substring(0, 18);
					messageId = Long.parseLong(stringMessageId);
					split[split.length - 1] = stringMessageId;
				}

				if (guild != null) {
					TextChannel channel = guild.getTextChannelById(channelId);

					if (channel != null && guild.getSelfMember().hasPermission(channel.getPermissionContainer(), Permission.VIEW_CHANNEL)) {
						Message message = channel.retrieveMessageById(messageId).complete();
						String text = message.getContentDisplay();

						if (text.length() > MessageEmbed.VALUE_MAX_LENGTH)
							text = text.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";

						//@formatter:off
						Utilities.sendMessage(event.getChannel(), new EmbedBuilder()
								.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "[Message in](" + String.join("/", split) + ") " + channel.getAsMention(), true)
								.addField("", text, false)
								.setFooter(message.getAuthor().getName(), message.getAuthor().getAvatarUrl())
								.setTimestamp(message.getTimeCreated())
								.setColor(message.getAuthor().retrieveProfile().complete().getAccentColor())
								.build());
						//@formatter:on
					}
				}
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().matches(DISCORD_LINK_REGEX);
	}
}
