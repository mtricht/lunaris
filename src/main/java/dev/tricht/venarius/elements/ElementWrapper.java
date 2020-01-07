package dev.tricht.venarius.elements;

import javafx.scene.Node;

public class ElementWrapper implements Element {

    private Node node;

    public ElementWrapper(Node node) {
        this.node = node;
    }

    @Override
    public Node build() {
        return node;
    }
}
