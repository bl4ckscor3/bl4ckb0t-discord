package bl4ckscor3.discord.bl4ckb0t.modules;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class UpgradeCounter extends AbstractModule
{
	public static final File UPGRADE_COUNT_FILE = new File(Utilities.getJarLocation() + File.separator + "upgradecount.txt");

	@Override
	public void init() throws IOException
	{
		if(!UPGRADE_COUNT_FILE.exists())
		{
			UPGRADE_COUNT_FILE.createNewFile();
			FileUtils.writeLines(UPGRADE_COUNT_FILE, Arrays.asList(new String[] {"received-Vauff:0", "received-bl4ckscor3:0"}));
		}
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		List<String> contents = FileUtils.readLines(UPGRADE_COUNT_FILE, Charset.defaultCharset());
		int index = event.getMessage().getMentions().get(0).getLongID() == IDs.VAUFF ? 0 : 1;

		contents.set(index, "received-" + (index == 0 ? "Vauff" : "bl4ckscor3") + ":" + (Integer.parseInt(contents.get(index).split(":")[1]) + 1));
		FileUtils.writeLines(UPGRADE_COUNT_FILE, contents);
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		if(event.getMessage().getMentions().size() > 0)
		{
			IUser mentioned = event.getMessage().getMentions().get(0);

			return mentioned != null && event.getMessage().getContent().replace("!", "").toLowerCase().matches("_upgrades " + event.getMessage().getMentions().get(0).mention().replace("!", "") + "'s extruder.*_") && (mentioned.getLongID() == IDs.VAUFF || mentioned.getLongID() == IDs.BL4CKSCOR3);
		}
		else return false;
	}

	@Override
	public long[] allowedChannels()
	{
		return new long[] {IDs.EXTRUDERS};
	}
}
