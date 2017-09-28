package bl4ckscor3.discord.bl4ckb0t.util;

import bl4ckscor3.discord.bl4ckb0t.modules.AbstractModule;

/**
 * Holds information about message that needs a reaction to continue further execution
 */
public class Await
{
	private long userID;
	private AbstractModule module;
	
	/**
	 * @param uID The user who triggered the message
	 * @param cmd The command with which to continue execution upon adding a reaction
	 */
	public Await(long uID, AbstractModule cmd)
	{
		userID = uID;
		module = cmd;
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
	public AbstractModule getCommand()
	{
		return module;
	}
}
