package me.dmillerw.io.circuit.gate;

import me.dmillerw.io.block.tile.TileGateContainer;

/**
 * @author dmillerw
 */
public class BaseGate {

    public static enum Category {

        ARITHMATIC,
        LOGIC,
        UTIL,
        TIME;
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

    public void tick(TileGateContainer tileGateContainer, int lifespan) {

    }

    public void calculateOutput(TileGateContainer parentTile) {

    }
}
