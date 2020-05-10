package dev.tricht.lunaris.com.pathofexile.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Query {
    private List<Stat> stats = new ArrayList<>();
    public Filters filters;
    private Status status = new Status();
    private String term;
    private String name;
    private String type;
}
