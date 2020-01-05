package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.elements.*;
import dev.tricht.poe.assistant.elements.Image;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemGrabber;
import dev.tricht.poe.assistant.tooltip.TooltipCreator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class MapInfoListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;

    public MapInfoListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (event.getKeyCode() == NativeKeyEvent.VC_A && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
            try {
                Item item = this.itemGrabber.grab();

                Label bossInfo = new Label("Boss: Terror of the Infinite Drifts");
                bossInfo.setTextFill(javafx.scene.paint.Color.WHITE);
                bossInfo.setFont(new Font("Arial", 12));
                bossInfo.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
                bossInfo.setPadding(new Insets(10, 10, 10, 10));

                Label pantheonInfo = new Label("Pantheon: Immune to poison");
                pantheonInfo.setTextFill(javafx.scene.paint.Color.WHITE);
                pantheonInfo.setFont(new Font("Arial", 12));
                pantheonInfo.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
                pantheonInfo.setPadding(new Insets(10, 10, 10, 10));

                Map<Element, int[]> elements = Map.ofEntries(
                        new AbstractMap.SimpleEntry<Element, int[]>(new Icon(item, 48), new int[]{0, 0}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new ItemName(item,48 + Icon.PADDING), new int[]{1, 0}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new Image("desert_spring.png"), new int[]{1, 1}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new ElementWrapper(bossInfo), new int[]{1, 2}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new ElementWrapper(pantheonInfo), new int[]{1, 3})
                );

                TooltipCreator.create(position, elements);

            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent event) {
        position = event.getPoint();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent event) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent event) {
        TooltipCreator.destroy();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }
}
