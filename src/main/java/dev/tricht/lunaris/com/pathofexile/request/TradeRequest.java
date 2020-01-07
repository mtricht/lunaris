package dev.tricht.lunaris.com.pathofexile.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TradeRequest {
    private Query query;
    private Sort sort = new Sort();
}
