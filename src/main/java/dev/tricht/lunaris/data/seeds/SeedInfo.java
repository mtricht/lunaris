package dev.tricht.lunaris.data.seeds;

import lombok.Data;

import java.util.List;

@Data
public class SeedInfo {
    private String name;
    private String effect;
    private List<String> effectList;
}
