package bl4ckscor3.discord.bl4ckb0t.module.scfaq;

import java.util.HashMap;
import java.util.Map;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SCFaq extends AbstractModule {
	private static final long SECURITYCRAFT_GUILD_ID = 318542314010312715L;
	private static final Map<String, String> FAQ = new HashMap<>();

	static {
		FAQ.put("1. Fabric/Bedrock Edition support", "757159143285981184");
		FAQ.put("2. View camera in frame", "757159158398058586");
		FAQ.put("3. How does it work?", "757159168577896498");
		FAQ.put("4. How do I break blocks?", "757159181470924800");
		FAQ.put("5. Why does it not work?", "757159191340253294");
		FAQ.put("6. No crafting recipe", "757159201591263283");
		FAQ.put("7. Allowlist module", "757159217835802665");
		FAQ.put("8. Inventory scanner not working", "757159227939881061");
		FAQ.put("10. Minecraft server", "757159247753642005");
		FAQ.put("11. Missing registry entries", "768534042508656641");
		FAQ.put("12. Reset configuration file", "810461564485238803");
		FAQ.put("13. Where are the config files?", "810461589580808223");
		FAQ.put("14. Turn off join message", "814091524723572738");
		FAQ.put("15. Mixin error", "922226168546066432");
		FAQ.put("16. Change or remove recipe", "999956598724239420");
		FAQ.put("17. Password in Jade/TOP/etc.", "1074429287001763941");
	}

	public SCFaq(String name) {
		super(name);
	}

	@Override
	public void onSlashCommand(SlashCommandInteractionEvent event) {
		if (event.getName().equals("faq"))
			event.reply("Please take a look at the FAQ! https://discord.com/channels/318542314010312715/757155298447327272/" + event.getOption("faq").getAsString()).complete();
	}

	@Override
	public SlashCommandData addSlashCommandFor(Guild guild) {
		if (Main.isDev() || guild.getIdLong() == SECURITYCRAFT_GUILD_ID) {
			var faqCommand = Commands.slash("faq", "Show a link to an FAQ message.");
			var faqOption = new OptionData(OptionType.STRING, "faq", "The FAQ to link to", true);

			for (var entry : FAQ.entrySet()) {
				faqOption.addChoice(entry.getKey(), entry.getValue());
			}

			faqCommand.addOptions(faqOption);
			return faqCommand;
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
}
