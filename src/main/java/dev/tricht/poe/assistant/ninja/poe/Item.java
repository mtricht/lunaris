package dev.tricht.poe.assistant.ninja.poe;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JsonAlias({"currencyTypeName", "name"})
    private String name;
    @JsonAlias({"chaosEquivalent", "chaosValue"})
    private int price;
    @JsonProperty("icon")
    private String iconUrl;
}