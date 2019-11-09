package bl4ckscor3.discord.bl4ckb0t.privatemodules;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.io.FilenameUtils;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.ModuleManager;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ModuleManagement extends AbstractModule
{
	public ModuleManagement(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		MessageChannel channel = event.getChannel();

		if(args.length == 2)
		{
			File folder = new File(Utilities.getJarLocation() + "/modules");

			switch(args[0])
			{
				case "disable":
					for(File f : folder.listFiles())
					{
						String name = f.getName().split("\\.")[0];

						if(name.equalsIgnoreCase(args[1]))
						{
							if(f.getName().endsWith(".disabled"))
							{
								Utilities.sendMessage(channel, "This module has already been disabled.");
								return;
							}

							inner:
								for(AbstractModule m : ModuleManager.MODULES)
								{
									if(m.getName().equalsIgnoreCase(name))
									{
										ModuleManager.MODULES.remove(m);
										m.onDisable();
										m.closeLoader();
										break inner;
									}
								}

							if(!f.renameTo(new File(f.getAbsolutePath() + ".disabled")))
							{
								Utilities.sendMessage(channel, "There was a problem while disabling the module.");
								System.out.println("Renaming the file did not work!");
								return;
							}

							Utilities.sendMessage(channel, "Module has been successfully disabled.");
							return;
						}
					}

					Utilities.sendMessage(channel, "This module is a private module or doesn't exist.");
					break;
				case "enable":
					for(File f : folder.listFiles())
					{
						String name = f.getName().split("\\.")[0];

						if(name.equalsIgnoreCase(args[1]))
						{
							if(!f.getName().endsWith(".disabled") && ModuleManager.isModuleLoaded(name))
							{
								Utilities.sendMessage(channel, "This module is already enabled.");
								return;
							}

							if(!f.renameTo(new File(f.getAbsolutePath().replace(".disabled", ""))))
							{
								Utilities.sendMessage(channel, "There was a problem while enabling the module.");
								System.out.println("Renaming the file did not work!");
								return;
							}

							int loadState = Main.manager.loadModule(new URL(f.toURI().toURL().toString().replace(".disabled", "")), name);

							if(loadState == 1)
								Utilities.sendMessage(channel, "Module has been successfully enabled.");
							else if(loadState == 0)
								Utilities.sendMessage(channel, "This module is already enabled.");
							else
								Utilities.sendMessage(channel, "The module was not enabled due to an error. See log for details.");

							return;
						}
					}

					Utilities.sendMessage(channel, "This module is a private module or doesn't exist.");
					break;
				case "reload": case "restart":
					exe(event, new String[]{"disable", args[1]});
					exe(event, new String[]{"enable", args[1]});
					break;
				case "load":
					try
					{
						String name = FilenameUtils.getName(args[1].contains("?") ? args[1].substring(0, args[1].indexOf('?')) : args[1]);
						URL link = new URL(args[1]);
						ReadableByteChannel rbc = Channels.newChannel(link.openStream());
						FileOutputStream stream = new FileOutputStream(Utilities.getJarLocation() + "/modules/" + name); //the substring call removes all parameters of the link
						File disabled = new File(Utilities.getJarLocation() + "/modules/" + name + ".disabled");

						if(disabled.exists()) //delete disabled file just in case
							disabled.delete();

						stream.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE); //maximum download of a 2gb file
						stream.close();
						rbc.close();

						int loadState = Main.manager.loadModule(new URL("file:" + Utilities.getJarLocation() + "/modules/" + name), name.substring(0, name.lastIndexOf('.')));

						if(loadState == 1)
							Utilities.sendMessage(channel, "The module was loaded successfully.");
						else if(loadState == 0)
							exe(event, new String[]{"reload", name.substring(0, name.lastIndexOf('.'))});
						else
							Utilities.sendMessage(channel, "There was an error while loading the module. See log for details.");
					}
					catch(MalformedURLException e)
					{
						Utilities.sendMessage(channel, "That is not a valid URL.");
					}
			}
		}
	}

	@Override
	public boolean requiresPermission()
	{
		return true;
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return event.getMessage().getContentRaw().toLowerCase().startsWith("-module");
	}
}
