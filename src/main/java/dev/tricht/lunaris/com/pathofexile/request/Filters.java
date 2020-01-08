package dev.tricht.lunaris.com.pathofexile.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Filters {

    @JsonProperty("misc_filters")
    private NestedFilters nestedFilters;

    @JsonProperty("req_filters")
    private NestedFilters reqFilters;

    @JsonProperty("socket_filters")
    private NestedFilters socketFilters;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class NestedFilters {
        private DeeperFilters filters;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class DeeperFilters {
        private Option corrupted;
        private Value ilvl;
        private Option mirrored;
        private Value quality;
        private Value str;
        @JsonProperty("int")
        private Value intelligence;
        private Value dex;
        private Value links;

        @JsonProperty("gem_level")
        private Value gemLevel;
    }

}
