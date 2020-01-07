package dev.tricht.venarius.com.pathofexile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PathOfExileAPI {

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    public PathOfExileAPI() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<String> getTradeLeagues() {
        return getLeagues().stream().filter(s -> !s.contains("SSF")).collect(Collectors.toList());
    }

    public List<String> getLeagues() {
        Request request = new Request.Builder()
                .url("http://api.pathofexile.com/leagues")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            List<League> leagues = objectMapper.readValue(response.body().string(), new TypeReference<List<League>>(){});
            return leagues.stream().map(League::getId).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to get leagues", e);
        }
    }
}
