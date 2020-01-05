package dev.tricht.poe.assistant.ninja.poe;

import lombok.Data;

@Data
public class Price {
    private double price;
    private boolean lowConfidence = false;
}
