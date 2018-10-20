package bl4ckscor3.discord.bl4ckb0t;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import bl4ckscor3.discord.bl4ckb0t.privatemodules.Exit;
import bl4ckscor3.discord.bl4ckb0t.privatemodules.ModuleManagement;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import sx.blah.discord.api.ClientBuilder;

public class ModuleManager
{
	public static final ArrayList<AbstractModule> MODULES = new ArrayList<AbstractModule>();
	private ClientBuilder builder;

	public ModuleManager(ClientBuilder c)
	{
		builder = c;
	}

	/**
	 * Loads all installed modules
	 */
	public void initPublic() throws URISyntaxException, IOException
	{
		File folder = new File(Utilities.getJarLocation() + "/modules");

		if(!folder.exists())
			folder.mkdirs();

		for(File f : folder.listFiles())
		{
			if(f.isFile() && f.getName().endsWith(".jar")) //it's likely a module
				loadModule(f.toURI().toURL(), f.getName().split("\\.")[0]);
		}
	}
	/**
	 * Loads all modules that are getting shipped with the bot
	 */
	public void initPrivate()
	{
		AbstractModule[] privateModules = {
				new Exit("Exit"),
				new ModuleManagement("ModuleManagement")
		};

		for(AbstractModule m : privateModules)
		{
			try
			{
				m.onEnable(builder);
				MODULES.add(m);
				System.out.println("  Loaded module " + m.getName());
			}
			catch(Exception e)
			{
				System.out.println(" Module " + m.getName() + " could not be loaded due to an error");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads a public module
	 * @param url The url to the file of the module (file.getURI().getURL())
	 * @param name The name of the file without the file type
	 * @return 1 if the module was loaded, 0 if it was already loaded, -1 if an error occured
	 */
	public int loadModule(URL url, String name) throws IOException
	{
		if(isModuleLoaded(name))
			return 0;

		String main = "bl4ckscor3.discord.bl4ckb0t.module." + name.toLowerCase() + "." + name;
		URLClassLoader loader = null;

		try
		{
			loader = URLClassLoader.newInstance(new URL[]{url}, getClass().getClassLoader());

			AbstractModule module = Class.forName(main, true, loader).asSubclass(AbstractModule.class).getDeclaredConstructor(String.class).newInstance(name);

			if(MODULES.contains(module))
			{
				System.out.println("Tried to load already loaded module.");
				return 0;
			}

			module.setLoader(loader);
			module.onEnable(builder);
			MODULES.add(module);
			System.out.println("  Loaded module " + name);
			return 1;
		}
		catch(ClassCastException e)
		{
			System.err.println("  " + name + ": Main class does not extend bl4ckscor3.bot.bl4ckb0t.AbstractModule");
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("  " + name + ": Couldn't find main class " + main);
		}
		catch(Exception e)
		{
			System.err.println("  AbstractModule " + name + " could not be loaded due to an error. Is it even a module?");
			e.printStackTrace();
		}

		if(loader != null)
			loader.close();

		return -1;
	}

	/**
	 * Checks whether a module with the given name is loaded
	 * @param name The module name to check
	 * @return true if the module is loaded, false otherwise
	 */
	public static boolean isModuleLoaded(String name)
	{
		for(AbstractModule m : MODULES)
		{
			if(m.getName().equalsIgnoreCase(name))
				return true;
		}

		return false;
	}
}
