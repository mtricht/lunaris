package dev.tricht.poe.assistant.ninja.poe;

import lombok.SneakyThrows;
import okhttp3.*;

import java.io.*;

public class Downloader {

    private static OkHttpClient client;
    private static File dataDirectory;
    private static final int HOUR_IN_MILLI = 60 * 60 * 1000;

    public static void download() throws IOException {
        client = new OkHttpClient();
        ensureDataDirectory();
        downloadAll();
    }

    private static void ensureDataDirectory() {
        String appData = System.getenv("APPDATA");
        dataDirectory = new File(appData + "\\PoEAssistant\\data");
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create directory.");
            }
        }
    }

    private static void downloadAll() throws IOException {
        File lastUpdatedFile = new File(dataDirectory.getAbsolutePath() + "\\.last-updated");
        if (lastUpdatedFile.exists() && (System.currentTimeMillis() - lastUpdatedFile.lastModified()) < HOUR_IN_MILLI) {
            return;
        }
        for (String type : Types.currencyTypes) {
            downloadJson("https://poe.ninja/api/data/currencyoverview", type);
        }
        for (String type : Types.itemTypes) {
            downloadJson("https://poe.ninja/api/data/itemoverview", type);
        }
        lastUpdatedFile.createNewFile();
        lastUpdatedFile.setLastModified(System.currentTimeMillis());
    }

    @SneakyThrows
    private static void downloadJson(String baseUrl, String type) {
        System.out.println("Downloading " + type);
        Request request = new Request.Builder()
                .url(String.format("%s?type=%s&league=%s", baseUrl, type, "Metamorph"))
                .build();
        Response response;
        response = client.newCall(request).execute();
        FileOutputStream output = new FileOutputStream(dataDirectory + "\\" + type + ".json");
        output.write(response.body().bytes());
        output.close();
    }

}
