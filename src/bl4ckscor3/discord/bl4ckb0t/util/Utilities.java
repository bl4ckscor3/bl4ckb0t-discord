package bl4ckscor3.discord.bl4ckb0t.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import bl4ckscor3.discord.bl4ckb0t.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class Utilities {
	public static final int RATE_LIMIT_DELAY = 2000; //in ms

	/**
	 * Gets the path of the running jar file
	 */
	public static String getJarLocation() {
		String path = "-";

		try {
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

			if (path.endsWith(".jar"))
				path = path.substring(0, path.lastIndexOf(File.separator));
		}
		catch (URISyntaxException e) {}

		return path;
	}

	/**
	 * Reacts to the given message with all given emojis
	 *
	 * @param msg The message to react to
	 * @param emojis The unicode emojis to react with
	 */
	public static void react(Message msg, String... emojis) {
		for (String s : emojis) {
			msg.addReaction(Emoji.fromUnicode(s)).queue();
		}
	}

	/**
	 * Checks if a given array contains a given value
	 *
	 * @param a The array to check
	 * @param v The value to check
	 * @return true if v is contained in a, false otherwise
	 */
	public static boolean longArrayContains(long[] a, long v) {
		for (long l : a) {
			if (l == v)
				return true;
		}

		return false;
	}

	/**
	 * Splits a command into its arguments, leaving out the command itself
	 *
	 * @param line The string to split
	 * @return An array of all the arguments
	 */
	public static String[] toArgs(String line) {
		String[] previous = line.split(" ");
		String[] result = new String[previous.length - 1];

		for (int i = 1; i < previous.length; i++) {
			result[i - 1] = previous[i];
		}

		return result;
	}

	/**
	 * Sends a message to the channel of the event
	 *
	 * @param event The event holding the channel
	 * @param message The message to be sent
	 */
	public static void sendMessage(MessageReceivedEvent event, String message) {
		sendMessage(event.getChannel(), message);
	}

	/**
	 * Sends a message to the channel
	 *
	 * @param channel The {@link MessageChannel} to send the message to
	 * @param message The message to be sent
	 */
	public static void sendMessage(MessageChannel channel, String message) {
		channel.sendMessage(message).queue();
	}

	/**
	 * Sends an {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to the channel of the event
	 *
	 * @param event The event holding the channel
	 * @param embed The {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to be sent
	 */
	public static void sendMessage(MessageReceivedEvent event, MessageEmbed embed) {
		sendMessage(event.getChannel(), embed);
	}

	/**
	 * Sends an {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to the channel
	 *
	 * @param channel The {@link sx.blah.discord.handle.obj.IChannel} to send the message to
	 * @param eo The {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to be sent
	 * @return An {@link sx.blah.discord.handle.obj.IMessage} representation of the sent
	 *         {@link sx.blah.discord.api.internal.json.objects.EmbedObject}
	 */
	public static void sendMessage(MessageChannel channel, MessageEmbed embed) {
		channel.sendMessageEmbeds(embed).queue();
	}

	/**
	 * Sends an {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to the channel
	 *
	 * @param channel The {@link sx.blah.discord.handle.obj.IChannel} to send the message to
	 * @param eo The {@link sx.blah.discord.api.internal.json.objects.EmbedObject} to be sent
	 * @param success See {@link RestAction#queue(Consumer)}
	 * @return An {@link sx.blah.discord.handle.obj.IMessage} representation of the sent
	 *         {@link sx.blah.discord.api.internal.json.objects.EmbedObject}
	 */
	public static void sendMessage(MessageChannel channel, MessageEmbed embed, Consumer<? super Message> success) {
		channel.sendMessageEmbeds(embed).queue(success);
	}

	public static void deleteMessage(MessageChannel channel, long messageID) {
		channel.deleteMessageById(messageID).queue();
	}

	/**
	 * Filters the alphabetic characters out of the input and returns them in order of appearance as a String
	 *
	 * @param The input to filter
	 * @return The alphabetic parts of the given input, defined by {@link Character#isAlphabetic(int)}
	 */
	public static String filterAlphabetic(String input) {
		String result = "";

		for (char c : input.toCharArray()) {
			if (Character.isAlphabetic(c))
				result += c;
		}

		return result;
	}
}
