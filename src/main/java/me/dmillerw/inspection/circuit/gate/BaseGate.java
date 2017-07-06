package me.dmillerw.inspection.circuit.gate;

import me.dmillerw.inspection.block.tile.TileGateContainer;

/**
 * @author dmillerw
 */
public class BaseGate {

    public static enum Category {

        ARITHMATIC;
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

    public void initialize(TileGateContainer parentTile) {

    }

    public void calculateOutput(TileGateContainer parentTile) {

    }
}
