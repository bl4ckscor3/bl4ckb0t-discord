package bl4ckscor3.discord.bl4ckb0t.privatemodules;

import java.awt.Color;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.util.Date;
import java.util.Random;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class Info extends AbstractModule
{
	public Info(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.sendMessage(event, new EmbedBuilder().withColor(new Color(new Random().nextInt(0xFFFFFF)))
				.appendField("Version", Main.VERSION, true)
				.appendField("Uptime", TimeParser.longToString(ManagementFactory.getRuntimeMXBean().getUptime(), "%s:%s:%s:%s"), true)
				.appendField("Build Date", "" + new Date(getBuildDate()), true)
				.appendField("Java Version", System.getProperty("java.version"), true)
				.appendField("Author", "bl4ckscor3", true).build());
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContent().toLowerCase().equals("-info");
	}

	/**
	 * Gets the build date of the jar
	 * @return The build date of the jar as a long, 0 if no jar file has been found
	 */
	private long getBuildDate()
	{
		try
		{
			return ((JarURLConnection)ClassLoader.getSystemResource(Main.class.getName().replace('.', '/') + ".class").openConnection()).getJarFile().getEntry("META-INF/MANIFEST.MF").getTime();
		}
		catch(Exception e)
		{
			return 0L;
		}
	}
}
