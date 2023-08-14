package bl4ckscor3.discord.bl4ckb0t;

import java.io.IOException;
import java.net.URLClassLoader;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class AbstractModule {
	/** The name of the module. For public modules it's the file name, built-in modules have their name predefined */
	private String name;
	private URLClassLoader loader;

	/**
	 * Constructor
	 *
	 * @param n The name of the module
	 */
	protected AbstractModule(String name) {
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
	public abstract void exe(MessageReceivedEvent event, String[] args);

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
		return new long[0];
	}

	/**
	 * @return true if this module can only be executed by specific, hard-coded users, false otherwise
	 */
	public boolean requiresPermission() {
		return false;
	}

	/**
	 * @return true if this module has a slash command for a specific guild
	 */
	public boolean hasGuildSpecificSlashCommand() {
		return false;
	}

	/**
	 * Get the slash command for the guild. {@link #hasGuildSpecificSlashCommand()} needs to return true for this to be called.
	 *
	 * @param guild The guild to get slash command for
	 * @return The slash command to add, null if there is none to add
	 */
	public SlashCommandData addSlashCommandFor(Guild guild) {
		return null;
	}

	/**
	 * Checks whether the command sender has permission to trigger this module.
	 *
	 * @param author The command sender
	 * @return true if they have permission to trigger this module, false otherwise
	 */
	public final boolean hasPermission(User author) {
		return !requiresPermission() || author.getIdLong() == IDs.BL4CKSCOR3 || author.getIdLong() == IDs.AKINO_GERMANY;
	}

	/**
	 * Checks whether this module can be triggered in the given channel.
	 *
	 * @param channel The channel where this module should be triggered
	 * @return true if the module can be triggered, false otherwise
	 */
	public final boolean isAllowedInChannel(Channel channel) {
		if (Main.isDev() && channel.getIdLong() == IDs.TESTING)
			return true;

		long[] allowedChannels = allowedChannels();

		return allowedChannels.length == 0 || Utilities.longArrayContains(allowedChannels, channel.getIdLong());
	}

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
