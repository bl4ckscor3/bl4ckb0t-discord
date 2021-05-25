package bl4ckscor3.discord.bl4ckb0t.privatemodules;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.util.Date;
import java.util.Random;
import java.util.jar.JarFile;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Info extends AbstractModule implements BuiltInModule
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
				.addField("Compiled with", "Java " + (getClassFormatVersion() - 44), true) //- 44 to get the java version from the class format version (dirty, but works until this is no longer consistent)
				.addField("Running on", "Java " + System.getProperty("java.version"), true)
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
		try(JarFile jar = getJarFile())
		{
			return jar.getEntry("META-INF/MANIFEST.MF").getTime();
		}
		catch(Exception e)
		{
			return 0L;
		}
	}

	/**
	 * Gets the major class format version of the jar file, representing the Java version it was compiled with
	 * @return The major class format version (NOT the Java version). 0 if no jar file has been found
	 */
	private int getClassFormatVersion()
	{
		try(JarFile jar = getJarFile(); DataInputStream input = new DataInputStream(jar.getInputStream(jar.getEntry("bl4ckscor3/discord/bl4ckb0t/Main.class"))))
		{
			input.readUnsignedShort(); //cafe
			input.readUnsignedShort(); //babe
			input.readUnsignedShort(); //minor version
			return input.readUnsignedShort(); //major version
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	/**
	 * Gets a connection to the jar file of this programm
	 * @return The connection to the jar file
	 */
	private JarFile getJarFile() throws IOException
	{
		return ((JarURLConnection)ClassLoader.getSystemResource(Main.class.getName().replace('.', '/') + ".class").openConnection()).getJarFile();
	}
}
