package dev.tricht.poe.assistant.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MapInfoResolver {

    private Map<String, MapInfo> maps = new HashMap<>();

    public MapInfoResolver() {
        ObjectMapper mapper = new ObjectMapper();
        List<AtlasMap> atlasMaps = new ArrayList<>();
        try {
            atlasMaps = mapper.readValue(MapInfoResolver.class.getResourceAsStream("/data/atlas_maps.json"), new TypeReference<List<AtlasMap>>(){});
        } catch (IOException e) {
            log.error("Failed to load atlas_maps.json", e);
        }
        List<Pantheon> pantheons = new ArrayList<>();
        try {
            pantheons = mapper.readValue(MapInfoResolver.class.getResourceAsStream("/data/pantheons.json"), new TypeReference<List<Pantheon>>(){});
        } catch (IOException e) {
            log.error("Failed to load pantheons.json", e);
        }
        combine(atlasMaps, pantheons);
    }

    private void combine(List<AtlasMap> atlasMaps, List<Pantheon> pantheons) {
        Map<String, String> pantheonsByBoss = new HashMap<>();
        for (Pantheon pantheon : pantheons) {
            pantheonsByBoss.put(pantheon.getBoss(), pantheon.getPantheonStatText());
        }
        for (AtlasMap atlasMap : atlasMaps) {
            MapInfo mapInfo = new MapInfo();
            mapInfo.setName(atlasMap.getName());
            mapInfo.setBosses(atlasMap.getBosses());
            mapInfo.setRegion(atlasMap.getRegion());
            for (String boss : mapInfo.getBosses()) {
                if (pantheonsByBoss.containsKey(boss)) {
                    mapInfo.setPantheon(pantheonsByBoss.get(boss));
                }
            }
            maps.put(mapInfo.getName(), mapInfo);
        }
    }

    public MapInfo getMapInfo(String name) {
        return maps.get(name);
    }

}
