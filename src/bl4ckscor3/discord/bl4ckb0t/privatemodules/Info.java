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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Info extends AbstractModule
{
	public Info(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		Utilities.sendMessage(event, new EmbedBuilder().setColor(new Color(new Random().nextInt(0xFFFFFF)))
				.addField("Version", Main.VERSION, true)
				.addField("Uptime", TimeParser.longToString(ManagementFactory.getRuntimeMXBean().getUptime(), "%s:%s:%s:%s"), true)
				.addField("Build Date", "" + new Date(getBuildDate()), true)
				.addField("Java Version", System.getProperty("java.version"), true)
				.addField("Source", "https://github.com/bl4ckscor3/bl4ckb0t-discord", true)
				.addField("Built with JDA", "https://github.com/DV8FromTheWorld/JDA", true)
				.addField("Author", "bl4ckscor3", true).build());
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().toLowerCase().equals("-info");
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
