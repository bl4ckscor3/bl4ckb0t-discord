package bl4ckscor3.discord.bl4ckb0t.module.forge113check;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.Main;
import bl4ckscor3.discord.bl4ckb0t.util.IDs;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Forge113Check extends AbstractModule
{
	private static final long EXPULSERS = 401899796564279296L;
	private static final long GEFORCE = 225350861138821122L;
	private ScheduledFuture sf;

	public Forge113Check(String name)
	{
		super(name);
	}

	@Override
	public void onEnable(ClientBuilder builder)
	{
		sf = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://files.minecraftforge.net/").openStream())))
			{
				String s;

				while((s = reader.readLine()) != null)
				{
					if(s.contains("li-version-list"))
					{
						reader.readLine();
						reader.readLine();
						s = reader.readLine();

						if(s.contains("1.13"))
						{
							Main.client().getChannelByID(EXPULSERS).sendMessage("<@" + IDs.BL4CKSCOR3 + ">, <@" + GEFORCE + ">: Minecraft Forge 1.13 is out! :D");
							sf.cancel(true);
						}
						else
							return;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}, 0L, 1L, TimeUnit.HOURS);
	}

	@Override
	public void onDisable()
	{
		if(sf != null)
			sf.cancel(true);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		return false;
	}
}
