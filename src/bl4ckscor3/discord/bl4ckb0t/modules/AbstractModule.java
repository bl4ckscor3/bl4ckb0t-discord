package bl4ckscor3.discord.bl4ckb0t.modules;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class AbstractModule
{
	/**The name of the module. For public modules it's the file name, private modules have their name predefined*/
	private String name;

	/**
	 * Constructor
	 * @param n The name of the module
	 */
	public AbstractModule(String name)
	{
		this.name = name;
	}

	/**
	 * Initializes this module. This method is called before the bot connects to Discord
	 */
	public void onEnable(ClientBuilder builder){}

	/**
	 * Gets called when the module gets disabled. Should be used to remove any Listeners
	 */
	public void onDisable(){}

	/**
	 * Executes this module
	 * @param content The event that triggered this module
	 * @param args The arguments of the command, without the command itself
	 */
	public abstract void exe(MessageReceivedEvent event, String[] args) throws Exception;

	/**
	 * @param content The event that is possibily triggering this module
	 * @return true if the event triggers this module, false otherwise
	 */
	public abstract boolean triggeredBy(MessageReceivedEvent event);

	/**
	 * @return An array of channel IDs in which this command is allowed to be executed
	 */
	public long[] allowedChannels()
	{
		return null;
	}

	/**
	 * @return true if this command can only be executed by Vauff and bl4ckscor3, false otherwise
	 */
	public boolean requiresPermission()
	{
		return false;
	}

	/**
	 * Gets the name of this module
	 * @return The name of this module
	 */
	public final String getName()
	{
		return name;
	}
}

