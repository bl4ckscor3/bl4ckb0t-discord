package bl4ckscor3.discord.bl4ckb0t;

import java.io.IOException;
import java.net.URLClassLoader;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractModule {
	/** The name of the module. For public modules it's the file name, private modules have their name predefined */
	private String name;
	private URLClassLoader loader;

	/**
	 * Constructor
	 *
	 * @param n The name of the module
	 */
	public AbstractModule(String name) {
		this.name = name;
	}

	/**
	 * Initializes this module. This method is called before the bot connects to Discord
	 */
	public void onEnable(JDABuilder builder) {}

	/**
	 * Can be used for actions that should happen after the bot connects to Discord
	 */
	public void postConnect() {}

	/**
	 * Gets called when the module gets disabled. Should be used to remove any Listeners
	 */
	public void onDisable() {}

	/**
	 * Executes this module
	 *
	 * @param content The event that triggered this module
	 * @param args The arguments of the command, without the command itself
	 */
	public abstract void exe(MessageReceivedEvent event, String[] args) throws Exception;

	/**
	 * When a slash command happens
	 *
	 * @param event The event from the slash command interaction
	 */
	public void onSlashCommand(SlashCommandInteractionEvent event) {}

	/**
	 * @param content The event that is possibily triggering this module
	 * @return true if the event triggers this module, false otherwise
	 */
	public abstract boolean triggeredBy(MessageReceivedEvent event);

	/**
	 * @return An array of channel IDs in which this command is allowed to be executed
	 */
	public long[] allowedChannels() {
		return null;
	}

	/**
	 * @return true if this module can only be executed by specific, hard-coded users, false otherwise
	 */
	public boolean requiresPermission() {
		return false;
	}

	/**
	 * @return true if this module has slash commands for a specific guild
	 */
	public boolean hasGuildSpecificSlashCommands() {
		return false;
	}

	/**
	 * Initialize slash commands for the guild. {@link #hasGuildSpecificSlashCommands()} needs to return true for this to be
	 * called.
	 *
	 * @param guild The guild to initialize slash commands for
	 */
	public void addSlashCommandsFor(Guild guild) {}

	/**
	 * Gets the name of this module
	 *
	 * @return The name of this module
	 */
	public final String getName() {
		return name;
	}

	public final void setLoader(URLClassLoader loader) {
		this.loader = loader;
	}

	public final void closeLoader() {
		if (loader != null) {
			try {
				loader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
