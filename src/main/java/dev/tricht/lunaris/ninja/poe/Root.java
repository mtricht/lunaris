package dev.tricht.lunaris.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Root {
    @JsonProperty("lines")
    private List<RemoteItem> items;
    @JsonProperty("currencyDetails")
    private List<CurrencyDetail> currencyDetails;
}
