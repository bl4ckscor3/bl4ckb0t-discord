package bl4ckscor3.discord.bl4ckb0t.module.evaluate;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import bl4ckscor3.discord.bl4ckb0t.AbstractModule;
import bl4ckscor3.discord.bl4ckb0t.module.evaluate.ApiResponse.QueryResult;
import bl4ckscor3.discord.bl4ckb0t.module.evaluate.ApiResponse.QueryResult.Pod;
import bl4ckscor3.discord.bl4ckb0t.module.evaluate.ApiResponse.QueryResult.Pod.SubPod;
import bl4ckscor3.discord.bl4ckb0t.util.Utilities;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Evaluate extends AbstractModule {
	public static final Gson GSON = new Gson();

	public Evaluate(String name) {
		super(name);
	}

	@Override
	public void exe(MessageReceivedEvent event, String[] args) {
		String input = "";

		for (int i = 0; i < args.length; i++) {
			input += args[i] + " ";
		}

		Utilities.sendMessage(event, evaluate(event, input.trim()));
	}

	@Override
	public boolean triggeredBy(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();

		return message.startsWith("-calc") || message.startsWith("-eval") || message.startsWith("-evaluate") || message.startsWith("-calculate");
	}

	/**
	 * Queries WolframAlpha
	 *
	 * @param event The MessageEvent the command that triggered this got sent in
	 * @param input The query for WolframAlpha (text to send)
	 */
	private String evaluate(MessageReceivedEvent event, String input) {
		try {
			Document doc = Jsoup.connect("https://api.wolframalpha.com/v2/query?appid=" + Tokens.WOLFRAM_ALPHA + "&output=json&includepodid=Result&format=plaintext&input=" + input.trim().replace("+", "%2B").replace(' ', '+').replace(',', '.')).ignoreContentType(true).get();
			QueryResult queryResult = GSON.fromJson(doc.text(), ApiResponse.class).queryresult();
			QueryError queryError = queryResult.error();

			if (queryError.error()) {
				QueryError.Info errorInfo = queryError.info();

				if (errorInfo.msg().isBlank())
					return "❌ An unknown API error occured,";
				else
					return "❌ API error: " + errorInfo.msg();
			}
			else if (queryResult.success()) {
				List<Pod> pods = queryResult.pods();

				if (pods != null && !pods.isEmpty()) {
					List<SubPod> subPods = pods.get(0).subpods();

					if (subPods != null && !subPods.isEmpty()) {
						String result = subPods.get(0).plaintext();

						if (result.matches("\\d+/\\d+.*")) {
							String decimalResult = evaluate(event, input + " in decimal");

							if (!decimalResult.startsWith("❌"))
								return decimalResult;
						}

						return result;
					}
				}
			}
			else if (queryResult.didyoumeans() != null && !queryResult.didyoumeans().isEmpty())
				return "❌ Did you mean: " + queryResult.didyoumeans().get(0).val();

			return "❌ WolframAlpha couldn't find a result.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "❌ Error: " + e.getMessage();
		}
	}
}
