package bl4ckscor3.discord.bl4ckb0t.module.remind;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class Remind extends AbstractModule
{
	public static final ArrayList<Reminder> reminders = new ArrayList<Reminder>();

	public Remind(String name)
	{
		super(name);
	}

	@Override
	public void onEnable(ClientBuilder builder)
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
		IChannel channel = event.getChannel();

		if(args.length == 0)
			return;

		if(args.length == 1 && args[0].equals("list"))
		{
			if(reminders.size() > 0)
			{
				String ids = "";
				boolean reminder = false; //whether the user has any active reminder or not

				for(Reminder r : reminders)
				{
					if(r.getIssuedUser() != event.getAuthor().getLongID())
						continue;

					reminder = true;
					ids += "" + r.getId() + ", ";
				}

				if(reminder)
				{
					ids = ids.substring(0, ids.lastIndexOf(','));
					Utilities.sendMessage(channel, String.format("You have %s active reminders: %s", reminders.size(), ids));
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
				for(Reminder r : reminders)
				{
					if(r.getId() == id)
					{
						if(args.length >= 2 && args[1].equals("stop"))
						{
							if(event.getAuthor().getLongID() == r.getIssuedUser())
							{
								if(channel.getLongID() == r.getIssuedChannel().getLongID())
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

						if(event.getAuthor().getLongID() == r.getIssuedUser())
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

				try
				{
					timeDue = TimeParser.stringToLong(args[0]) * 1000L;
				}
				catch(NumberFormatException ex)
				{
					ex.printStackTrace();
					return;
				}

				Reminder reminder = new Reminder(event.getAuthor().getLongID(), channel, ev, timeDue, false);

				Utilities.sendMessage(channel, String.format("I'll remind you! (ID: %s)", reminder.getId()));
				reminders.add(reminder);
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().startsWith("-remind");
	}
}