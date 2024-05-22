package bl4ckscor3.discord.bl4ckb0t.builtin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.ModuleManager;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ModuleManagement extends AbstractModule implements BuiltInModule {
	public ModuleManagement(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		MessageChannel channel = event.getChannel();

		if (args.length == 2) {
			File folder = new File(Utilities.getJarLocation() + "/modules");

			switch (args[0]) {
				case "disable":
					Utilities.sendMessage(channel, disableModule(args[1], folder));
					break;
				case "enable":
					Utilities.sendMessage(channel, enableModule(args[1], folder));
					break;
				case "reload", "restart":
					Utilities.sendMessage(channel, disableModule(args[1], folder));
					Utilities.sendMessage(channel, enableModule(args[1], folder));
					break;
				case "load":
					loadModule(event, args, channel);
					break;
				default:
					Utilities.sendMessage(channel, "Unknown command.");
			}
		}
		else if (args.length == 1 && args[0].equals("list")) {
			EmbedBuilder embed = new EmbedBuilder().setTitle("Active modules").setFooter("(italics = built-in)").setColor(Main.RANDOM.nextInt(0xFFFFFF));

			for (AbstractModule module : ModuleManager.MODULES) {
				if (module instanceof BuiltInModule)
					embed.appendDescription("*" + module.getName() + "* | ");
				else
					embed.appendDescription(module.getName() + " | ");
			}

			//can't know the actual length of the string (unless i put in effort), so in order to remove the last 3 characters:
			//reverse the string, remove the first 3, then reverse the string again to get it back to the order it was in before
			embed.getDescriptionBuilder().reverse().replace(0, 3, "").reverse();
			Utilities.sendMessage(channel, embed.build());
		}
	}

	private String disableModule(String requestedModule, File folder) {
		for (File f : folder.listFiles()) {
			String name = f.getName().split("\\.")[0];

			if (name.equalsIgnoreCase(requestedModule)) {
				if (f.getName().endsWith(".disabled"))
					return "This module has already been disabled.";

				for (AbstractModule m : ModuleManager.MODULES) {
					if (m.getName().equalsIgnoreCase(name)) {
						ModuleManager.MODULES.remove(m);
						m.onDisable();
						break;
					}
				}

				if (!f.renameTo(new File(f.getAbsolutePath() + ".disabled")))
					return "Renaming the file did not work!";

				return "The module has been successfully disabled!";
			}
		}

		return "This module is a built-in module or doesn't exist.";
	}

	private String enableModule(String requestedModule, File folder) {
		for (File f : folder.listFiles()) {
			String name = f.getName().split("\\.")[0];

			if (name.equalsIgnoreCase(requestedModule)) {
				if (!f.getName().endsWith(".disabled") && ModuleManager.isModuleLoaded(name))
					return "This module is already enabled.";

				if (!f.renameTo(new File(f.getAbsolutePath().replace(".disabled", ""))))
					return "Renaming the file did not work!";

				int loadState = -1;

				try {
					loadState = Main.getModuleManager().loadModule(new URL(f.toURI().toURL().toString().replace(".disabled", "")), name);
				}
				catch (MalformedURLException e) {
					e.printStackTrace();
				}

				if (loadState == 1)
					return "The module has been successfully enabled!";
				else if (loadState == 0)
					return "This module is already enabled.";
				else
					return "The module was not enabled due to an error. See log for details.";
			}
		}

		return "This module is a built-in module or doesn't exist.";
	}

	private void loadModule(MessageReceivedEvent event, String[] args, MessageChannel channel) {
		try {
			String name = FilenameUtils.getName(args[1].contains("?") ? args[1].substring(0, args[1].indexOf('?')) : args[1]);
			URL link = new URL(args[1]);
			File disabled = new File(Utilities.getJarLocation() + "/modules/" + name + ".disabled");

			if (disabled.exists()) //delete disabled file just in case
				Files.delete(disabled.toPath());

			//the substring call removes all parameters of the link
			try (ReadableByteChannel rbc = Channels.newChannel(link.openStream()); FileOutputStream stream = new FileOutputStream(Utilities.getJarLocation() + "/modules/" + name)) {
				stream.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE); //maximum download of a 2gb file

				int loadState = Main.getModuleManager().loadModule(new URL("file:" + Utilities.getJarLocation() + "/modules/" + name), name.substring(0, name.lastIndexOf('.')));

				if (loadState == 1)
					Utilities.sendMessage(channel, "The module was loaded successfully.");
				else if (loadState == 0)
					exe(event, new String[] {
							"reload", name.substring(0, name.lastIndexOf('.'))
					});
				else
					Utilities.sendMessage(channel, "There was an error while loading the module. See log for details.");
			}
		}
		catch (MalformedURLException e) {
			Utilities.sendMessage(channel, "That is not a valid URL.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean requiresPermission() {
		return true;
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().toLowerCase().startsWith("-module");
	}
}
