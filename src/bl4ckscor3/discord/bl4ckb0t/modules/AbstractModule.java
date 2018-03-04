package bl4ckscor3.discord.bl4ckb0t.modules;

import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.util.Await;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

public abstract class AbstractModule
{
	/**
	 * Holds all messages as keys which await a reaction by a specific user.
	 * The values hold an instance of {@link Await}
	 */
	public static final HashMap<Long,Await> AWAITED = new HashMap<Long,Await>();

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

	/**
	 * Sets up this command to await a reaction by the user who triggered this command
	 * @param messageID The message which should get reacted on
	 * @param userID The user who triggered this command
	 */
	public final void waitForReaction(long messageID, long userID)
	{
		AWAITED.put(messageID, new Await(userID, this));
	}

	/**
	 * Gets called when a reaction is added to a message defined prior in {@link AbstractCommand#waitForReaction(String, String)}
	 * @param event The event holding information about the added reaction
	 */
	public void onReactionAdd(ReactionAddEvent event) {}
}
