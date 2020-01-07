package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public
class AffixGroup {
    private String label;
    @JsonProperty("entries")
    private List<Affix> affixResponses;
}
