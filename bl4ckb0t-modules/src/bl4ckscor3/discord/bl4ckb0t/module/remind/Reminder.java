package bl4ckscor3.discord.bl4ckb0t.module.remind;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
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

public class Reminder {
	public static int latestId = 1;
	private int id;
	private long issuedUser;
	private MessageChannel issuedChannel;
	private String ev;
	private ScheduledFuture<?> thread;
	private File f;

	/**
	 * Saves and manages a reminder
	 *
	 * @param user The user this Reminder belongs to
	 * @param channel The channel this Reminder got issued from
	 * @param e The reminder text
	 * @param timeDue How long to wait between issuing the Reminder and reminding the person
	 * @param load Whether or not the reminder gets loaded from a saved reminder
	 */
	public Reminder(long user, MessageChannel channel, String e, long timeDue, boolean load) throws URISyntaxException, IOException {
		id = latestId++;

		if (load) {
			if (timeDue <= 0) {
				Utilities.sendMessage(channel, String.format("<@%s>, your reminder for \"%s\" was %s ago.", user, e, TimeParser.longToString(0 - timeDue, "%sd%sh%sm%ss")));
				latestId--;
				return;
			}
		}

		File folder = new File(Utilities.getJarLocation() + "/reminders");
		ArrayList<String> lines = new ArrayList<>();

		f = new File(Utilities.getJarLocation() + "/reminders/" + id + ".txt");

		if (!folder.exists())
			folder.mkdirs();

		if (!f.exists())
			f.createNewFile();

		issuedUser = user;
		issuedChannel = channel;
		ev = e;
		thread = Executors.newSingleThreadScheduledExecutor().schedule(() -> {
			Utilities.sendMessage(channel, String.format("<@%s>, reminder for: %s", user, e));
			Remind.REMINDERS.remove(this);
			f.delete();
		}, timeDue, TimeUnit.MILLISECONDS);

		if (!load) {
			lines.add("issuedUser: " + issuedUser);
			lines.add("issuedChannel: " + issuedChannel.getIdLong());
			lines.add("event: " + ev);
			lines.add("timeDue: " + (System.currentTimeMillis() + timeDue));
			FileUtils.writeLines(f, lines);
		}
		else
			Remind.REMINDERS.add(this);
	}

	/**
	 * @return The ID of the Reminder
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The user this Reminder belongs to
	 */
	public long getIssuedUser() {
		return issuedUser;
	}

	/**
	 * @return The channel this Reminder got issued from
	 */
	public MessageChannel getIssuedChannel() {
		return issuedChannel;
	}

	/**
	 * @return The text of this Reminder
	 */
	public String getEvent() {
		return ev;
	}

	/**
	 * @return How long it's left until reminding the user
	 */
	public long getRemainingTime() {
		return thread.getDelay(TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops the reminder
	 */
	public void stop() {
		thread.cancel(true);
		Remind.REMINDERS.remove(this);
		f.delete();
	}

	/**
	 * Loads reminders from the filesystem, if existing
	 */
	public static void loadReminders() throws URISyntaxException, NumberFormatException, IOException {
		if (Main.client() == null)
			return;

		File folder = new File(Utilities.getJarLocation() + "/reminders");

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
				f.delete();
			else if (!f.getName().split(".txt")[0].equals("" + r.getId())) {
				File newFile = new File(Utilities.getJarLocation() + "/reminders/" + r.getId() + ".txt");

				FileUtils.writeLines(newFile, lines);
				f.delete();

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