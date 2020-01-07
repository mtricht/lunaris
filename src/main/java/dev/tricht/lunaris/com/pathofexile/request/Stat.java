package dev.tricht.lunaris.com.pathofexile.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Stat {
    private String type = "and";
    private List<StatFilter> filters = new ArrayList<>();
}
