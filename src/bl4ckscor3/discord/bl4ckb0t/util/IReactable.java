package bl4ckscor3.discord.bl4ckb0t.util;

import java.util.HashMap;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface IReactable
{
	/**
	 * Holds all messages as keys which await a reaction by a specific user.
	 * The values hold an instance of {@link Await}
	 */
	public static final HashMap<Long,Await> AWAITED_REACTIONS = new HashMap<>();

	/**
	 * Sets up this IReactable to await a reaction by the user who triggered this IReactable
	 * @param messageID The message which should get reacted on
	 * @param userID The user who triggered this IReactable
	 */
	public default void waitForReaction(long messageID, long userID)
	{
		AWAITED_REACTIONS.put(messageID, new Await(userID, this));
	}

	/**
	 * Gets called when a reaction is added to a message defined prior in {@link IReactable#waitForReaction}
	 * @param event The event holding information about the added reaction
	 */
	public void onReactionAdd(MessageReactionAddEvent event);
}
