package bl4ckscor3.discord.bl4ckb0t.module.evaluate;

import java.util.List;

import com.google.gson.annotations.JsonAdapter;

public record ApiResponse(QueryResult queryresult) {
	public record QueryResult(boolean success, @JsonAdapter(QueryError.Adapter.class) QueryError error, List<Pod> pods, List<DidYouMean> didyoumeans) {
		public record Pod(String title, List<SubPod> subpods) {
			public record SubPod(String title, String plaintext) {}
		}

		public record DidYouMean(String score, String level, String val) {}
	}
}
