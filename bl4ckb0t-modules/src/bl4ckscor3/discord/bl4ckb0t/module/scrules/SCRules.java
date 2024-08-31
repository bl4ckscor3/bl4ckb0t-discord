package bl4ckscor3.discord.bl4ckb0t.module.scrules;

import java.util.HashMap;
import java.util.Map;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SCRules extends AbstractModule {
	private static final long SECURITYCRAFT_GUILD_ID = 318542314010312715L;
	private static final Map<String, Rule> RULES = new HashMap<>();

	static {
		RULES.put("1. English only.", new Rule("**English only.** This is an English server. We cannot reliably moderate non-English conversations, so please only talk English. Feel free to use a translation service.", "1️⃣"));
		RULES.put("2. Be friendly to others.", new Rule("**Be friendly to others.** Insults, attacks, and similar have no place here.", "2️⃣"));
		RULES.put("3. Keep to the channel topic.", new Rule("**Keep to the channel topic.** Channels have topics explaining what can be discussed in them. Please read it before first writing into a channel.", "3️⃣"));
		RULES.put("4. Do not spam.", new Rule("**Do not spam.** This includes, but is not limited to, sending the same message in multiple channels and advertising your own Discord server or content.", "4️⃣"));
		RULES.put("5. No NSFW content.", new Rule("**No NSFW content.**", "5️⃣"));
		RULES.put("6. Keep pinging/mentioning people to a minimum.", new Rule("**Keep pinging/mentioning people to a minimum.** If someone tells you they don't want to get pinged, please respect that. Feel free to set your own preference in <id:customize>.", "6️⃣"));
		RULES.put("7. Don't ask for help if you are using cracked launchers.", new Rule("**Don't ask for help if you are using cracked launchers** like TLauncher or similar. We do not support them, and SecurityCraft may not work. You're on your own.", "7️⃣"));
		RULES.put("8. Leave moderating to the moderators.", new Rule("**Leave moderating to the moderators.** If there is a problem or someone is breaking the rules, you can let the @Discord Staff know. If a moderator feels a topic is not suitable for the server, even if not covered by the rules, they may end the conversation. They have the final say.", "8️⃣"));
	}

	public SCRules(String name) {
		super(name);
	}

	@Override
	public void onSlashCommand(SlashCommandInteractionEvent event) {
		if (event.getName().equals("rule")) {
			try {
				String option = event.getOption("rule").getAsString();
				Rule rule = RULES.get(option);
				User mentionedUser = event.getOption("user") == null ? null : event.getOption("user").getAsUser();
				//@formatter:off
				EmbedBuilder embed = new EmbedBuilder()
						.setColor(0xFF0000)
						.setTitle("❗ **Rule** " + rule.numberEmoji)
						.setDescription((mentionedUser != null ? mentionedUser.getAsMention() + ": " : "") + rule.rule)
						.addField("Please take a moment to review the rules.", "[Click here to see the rules](https://canary.discord.com/channels/318542314010312715/318544502438756352/667401399025401866)", false)
						.setTimestamp(event.getTimeCreated());
				//@formatter:on

				event.replyEmbeds(embed.build()).complete();
			}
			catch (Exception e) {
				e.printStackTrace();
				event.reply("An error occured. Please check the log.").complete();
			}
		}
	}

	@Override
	public SlashCommandData addSlashCommandFor(Guild guild) {
		if (Main.isDev() || guild.getIdLong() == SECURITYCRAFT_GUILD_ID) {
			var ruleCommand = Commands.slash("rule", "Show a specific rule to a user for informational purposes.");
			var ruleOption = new OptionData(OptionType.STRING, "rule", "The rule to show", true);

			for (var rule : RULES.entrySet()) {
				ruleOption.addChoice(rule.getKey(), rule.getKey());
			}

			ruleCommand.addOptions(ruleOption, new OptionData(OptionType.USER, "user", "Set a user to mention in the rule embed."));
			return ruleCommand;
		}

		return null;
	}

	@Override
	public boolean hasGuildSpecificSlashCommand() {
		return true;
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return false;
	}

	private record Rule(String rule, String numberEmoji) {}
}
