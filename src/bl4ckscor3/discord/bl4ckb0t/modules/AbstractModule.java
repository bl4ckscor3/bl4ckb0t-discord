package bl4ckscor3.discord.bl4ckb0t.modules;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class AbstractModule
{
	/**
	 * Initializes this module. This method is called before the bot connects to Discord
	 */
	public void init() throws Exception {}

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
}
