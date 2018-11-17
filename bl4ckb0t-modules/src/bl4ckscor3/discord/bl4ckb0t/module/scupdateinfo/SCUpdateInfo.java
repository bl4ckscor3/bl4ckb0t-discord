package bl4ckscor3.discord.bl4ckb0t.module.scupdateinfo;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class SCUpdateInfo extends AbstractModule
{
	private static final long SECURITYCRAFT = 318542314010312715L;
	private static final long CAZSIUS = 132213780011548672L;
	private static final long GEFORCE = 225350861138821122L;
	private static final long VAKONOF = 233147380566982656L;
	private static final long VAUFF = 129448521861431296L;

	public SCUpdateInfo(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.sendMessage(event.getChannel(), "<#488028501442822155>");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String content = event.getMessage().getContent();
		long author = event.getAuthor().getLongID();

		return event.getGuild().getLongID() == SECURITYCRAFT && author != IDs.BL4CKSCOR3 && author != GEFORCE && author != VAUFF && author != CAZSIUS && author != VAKONOF &&
				content.contains("1.13") && (content.contains("update") || content.contains("version") || content.contains("when"));
	}
}
