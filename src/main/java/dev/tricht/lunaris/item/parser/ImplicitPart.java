package dev.tricht.lunaris.item.parser;

import lombok.Getter;

import java.util.ArrayList;

public class ImplicitPart {

    private ArrayList<String> part;

    @Getter
    private boolean isRealImplicit = false;

    public ImplicitPart(ArrayList<String> part) {
        this.part = part;
    }


    public ArrayList<String> getImplicits() {
        ArrayList<String> implicits = new ArrayList<>();

        for (String line : part) {
            if (line.contains("(implicit)")) {
                isRealImplicit = true;
                implicits.add(line.replace(" (implicit)", ""));
                continue;
            }
            implicits.add(line);
        }
        return implicits;
    }
}
