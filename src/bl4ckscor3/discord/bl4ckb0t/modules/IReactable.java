package bl4ckscor3.discord.bl4ckb0t.modules;

import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.util.Await;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

public interface IReactable
{
	/**
	 * Holds all messages as keys which await a reaction by a specific user.
	 * The values hold an instance of {@link Await}
	 */
	public static final HashMap<Long,Await> AWAITED = new HashMap<Long,Await>();

	/**
	 * Sets up this IReactable to await a reaction by the user who triggered this IReactable
	 * @param messageID The message which should get reacted on
	 * @param userID The user who triggered this IReactable
	 */
	public default void waitForReaction(long messageID, long userID)
	{
		AWAITED.put(messageID, new Await(userID, this));
	}

	/**
	 * Gets called when a reaction is added to a message defined prior in {@link IReactable#waitForReaction}
	 * @param event The event holding information about the added reaction
	 */
	public void onReactionAdd(ReactionAddEvent event);
}
