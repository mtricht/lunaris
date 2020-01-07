package dev.tricht.lunaris.ninja.poe;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteItem {
    @JsonAlias({"currencyTypeName", "name"})
    private String name;
    @JsonAlias({"chaosEquivalent", "chaosValue"})
    private double price;
    @JsonProperty("icon")
    private String iconUrl;
    @JsonProperty("mapTier")
    private int mapTier;
    @JsonProperty("levelRequired")
    private int itemLevel;

    @JsonProperty("variant")
    private String influence;

    private String reason;

    @JsonProperty("lowConfidenceSparkline")
    private GraphData lowConfidenceGraphData;

    @JsonProperty("sparkline")
    private GraphData graphData;


    public boolean isLowConfidence() {
        if (getLowConfidenceGraphData() != null) {
            if (getLowConfidenceGraphData().equals(getGraphData())) {
                return false;
            }

            return !getLowConfidenceGraphData().isEmpty();
        }
        return false;
    }
}
