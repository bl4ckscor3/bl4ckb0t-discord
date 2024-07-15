package bl4ckscor3.discord.bl4ckb0t.module.weather;

import java.util.List;

//@formatter:off
public record GeoResults(List<Result> results) {
    public record Result(
            String name,
            float latitude,
            float longitude,
            String country) {}
}