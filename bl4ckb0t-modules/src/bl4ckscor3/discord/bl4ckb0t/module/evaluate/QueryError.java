package bl4ckscor3.discord.bl4ckb0t.module.evaluate;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public record QueryError(boolean error, Info info) {
	public record Info(String code, String msg) {}

	public static class Adapter extends TypeAdapter<QueryError> {
		public Adapter() {}

		@Override
		public void write(JsonWriter out, QueryError value) throws IOException {
			throw new UnsupportedOperationException("Only used for deserialization");
		}

		@Override
		public QueryError read(JsonReader in) throws IOException {
			return switch (in.peek()) {
				case BOOLEAN -> new QueryError(in.nextBoolean(), new Info("", ""));
				case BEGIN_OBJECT -> new QueryError(true, Evaluate.GSON.fromJson(in, Info.class));
				default -> throw new RuntimeException("Unhandled case in QueryError$Adapter#read: " + in.peek());
			};
		}
	}
}
