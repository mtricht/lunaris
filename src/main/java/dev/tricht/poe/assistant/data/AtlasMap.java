package dev.tricht.poe.assistant.data;

import lombok.Data;

import java.util.List;

@Data
public class AtlasMap {
    private String name;
    private String region;
    private List<String> bosses;
}
