package bl4ckscor3.discord.bl4ckb0t.module.remind;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Remind extends AbstractModule
{
	public static final List<Reminder> REMINDERS = new ArrayList<>();

	public Remind(String name)
	{
		super(name);
	}

	@Override
	public void onEnable(JDABuilder builder)
	{
		tryLoadReminders();
	}

	@Override
	public void postConnect()
	{
		tryLoadReminders();
	}

	private void tryLoadReminders()
	{
		try
		{
			Reminder.loadReminders();
		}
		catch(NumberFormatException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		MessageChannel channel = event.getChannel();

		if(args.length == 0)
			return;

		if(args.length == 1 && args[0].equals("list"))
		{
			if(REMINDERS.size() > 0)
			{
				String ids = "";
				boolean reminder = false; //whether the user has any active reminder or not

				for(Reminder r : REMINDERS)
				{
					if(r.getIssuedUser() != event.getAuthor().getIdLong())
						continue;

					reminder = true;
					ids += "" + r.getId() + ", ";
				}

				if(reminder)
				{
					ids = ids.substring(0, ids.lastIndexOf(','));
					Utilities.sendMessage(channel, String.format("You have %s active reminders: %s", REMINDERS.size(), ids));
				}
				else
					Utilities.sendMessage(channel, "You don't have any active reminders.");
			}
			else
				Utilities.sendMessage(channel, "You don't have any active reminders.");

			return;
		}

		try
		{
			int id = Integer.parseInt(args[0]); //if this doesn't fail we know that the user is probably asking for a specific id

			if(!(id > Reminder.latestId))
			{
				for(Reminder r : REMINDERS)
				{
					if(r.getId() == id)
					{
						if(args.length >= 2 && args[1].equals("stop"))
						{
							if(event.getAuthor().getIdLong() == r.getIssuedUser())
							{
								if(channel.getIdLong() == r.getIssuedChannel().getIdLong())
								{
									r.stop();
									Utilities.sendMessage(channel, String.format("Reminder with ID %s successfully stopped.", r.getId()));
								}
								else
									Utilities.sendMessage(channel, "This reminder was not issued in this channel.");
							}
							else
								Utilities.sendMessage(channel, "This is not your reminder.");

							return;
						}

						if(event.getAuthor().getIdLong() == r.getIssuedUser())
							Utilities.sendMessage(channel, String.format("Time left for \"%s\": %s", r.getEvent(), TimeParser.longToString(r.getRemainingTime(), "%sd %sh %sm %ss")));
						else
							Utilities.sendMessage(channel, "This is not your reminder.");
						return;
					}
				}
			}

			Utilities.sendMessage(channel, "This ID could not be found.");
		}
		catch(NumberFormatException e)
		{
			if(args.length >= 2)
			{
				String s = "";

				for(int i = 1; i < args.length; i++)
				{
					s += args[i] + " ";
				}

				String ev = s.trim();
				long timeDue = 0;
				long timestampDue;

				try
				{
					timestampDue = TimeParser.stringToLong(args[0]);
					timeDue = timestampDue * 1000L;
				}
				catch(NumberFormatException ex)
				{
					ex.printStackTrace();
					return;
				}

				Reminder reminder = new Reminder(event.getAuthor().getIdLong(), channel, ev, timeDue, false);

				Utilities.sendMessage(channel, String.format("I'll remind you <t:" + ((System.currentTimeMillis() / 1000L) + timestampDue) + ":R>! (ID: %s)", reminder.getId()));
				REMINDERS.add(reminder);
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().startsWith("-remind");
	}
}