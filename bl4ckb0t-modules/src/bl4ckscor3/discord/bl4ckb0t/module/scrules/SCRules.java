package bl4ckscor3.discord.bl4ckb0t.module.scrules;

import java.util.List;
import java.util.stream.Collectors;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SCRules extends AbstractModule {
	private static final long SECURITYCRAFT_ID = 318542314010312715L;
	private static final String[] RULES = {
			"", //no rule 0
			"Don't mention users that have the `Moderators`, `Developers`, or `Owner` roles without an urgent cause (examples of an urgent cause: our official Minecraft server is down, critical crash in new version, ...). When using Discord's Reply functionality, remember to turn pinging off.",
			"This is an English server. We cannot moderate non-English conversations, so please only talk English. Feel free to use a translation service.",
			"Keep messages in the correct channels. Check the channel description before writing! If your message does not fit the channel at all, it may be removed without notice.",
			"Treat others with respect.",
			"Do not spam in any way, shape, or form. This includes NSFW content.",
			"Don't ask for help if you are using cracked launchers like TLauncher or similar. We do not support them, and SecurityCraft may not work. You're on your own.",
			"Leave moderating to the moderators. If there is a problem, or someone breaking the rules, you can let the `Discord Staff` know (this is an exception to rule 1).",
			"Don't ask about updates, particularly release ETAs. They will be published eventually. _It's done when it's done._",
			"Do not advertise. This includes posting Discord invite links, unless asked about."
	};
	private static final String[] NUMBERS = {
			"", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"
	};
	private static final long ALLOWED_ROLE = 329137158692798464L;

	public SCRules(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {
		Message message = event.getMessage();
		Guild guild = message.getGuild();

		if (guild.getMembersWithRoles(guild.getRoleById(ALLOWED_ROLE)).stream().anyMatch(member -> member.getIdLong() == message.getAuthor().getIdLong())) {
			try {
				int rule = Integer.parseInt(args[0]);
				List<User> mentions = event.getMessage().getMentions().getUsers();
				boolean mention = !mentions.isEmpty();
				EmbedBuilder embed = new EmbedBuilder().setColor(0xFF0000).setTitle("❗ **Rule** " + NUMBERS[rule]).setDescription((mention ? mentions.stream().map(u -> u.getAsMention()).collect(Collectors.joining(", ")) + ": " : "") + RULES[rule]).addField("Please take a moment to review the rules.", "[Click here to see the rules](https://canary.discord.com/channels/318542314010312715/318544502438756352/667401399025401866)", false).setTimestamp(message.getTimeCreated()).setFooter(message.getAuthor().getName(), message.getAuthor().getAvatarUrl());

				if (rule == 1)
					embed.setImage("https://i.imgur.com/JoKAvjW.png");

				Utilities.sendMessage(event.getChannel(), embed.build());
				message.delete().queue();
			}
			catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				message.addReaction(Emoji.fromUnicode("❗")).queue();
			}
			catch (Exception e) {
				e.printStackTrace();
				message.addReaction(Emoji.fromUnicode("‼")).queue();
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return (Main.isDev() || event.getGuild().getIdLong() == SECURITYCRAFT_ID) && event.getMessage().getContentRaw().startsWith("-rule");
	}
}
