package bl4ckscor3.discord.bl4ckb0t.modules;

import java.util.List;

import com.github.sheigutn.pushbullet.Pushbullet;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Tokens;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CSGONotification extends AbstractModule
{
	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		new Pushbullet(Tokens.PUSHBULLET).pushNote("New CS:GO update!", event.getMessage().getContent().toLowerCase().contains("beta") ? "Beta" : "Release");

		List<IUser> users = event.getChannel().getUsersHere();
		String bl4uff = "";

		for(IUser user : users)
		{
			if(user.isBot())
				continue;
			bl4uff += user.mention() + " ";
		}

		Utilities.sendMessage(event, bl4uff + "^");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getAuthor().getLongID() == IDs.MAUNZ && event.getMessage().getContent().toLowerCase().contains("was pushed to the steam client!");
	}
	
	@Override
	public long[] allowedChannels()
	{
		return new long[] {IDs.EXTRUDERS};
	}
}
