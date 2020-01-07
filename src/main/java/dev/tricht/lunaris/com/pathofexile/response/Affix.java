package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public
class Affix {
    private String id;
    private String text;
}
