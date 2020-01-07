package dev.tricht.venarius.data;

import lombok.Data;

import java.util.List;

@Data
public class MapInfo {
    private String name;
    private String region;
    private List<String> bosses;
    private String pantheon;
}
