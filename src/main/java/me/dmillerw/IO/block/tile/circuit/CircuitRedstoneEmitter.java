package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;

/**
 * @author dmillerw
 */
public class CircuitRedstoneEmitter extends TileToolContainer {

    public static final String KEY_REDSTONE_LEVEL = "RedstoneLevel";

    public int redstoneLevel = 0;

    @Override
    public void initialize() {
        setName("Redstone Emitter");

        registerInput(KEY_REDSTONE_LEVEL, DataType.NUMBER);
    }

    @Override
    public void triggerInputChange(String port, Object value) {
        if (port.equals(KEY_REDSTONE_LEVEL)) {
            redstoneLevel = ((Number)value).intValue();

            markDirtyAndNotify();
            notifyNeighbors();
        }
    }
}
