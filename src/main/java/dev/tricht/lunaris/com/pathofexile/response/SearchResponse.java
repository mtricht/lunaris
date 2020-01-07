package dev.tricht.lunaris.com.pathofexile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    private String id;
    private String total;
    private List<String> result;
}
