package dev.tricht.lunaris.item.parser;

import java.util.ArrayList;

public class ImplicitPart {


    private ArrayList<ArrayList<String>> parts;

    public ImplicitPart(ArrayList<ArrayList<String>> parts) {
        this.parts = parts;
    }


    public ArrayList<String> getImplicits() {
        //TODO: Items can have multiple influence types

        ArrayList<String> implicits = new ArrayList<>();

        for (ArrayList<String> part : parts) {
            for (String line : part) {
                if (line.contains("(implicit)")) {
                    implicits.add(line.replace(" (implicit)", ""));
                }
            }
        }
        return implicits;
    }


}
