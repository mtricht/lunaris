package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingResponse {
    private List<Item> result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private ItemListing listing;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemListing {
        private String indexed;
        private Price price;
        private Account account;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
        private int amount;
        private String currency;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        private String lastCharacterName;
    }
}
