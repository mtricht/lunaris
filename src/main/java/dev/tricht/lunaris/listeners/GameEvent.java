package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.item.Item;
import lombok.Data;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;

@Data
public class GameEvent {

    private Item item;
    private Point mousePos;
    private int mouseWheelRotation;
    private NativeKeyEvent originalEvent;
}
