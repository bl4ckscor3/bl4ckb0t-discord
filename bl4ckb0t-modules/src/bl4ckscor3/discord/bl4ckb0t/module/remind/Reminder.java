package bl4ckscor3.discord.bl4ckb0t.module.remind;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

class Reminder {
	protected static int latestId = 1;
	private int id;
	private long issuedUser;
	private MessageChannel issuedChannel;
	private String text;
	private ScheduledFuture<?> thread;
	private File f;

	/**
	 * Saves and manages a reminder
	 *
	 * @param user The user this Reminder belongs to
	 * @param channel The channel this Reminder got issued from
	 * @param reminderText The reminder text
	 * @param timeDue How long to wait between issuing the Reminder and reminding the person
	 * @param load Whether or not the reminder gets loaded from a saved reminder
	 */
	protected Reminder(long user, MessageChannel channel, String reminderText, long timeDue, boolean load) {
		id = latestId++;

		if (load && timeDue <= 0) {
			Utilities.sendMessage(channel, String.format("<@%s>, your reminder for \"%s\" was %s ago.", user, reminderText, TimeParser.longToString(0 - timeDue, "%sd%sh%sm%ss")));
			latestId--;
			return;
		}

		File folder = new File(Utilities.getJarLocation() + "/reminders");
		ArrayList<String> lines = new ArrayList<>();

		f = Paths.get(Utilities.getJarLocation(), "reminders", id + ".txt").toFile();

		if (!folder.exists())
			folder.mkdirs();

		try {
			if (!f.exists() && !f.createNewFile())
				Utilities.sendMessage(channel, "The reminder file was not created.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		issuedUser = user;
		issuedChannel = channel;
		text = reminderText;
		thread = Executors.newSingleThreadScheduledExecutor().schedule(() -> {
			Utilities.sendMessage(channel, String.format("<@%s>, reminder for: %s", user, reminderText));
			Remind.REMINDERS.remove(this);

			try {
				Files.delete(f.toPath());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}, timeDue, TimeUnit.MILLISECONDS);

		if (!load) {
			lines.add("issuedUser: " + issuedUser);
			lines.add("issuedChannel: " + issuedChannel.getIdLong());
			lines.add("event: " + text);
			lines.add("timeDue: " + (System.currentTimeMillis() + timeDue));

			try {
				FileUtils.writeLines(f, lines);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			Remind.REMINDERS.add(this);
	}

	/**
	 * @return The ID of the Reminder
	 */
	protected int getId() {
		return id;
	}

	/**
	 * @return The user this Reminder belongs to
	 */
	protected long getIssuedUser() {
		return issuedUser;
	}

	/**
	 * @return The channel this Reminder got issued from
	 */
	protected MessageChannel getIssuedChannel() {
		return issuedChannel;
	}

	/**
	 * @return The text of this Reminder
	 */
	protected String getEvent() {
		return text;
	}

	/**
	 * @return How long it's left until reminding the user
	 */
	protected long getRemainingTime() {
		return thread.getDelay(TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops the reminder
	 */
	protected void stop() {
		thread.cancel(true);
		Remind.REMINDERS.remove(this);

		try {
			Files.delete(f.toPath());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads reminders from the filesystem, if existing
	 */
	public static void loadReminders() throws NumberFormatException, IOException {
		if (Main.client() == null)
			return;

		File folder = Paths.get(Utilities.getJarLocation(), "reminders").toFile();

		if (!folder.exists())
			return;

		for (File f : folder.listFiles()) {
			List<String> lines = FileUtils.readLines(f, Charset.defaultCharset());
			long user = Long.parseLong(lines.get(0).split(": ")[1]);
			MessageChannel channel = Main.client().getTextChannelById(Long.parseLong(lines.get(1).split(": ")[1]));
			String e = lines.get(2).split(": ")[1];
			long timeDue = Long.parseLong(lines.get(3).split(": ")[1]) - System.currentTimeMillis();
			Reminder r = new Reminder(user, channel, e, timeDue, true);

			if (timeDue <= 0)
				Files.delete(f.toPath());
			else if (!f.getName().split(".txt")[0].equals("" + r.getId())) {
				File newFile = Paths.get(Utilities.getJarLocation(), "reminders", r.getId() + ".txt").toFile();

				FileUtils.writeLines(newFile, lines);
				Files.delete(f.toPath());

				try {
					Utilities.sendMessage(channel, String.format("<@%s>, the ID assigned to your reminder for \"%s\" is now %s.", user, e, r.getId()));
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}