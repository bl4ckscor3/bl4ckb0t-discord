package bl4ckscor3.discord.bl4ckb0t.util;

import java.util.HashMap;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface IRequestDM
{
	/**
	 * Holds all user IDs as keys of which a DM is awaited.
	 * The values hold information that is gathered from the DM and this instance saved as "instance"
	 */
	public static final HashMap<Long,HashMap<String,Object>> AWAITED_DMS = new HashMap<Long,HashMap<String,Object>>();

	/**
	 * Sets up this IRequestDM to await a DM by the user who triggered this IRequestDM
	 * @param userID The user who triggered this IRequestDM
	 */
	public default void waitForDM(long userID, HashMap<String,Object> info)
	{
		AWAITED_DMS.put(userID, info);
	}

	/**
	 * Gets called when a DM by a user defined prior in {@link IReactable#waitForReaction} is sent
	 * @param event The event holding information about the DM
	 */
	public void onDMReceived(MessageReceivedEvent event, HashMap<String,Object> info);
}
