package dev.tricht.lunaris.data.seeds;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.data.AtlasMap;
import dev.tricht.lunaris.data.MapInfo;
import dev.tricht.lunaris.data.Pantheon;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SeedResolver {

    private Map<String, SeedInfo> seedMap = new HashMap<>();

    public SeedResolver() {
        ObjectMapper mapper = new ObjectMapper();
        List<SeedInfo> seeds = new ArrayList<>();
        try {
            seeds = mapper.readValue(SeedResolver.class.getResourceAsStream("/data/seeds.json"), new TypeReference<List<SeedInfo>>(){});
        } catch (IOException e) {
            log.error("Failed to load seeds.json", e);
        }
        for (SeedInfo seed : seeds) {
            seedMap.put(seed.getName(), seed);
        }
    }

    public SeedInfo getSeedInfo(String name) {
        return seedMap.get(name);
    }
}
