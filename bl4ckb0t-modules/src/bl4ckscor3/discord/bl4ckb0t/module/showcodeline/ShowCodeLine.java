package bl4ckscor3.discord.bl4ckb0t.module.showcodeline;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShowCodeLine extends AbstractModule {
	public ShowCodeLine(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) throws Exception {
		String message = event.getMessage().getContentRaw();

		for (String link : message.split(" ")) {
			if ((link.contains("www.") || link.contains("http://") || link.contains("https://")) && link.contains("github") && link.contains("#L")) {
				if (link.startsWith("www."))
					link = "http://" + link;

				Document doc = null;

				try {
					doc = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.183 Safari/537.36 Vivaldi/1.96.1147.42").get();
				}
				catch (HttpStatusException e) {
					System.out.println("[ShowCodeLine] ERROR: " + e.getStatusCode());
					return;
				}

				StringBuilder builder = new StringBuilder("```");
				String fileExtension = doc.getElementsByClass("final-path").get(0).text();
				String firstSplit = link.split("#L")[1];
				int firstCodeLine;
				int lastCodeLine;

				if (firstSplit.contains("-")) {
					String[] secondSplit = firstSplit.split("-");

					firstCodeLine = Integer.parseInt(secondSplit[0]);

					if (secondSplit[1].startsWith("L"))
						lastCodeLine = Integer.parseInt(secondSplit[1].substring(1));
					else
						lastCodeLine = Integer.parseInt(secondSplit[1]);
				}
				else
					firstCodeLine = lastCodeLine = Integer.parseInt(firstSplit);

				if (fileExtension.split("\\.").length > 0) //for syntax highlighting, just put the file extension. if it's unrecognized, it's ignored by discord
					builder.append(fileExtension.split("\\.")[1]);

				builder.append("\n");

				for (int currentCodeLine = firstCodeLine; currentCodeLine <= lastCodeLine; currentCodeLine++) {
					String codeLine = doc.getElementById("LC" + currentCodeLine).wholeText();

					if (builder.length() + codeLine.length() < 1997) //2000 is the maximum message length, and there still needs to be space for the three backticks to close the code block
						builder.append(codeLine + "\n");
					else
						break;
				}

				builder.append("```");
				Utilities.sendMessage(event.getChannel(), builder.toString());
			}
		}
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();

		for (String link : message.split(" ")) {
			if ((link.contains("www.") || link.contains("http://") || link.contains("https://")) && link.contains("github") && link.contains("#L"))
				return true;
		}

		return false;
	}
}
