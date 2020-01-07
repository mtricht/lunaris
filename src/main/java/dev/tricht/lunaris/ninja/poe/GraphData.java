package dev.tricht.lunaris.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphData {
    @JsonProperty("data")
    private double[] data;

    public boolean isSame(GraphData otherGraph) {
        return Arrays.equals(otherGraph.getData(), data);
    }

    public boolean isEmpty() {
        for (double point : data) {
            if (point == 0.0) {
                continue;
            }
            return false;
        }
        return true;
    }
}
