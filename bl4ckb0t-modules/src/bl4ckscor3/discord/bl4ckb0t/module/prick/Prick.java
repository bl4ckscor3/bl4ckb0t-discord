package bl4ckscor3.discord.bl4ckb0t.module.prick;

import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;

public class Prick extends AbstractModule
{
	public static final long BL4CKSCOR3 = 230001507481681920L;
	public static final long COCTUS = 365223663718367232L;
	public static final long RAQBIT = 140370465699921920L;
	public final Random rand = new Random();
	public boolean enabled = true;

	public Prick(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		if(event.getAuthor().getLongID() == BL4CKSCOR3 && event.getMessage().getContent().startsWith("-prick"))
		{
			enabled = !enabled;
			event.getMessage().addReaction(ReactionEmoji.of("coctus", COCTUS));
		}
		else if(enabled && rand.nextInt(1000) < 10)
			event.getMessage().addReaction(ReactionEmoji.of("coctus", COCTUS));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getAuthor().getLongID() == RAQBIT || (event.getAuthor().getLongID() == BL4CKSCOR3 && event.getMessage().getContent().startsWith("-prick"));
	}
}
