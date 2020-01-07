package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.tricht.lunaris.com.pathofexile.response.AffixGroup;
import lombok.Data;

import java.util.List;

@Data
public class StatsResponse {
    @JsonProperty("result")
    private List<AffixGroup> affixGroup;
}
