package bl4ckscor3.discord.bl4ckb0t.modules.upgrading;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import bl4ckscor3.discord.bl4ckb0t.modules.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Upgrades extends AbstractModule
{
	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		List<String> contents = FileUtils.readLines(UpgradeCounter.UPGRADE_COUNT_FILE, Charset.defaultCharset());
		int index = event.getAuthor().getLongID() == IDs.VAUFF ? 0 : 1;

		Utilities.sendMessage(event, (index == 0 ? "bl4ckscor3" : "Vauff") + " upgraded your extruder " + contents.get(index).split(":")[1] + " times ( ͡° ͜ʖ ͡°)");
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().equalsIgnoreCase("-upgrades");
	}
	
	@Override
	public long[] allowedChannels()
	{
		return new long[] {IDs.EXTRUDERS};
	}
}
