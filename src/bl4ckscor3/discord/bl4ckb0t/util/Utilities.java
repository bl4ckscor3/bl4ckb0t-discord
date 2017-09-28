package bl4ckscor3.discord.bl4ckb0t.util;

import java.io.File;
import java.net.URISyntaxException;

import bl4ckscor3.discord.bl4ckb0t.Main;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;

public class Utilities
{
	/**
	 * Gets the path of the running jar file
	 */
	public static String getJarLocation()
	{
		String path = "-";

		try
		{
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

			if(path.endsWith(".jar"))
				path = path.substring(0, path.lastIndexOf(File.separator));
		}
		catch(URISyntaxException e){}

		return path;
	}
	
	/**
	 * Reacts to the given message with all given emojis
	 * @param msg The message to react to
	 * @param emojis The emojis to react with
	 */
	public static void react(IMessage msg, String... emojis)
	{
		try
		{
			for(String s : emojis)
			{
				msg.addReaction(ReactionEmoji.of(s));
				Thread.sleep(250);
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a given array contains a given value
	 * @param a The array to check
	 * @param v The value to check
	 * @return true if v is contained in a, false otherwise
	 */
	public static boolean longArrayContains(long[] a, long v)
	{
		for(long l : a)
		{
			if(l == v)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Splits a command into its arguments, leaving out the command itself
	 * @param line The string to split
	 * @return An array of all the arguments
	 */
	public static String[] toArgs(String line)
	{
		String[] previous = line.split(" ");
		String[] result = new String[previous.length - 1];

		for(int i = 1; i < previous.length; i++)
		{
			result[i - 1] = previous[i];
		}

		return result;
	}
}
