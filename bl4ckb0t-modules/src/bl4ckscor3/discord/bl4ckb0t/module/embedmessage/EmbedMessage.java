package bl4ckscor3.discord.bl4ckb0t.module.embedmessage;

import org.jsoup.helper.StringUtil;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EmbedMessage extends AbstractModule
{
	private static final String DISCORD_LINK_REGEX = "(\\r\\n|\\r|\\n|.)*(discordapp\\.com|discord\\.com)\\/channels\\/[0-9]{18}\\/[0-9]{18}\\/[0-9]{18}(\\r\\n|\\r|\\n|.)*";

	public EmbedMessage(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String msg = event.getMessage().getContentRaw().replace("\r\n", " ").replace("\r", " ").replace("\n", " ");

		for(String arg : msg.split(" "))
		{
			if(arg.matches(DISCORD_LINK_REGEX))
			{
				String[] split = arg.trim().split("/");
				long messageId = Long.parseLong(split[split.length - 1] = split[split.length - 1].substring(0, 18));
				long channelId = Long.parseLong(split[split.length - 2]);
				long guildId = Long.parseLong(split[split.length - 3]);
				Guild guild = Main.client().getGuildById(guildId);

				if(guild != null)
				{
					TextChannel channel = guild.getTextChannelById(channelId);

					if(channel != null)
					{
						Message message = channel.retrieveMessageById(messageId).complete();
						String text = message.getContentDisplay();

						if(text.length() > MessageEmbed.VALUE_MAX_LENGTH)
							text = text.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";

						event.getChannel().sendMessage(new EmbedBuilder()
								.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "[Message in](" + StringUtil.join(split, "/") + ") " + channel.getAsMention(), true)
								.addField("", text, false)
								.setFooter(message.getAuthor().getName(), message.getAuthor().getAvatarUrl())
								.setTimestamp(message.getTimeCreated())
								.setColor(0x2F3136)
								.build()).complete();
					}
				}
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().matches(DISCORD_LINK_REGEX);
	}
}
