package dev.tricht.poe.assistant.item;

import lombok.Data;

import java.awt.*;

@Data
public class Item {
    private Point mousePosition;
    private String name;
    private Integer meanPrice;
    private String iconUrl;
    private boolean isMap;
}
