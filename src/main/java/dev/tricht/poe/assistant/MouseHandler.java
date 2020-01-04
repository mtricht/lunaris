package dev.tricht.poe.assistant;

import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.mouse.event.GlobalMouseListener;

import java.awt.*;

public class MouseHandler implements GlobalMouseListener {

    static Point position;

    @Override
    public void mousePressed(GlobalMouseEvent event) {
    }

    @Override
    public void mouseReleased(GlobalMouseEvent event) {
    }

    @Override
    public void mouseMoved(GlobalMouseEvent event) {
        position = new Point(event.getX(), event.getY());
    }

    @Override
    public void mouseWheel(GlobalMouseEvent event) {
    }
}
