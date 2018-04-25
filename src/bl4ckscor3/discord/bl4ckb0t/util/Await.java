package bl4ckscor3.discord.bl4ckb0t.util;

import bl4ckscor3.discord.bl4ckb0t.modules.IReactable;

/**
 * Holds information about message that needs a reaction to continue further execution
 */
public class Await
{
	private long userID;
	private IReactable reactable;

	/**
	 * @param uID The user who triggered the message
	 * @param cmd The command with which to continue execution upon adding a reaction
	 */
	public Await(long uID, IReactable r)
	{
		userID = uID;
		reactable = r;
	}

	/**
	 * @return The user who triggered the message
	 */
	public long getUserID()
	{
		return userID;
	}

	/**
	 * @return The command with which to continue execution upon adding a reaction
	 */
	public IReactable getReactable()
	{
		return reactable;
	}
}
