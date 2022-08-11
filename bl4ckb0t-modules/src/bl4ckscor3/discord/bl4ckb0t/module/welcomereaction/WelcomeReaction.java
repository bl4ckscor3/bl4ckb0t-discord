package bl4ckscor3.discord.bl4ckb0t.module.welcomereaction;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.emoji.RichCustomEmojiImpl;

public class WelcomeReaction extends AbstractModule
{
	public WelcomeReaction(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		event.getChannel().addReactionById(event.getMessageIdLong(), Emoji.fromCustom(new RichCustomEmojiImpl(592090198431498240L, (GuildImpl)event.getGuild()).setName("peekaboo"))).queue();
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getType() == MessageType.GUILD_MEMBER_JOIN;
	}
}
