package bl4ckscor3.discord.bl4ckb0t.module.blackjack;

import java.util.HashMap;
import java.util.Map;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BlackJack extends AbstractModule {
	private Map<MessageChannel, Round> rounds = new HashMap<>();

	public BlackJack(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		if (!rounds.containsKey(event.getChannel()))
			rounds.put(event.getChannel(), new Round(event.getChannel()));

		Round round = rounds.get(event.getChannel());

		if (args.length != 0 && args[0].equalsIgnoreCase("leave")) {
			round.leave(event.getAuthor());

			if (round.players.isEmpty())
				rounds.remove(event.getChannel());
		}
		else if (args.length != 0 && args[0].equalsIgnoreCase("reset") && event.getAuthor().getIdLong() == IDs.BL4CKSCOR3)
			round.reset(true);
		else {
			if (!round.join(event.getAuthor(), Status.WAITING))
				return;

			if (!round.hasStarted && !round.isStarting)
				round.start();
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().toLowerCase().startsWith("-blowjob"))
			Utilities.sendMessage(event.getChannel(), "No, " + event.getAuthor().getAsMention());

		return event.getMessage().getContentRaw().toLowerCase().startsWith("-bj") || event.getMessage().getContentRaw().toLowerCase().startsWith("-blackjack");
	}
}
