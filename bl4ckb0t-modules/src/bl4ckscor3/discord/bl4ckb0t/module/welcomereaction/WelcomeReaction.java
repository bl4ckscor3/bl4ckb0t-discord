package bl4ckscor3.discord.bl4ckb0t.module.welcomereaction;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage.Type;

public class WelcomeReaction extends AbstractModule
{
	public WelcomeReaction(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		event.getMessage().addReaction(ReactionEmoji.of("peekaboo", 592090198431498240L));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getType() == Type.GUILD_MEMBER_JOIN;
	}
}
