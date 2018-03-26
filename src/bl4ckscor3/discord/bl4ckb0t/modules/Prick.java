package bl4ckscor3.discord.bl4ckb0t.modules;

import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;

public class Prick extends AbstractModule
{
	public final Random rand = new Random();
	public boolean enabled = true;

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		if(event.getAuthor().getLongID() == IDs.BL4CKSCOR3 && event.getMessage().getContent().startsWith("-prick"))
		{
			enabled = !enabled;
			event.getMessage().addReaction(ReactionEmoji.of(enabled ? "✅" : "❌"));
		}
		else if(enabled && rand.nextInt(100) < 5)
			event.getMessage().addReaction(ReactionEmoji.of("coctus", IDs.COCTUS));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getAuthor().getLongID() == IDs.RAQBIT || (event.getAuthor().getLongID() == IDs.BL4CKSCOR3 && event.getMessage().getContent().startsWith("-prick"));
	}
}
