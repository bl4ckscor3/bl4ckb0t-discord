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
	private static final String RULE_1 = "1. Don't mention staff";

	static {
		RULES.put(RULE_1, new Rule("Don't mention users that have the `Moderators`, `Developers`, or `Owner` roles without an urgent cause (examples of an urgent cause: our official Minecraft server is down, critical crash in new version, ...). When using Discord's Reply functionality, remember to turn pinging off.", "1️⃣"));
		RULES.put("2. English only", new Rule("This is an English server. We cannot moderate non-English conversations, so please only talk English. Feel free to use a translation service.", "2️⃣"));
		RULES.put("3. Wrong channel", new Rule("Keep messages in the correct channels. Check the channel description before writing! If your message does not fit the channel at all, it may be removed without notice.", "3️⃣"));
		RULES.put("4. Respect", new Rule("Treat others with respect.", "4️⃣"));
		RULES.put("5. Spam", new Rule("Do not spam in any way, shape, or form. This includes NSFW content.", "5️⃣"));
		RULES.put("6. Cracked launchers", new Rule("Don't ask for help if you are using cracked launchers like TLauncher or similar. We do not support them, and SecurityCraft may not work. You're on your own.", "6️⃣"));
		RULES.put("7. Backseat moderating", new Rule("Leave moderating to the moderators. If there is a problem, or someone breaking the rules, you can let the `Discord Staff` know (this is an exception to rule 1).", "7️⃣"));
		RULES.put("8. Update pestering", new Rule("Don't ask about updates, particularly release ETAs. They will be published eventually. _It's done when it's done._", "8️⃣"));
		RULES.put("9. Advertising", new Rule("Do not advertise. This includes posting Discord invite links, unless asked about.", "9️⃣"));
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

				if (option.equals(RULE_1))
					embed.setImage("https://i.imgur.com/lS7cE1f.png");

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
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return false;
	}

	private record Rule(String rule, String numberEmoji) {}
}
