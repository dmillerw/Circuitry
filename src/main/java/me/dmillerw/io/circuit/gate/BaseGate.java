package me.dmillerw.io.circuit.gate;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.client.gui.config.element.Element;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public abstract class BaseGate {

    public static enum Category {

        ARITHMETIC,
        LOGIC,
        UTIL,
        TIME,
        ENTITY,
        VECTOR,
        COMPARISON;
    }

    private String key;
    private Category category;

    public BaseGate(String key, Category category) {
        this.key = key;
        this.category = category;
    }

    public final String getKey() {
        return key;
    }

    public final Category getCategory() {
        return category;
    }

    public void addElements(LinkedList<Element> elements) {}

    public abstract void initialize(TileGateContainer parentTile);

    public abstract void calculateOutput(TileGateContainer parentTile);

    public void tick(TileGateContainer tileGateContainer, int lifespan) {

    }
}
