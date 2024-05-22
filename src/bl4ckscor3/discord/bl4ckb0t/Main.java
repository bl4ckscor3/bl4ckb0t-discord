package bl4ckscor3.discord.bl4ckb0t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.IReactable;
import bl4ckscor3.discord.bl4ckb0t.util.IRequestDM;
import bl4ckscor3.discord.bl4ckb0t.util.Tokens;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main extends ListenerAdapter {
	public static final Main INSTANCE = new Main();
	public static final Random RANDOM = new Random();
	private static boolean dev;
	private static JDA client;
	private static ModuleManager moduleManager;

	private Main() {}

	public static void main(String[] args) {
		dev = args.length >= 1 && args[0].equalsIgnoreCase("-dev");

		try {
			JDABuilder builder = JDABuilder.create(dev ? Tokens.DISCORD_DEV : Tokens.DISCORD, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

			moduleManager = new ModuleManager(builder);
			getModuleManager().initBuiltIn();
			getModuleManager().initPublic();
			builder.addEventListeners(INSTANCE);
			client = builder.build();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReady(ReadyEvent event) {
		Map<Guild, List<SlashCommandData>> slashCommands = new HashMap<>();

		for (AbstractModule m : ModuleManager.MODULES) {
			m.postConnect();

			if (m.hasGuildSpecificSlashCommand()) {
				for (Guild guild : client.getGuilds()) {
					SlashCommandData slashCommand = null;

					if (isDev()) {
						if (guild.getIdLong() == IDs.MODDING_RANGE)
							slashCommand = m.addSlashCommandFor(guild);
					}
					else
						slashCommand = m.addSlashCommandFor(guild);

					if (slashCommand != null) {
						if (!slashCommands.containsKey(guild))
							slashCommands.put(guild, new ArrayList<>());

						slashCommands.get(guild).add(slashCommand);
					}
				}
			}
		}

		slashCommands.keySet().forEach(guild -> guild.updateCommands().addCommands(slashCommands.get(guild)).complete());
		updatePresence();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			if (event.getChannel().getType() == ChannelType.PRIVATE) {
				if (IRequestDM.AWAITED_DMS.containsKey(event.getAuthor().getIdLong())) {
					Map<String, Object> info = IRequestDM.AWAITED_DMS.get(event.getAuthor().getIdLong());

					((IRequestDM) info.get("instance")).onDMReceived(event, info);
					IRequestDM.AWAITED_DMS.remove(event.getAuthor().getIdLong());
				}
			}
			else {
				//copy to counteract ConcurrentModificationException
				List<AbstractModule> copy = new ArrayList<>(ModuleManager.MODULES);

				for (AbstractModule module : copy) {
					if (module.triggeredBy(event) && module.hasPermission(event.getAuthor()) && module.isAllowedInChannel(event.getChannel()))
						module.exe(event, Utilities.toArgs(event.getMessage().getContentRaw())); //no return to allow for modules to fire after other modules
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		//copy to counteract ConcurrentModificationException
		List<AbstractModule> copy = new ArrayList<>();

		Collections.copy(copy, ModuleManager.MODULES);

		for (AbstractModule m : copy) {
			if (m.hasGuildSpecificSlashCommand())
				m.onSlashCommand(event);
		}
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		try {
			if (IReactable.AWAITED_REACTIONS.containsKey(event.getMessageIdLong()) && event.getUser().getIdLong() == IReactable.AWAITED_REACTIONS.get(event.getMessageIdLong()).getUserID()) {
				IReactable.AWAITED_REACTIONS.get(event.getMessageIdLong()).getReactable().onReactionAdd(event);
				IReactable.AWAITED_REACTIONS.remove(event.getMessageIdLong());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSessionResume(SessionResumeEvent event) {
		updatePresence();
	}

	public void updatePresence() {
		client.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("with bl4ckscor3"));
	}

	/**
	 * @return The client
	 */
	public static JDA client() {
		return client;
	}

	public static boolean isDev() {
		return dev;
	}

	public static ModuleManager getModuleManager() {
		return moduleManager;
	}
}
