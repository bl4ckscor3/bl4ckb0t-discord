package bl4ckscor3.discord.bl4ckb0t.builtin;

import java.awt.Color;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.util.Date;
import java.util.jar.JarFile;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.TimeParser;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Info extends AbstractModule implements BuiltInModule {
	public Info(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		try (JarFile jar = getJarFile()) {
			//@formatter:off
			Utilities.sendMessage(event, new EmbedBuilder()
					.setColor(new Color(Main.RANDOM.nextInt(0xFFFFFF)))
					.addField("Version", getBotVersion(jar), true)
					.addField("Uptime", TimeParser.longToString(ManagementFactory.getRuntimeMXBean().getUptime(), "%s:%s:%s:%s"), true)
					.addField("Build Date", "" + new Date(getBuildDate(jar)), true)
					.addField("Compiled with", "Java " + (getJdkVersion(jar)), true) //- 44 to get the java version from the class format version (dirty, but works until this is no longer consistent)
					.addField("Running on", "Java " + System.getProperty("java.version"), true)
					.addField("Source", "https://github.com/bl4ckscor3/bl4ckb0t-discord", true)
					.addField("Built with JDA " + JDAInfo.VERSION, JDAInfo.GITHUB, true)
					.addField("Author", "bl4ckscor3", true)
					.build());
			//@formatter:on
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().equalsIgnoreCase("-info");
	}

	/**
	 * Gets the bot's version from the jar file
	 *
	 * @param jar The bot's jar file
	 * @return The bot's version, an empty string if an IOException occurs.
	 */
	private String getBotVersion(JarFile jar) {
		try {
			return jar.getManifest().getMainAttributes().getValue("Implementation-Version");
		}
		catch (IOException e) {
			return "";
		}
	}

	/**
	 * Gets the build date of the jar
	 *
	 * @param jar The bot's jar file
	 * @return The build date of the jar as a long, 0 if no jar file has been found
	 */
	private long getBuildDate(JarFile jar) {
		return jar.getEntry("META-INF/MANIFEST.MF").getTime();
	}

	/**
	 * Gets the JDK version number that this jar file was built with
	 *
	 * @param jar The bot's jar file
	 * @return The JDK version number, an empty string if an IOException occurs.
	 */
	private String getJdkVersion(JarFile jar) {
		try {
			return jar.getManifest().getMainAttributes().getValue("Build-Jdk");
		}
		catch (IOException e) {
			return "";
		}
	}

	/**
	 * Gets the jar file of this programm
	 *
	 * @return The jar file
	 */
	private JarFile getJarFile() throws IOException {
		return ((JarURLConnection) ClassLoader.getSystemResource(Main.class.getName().replace('.', '/') + ".class").openConnection()).getJarFile();
	}
}
