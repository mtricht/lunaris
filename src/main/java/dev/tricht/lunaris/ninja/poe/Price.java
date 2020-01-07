package dev.tricht.lunaris.ninja.poe;

import lombok.Data;

@Data
public class Price {
    private double price;
    private boolean lowConfidence = false;
    private String reason = null;
}
