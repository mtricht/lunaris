package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        public Date getTimeAgo() {
            return Date.from(
                    LocalDateTime.parse(indexed, DateTimeFormatter.ISO_DATE_TIME)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            );
        }
        public String getPrice() {
            if (price == null) {
                return "No price set";
            }
            return "~" + price.getAmount() + " " + price.getCurrency();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
        private double amount;
        private String currency;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        private String lastCharacterName;
    }
}
