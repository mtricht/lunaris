package dev.tricht.venarius.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class CurrencyDetail {
    @JsonProperty("name")
    private String name;
    @JsonProperty("icon")
    private String iconUrl;
}
