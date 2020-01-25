package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public
class Affix {
    private String id;
    private String text;
    private Double value; // Only set by us
    public long getIdLong() {
        return Long.parseLong(id.replaceAll("\\D+", ""));
    }
}
