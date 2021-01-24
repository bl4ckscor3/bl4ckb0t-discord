package bl4ckscor3.discord.bl4ckb0t.module.scrules;

import java.util.Arrays;
import java.util.List;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SCRules extends AbstractModule
{
	private static final long SECURITYCRAFT_ID = 318542314010312715L;
	private static final String[] RULES = {
			"", //no rule 0
			"Don't mention users that have the `Moderators`, `Developers`, or `Owner` roles without an urgent cause (examples of an urgent cause: our official Minecraft server is down, critical crash in new version, ...). When using Discord's Reply functionality, remember to turn pinging off.",
			"This is an English server. As such, please only converse using English.",
			"Keep messages in the correct channels. Check the channel description before writing! If your message does not fit the channel at all, it may be removed without notice.",
			"Treat others with respect.",
			"Do not spam in any way, shape, or form. This includes NSFW content.",
			"If you have any issues and/or need help, send your problem and be patient while waiting for a reply. Generally, there will be one quick enough. Do not mention the developers (-> Rule 1).",
			"Leave moderating to the moderators. If there is a problem, or someone breaking the rules, you can let the `Discord Staff` know (this is an exception to rule 1).",
			"Don't ask/beg for updates, they will come eventually. _It's done when it's done_.",
			"Do not advertise."
	};
	private static final String[] NUMBERS = {"", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
	private static final List<Long> ALLOWED_USERS = Arrays.asList(230001507481681920L, 225350861138821122L, 466978012886466570L, 351751556770562050L, 233147380566982656L);

	public SCRules(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Message message = event.getMessage();

		if(ALLOWED_USERS.contains(event.getAuthor().getIdLong()))
		{
			try
			{
				int rule = Integer.parseInt(args[0]);

				Utilities.sendMessage(event.getChannel(), new EmbedBuilder().setColor(0xFF0000)
						.setTitle("❗ **Rule** " + NUMBERS[rule])
						.setDescription(RULES[rule])
						.addField("Please take a moment to review the rules.", "[Click here to see the rules](https://canary.discord.com/channels/318542314010312715/318544502438756352/667401399025401866)", false)
						.setTimestamp(message.getTimeCreated())
						.setFooter(message.getAuthor().getName(), message.getAuthor().getAvatarUrl())
						.build());
			}
			catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		message.delete().queue();
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getGuild().getIdLong() == SECURITYCRAFT_ID && event.getMessage().getContentRaw().startsWith("-rule");
	}
}
