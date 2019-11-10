package bl4ckscor3.discord.bl4ckb0t.module.tweetvotemarker;

import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TweetVoteMarker extends AbstractModule
{
	public TweetVoteMarker(String name)
	{
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception
	{
		String message = event.getMessage().getContentRaw();

		for(String link : message.split(" "))
		{
			if((link.contains("www.") || link.contains("http://") || link.contains("https://")) && link.contains("twitter"))
			{
				if(link.startsWith("www."))
					link = "http://" + link;

				if(link.split("/").length < 6) //it's not a tweet
					return;

				Document doc = null;

				try
				{
					doc = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.183 Safari/537.36 Vivaldi/1.96.1147.42").get();
				}
				catch(HttpStatusException e)
				{
					System.out.println("[TweetVoteMarker] ERROR: " + e.getStatusCode());
					return;
				}

				List<Element> vote = doc.select(".tweet").get(0).select(".card2");

				for(Element e : vote)
				{
					if(e.hasAttr("data-card2-name") && e.attr("data-card2-name").matches("poll[0-9]+choice_text_only"))
					{
						Utilities.react(event.getMessage(), "🔢"); //1234
						return;
					}
				}
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event)
	{
		String message = event.getMessage().getContentRaw();

		for(String link : message.split(" "))
		{
			if((link.contains("www.") || link.contains("http://") || link.contains("https://")) && link.contains("twitter"))
				return true;
		}

		return false;
	}
}
