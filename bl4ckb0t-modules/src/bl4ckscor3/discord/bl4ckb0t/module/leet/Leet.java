package bl4ckscor3.discord.bl4ckb0t.module.leet;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Leet extends AbstractModule
{
	public Leet(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		if(args.length >= 1)
		{
			char[] chars = event.getMessage().getContent().substring(6).toCharArray();
			String result = "";

			for(int i = 0; i < chars.length; i++)
			{
				switch(chars[i])
				{
					case 'A': case 'a': result += "4"; break;
					case 'E': case 'e': result += "3"; break;
					case 'G': case 'g': result += "6"; break;
					case 'L': case 'l': result += "1"; break;
					case 'O': case 'o': result += "0"; break;
					case 'S': case 's': result += "5"; break;
					case 'T': case 't': result += "7"; break;
					case 'Z': case 'z': result += "2"; break;
					default: result += chars[i];
				}
			}

			Utilities.sendMessage(event.getChannel(), result);
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().startsWith("-leet");
	}
}
