package bl4ckscor3.discord.bl4ckb0t.util;

import java.util.HashMap;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public interface IRequestDM
{
	/**
	 * Holds all user IDs as keys of which a DM is awaited. and the object which gets invoked when a DM is received (key "instance") as well as extra info
	 */
	public static final HashMap<Long,HashMap<String,Object>> AWAITED_DMS = new HashMap<>();

	/**
	 * Sets up this IRequestDM to await a DM by the user who triggered this IRequestDM
	 * @param userID The user who triggered this IRequestDM
	 */
	public default void waitForDM(long userID, HashMap<String,Object> info)
	{
		info.put("instance", this);
		AWAITED_DMS.put(userID, info);
	}

	/**
	 * Gets called when a DM by a user defined prior in {@link IReactable#waitForReaction} is sent
	 * @param event The event holding information about the DM
	 * @param info Extra info about the context
	 */
	public void onDMReceived(PrivateMessageReceivedEvent event, HashMap<String,Object> info);
}
