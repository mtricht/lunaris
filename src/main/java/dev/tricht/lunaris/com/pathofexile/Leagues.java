package dev.tricht.lunaris.com.pathofexile;

import java.util.ArrayList;
import java.util.List;

public class Leagues {
    private static List<String> leagues;

    public static void load(PathOfExileAPI api) {
        leagues = new ArrayList<>();

        for (String league : api.getLeagues()) {
            if (!league.contains("SSF")) {
                leagues.add(league);
            }
        }
    }

    public static List<String> getLeagues() {
        return leagues;
    }
}
