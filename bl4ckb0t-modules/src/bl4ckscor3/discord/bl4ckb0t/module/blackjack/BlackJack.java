package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.HashMap;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class BlackJack extends AbstractModule
{
	public static final long BL4CKSCOR3 = 230001507481681920L;
	public HashMap<IChannel,Round> rounds = new HashMap<IChannel,Round>();

	public BlackJack(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		if(!rounds.containsKey(event.getChannel()))
			rounds.put(event.getChannel(), new Round(event.getChannel()));

		Round round = rounds.get(event.getChannel());

		if(args.length != 0 && args[0].toLowerCase().equals("leave"))
		{
			round.leave(event.getAuthor());

			if(round.players.isEmpty())
				rounds.remove(event.getChannel());
		}
		else if(args.length != 0 && args[0].toLowerCase().equals("reset") && event.getAuthor().getLongID() == BL4CKSCOR3)
			round.reset(true);
		else
		{
			if(!round.join(event.getAuthor(), Status.WAITING))
				return;

			if(!round.hasStarted && !round.isStarting)
				round.start();
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		if(event.getMessage().getContent().toLowerCase().startsWith("-blowjob"))
			Utilities.sendMessage(event.getChannel(), "No, " + event.getAuthor().mention());

		return event.getMessage().getContent().toLowerCase().startsWith("-bj") ||
				event.getMessage().getContent().toLowerCase().startsWith("-blackjack");
	}
}
