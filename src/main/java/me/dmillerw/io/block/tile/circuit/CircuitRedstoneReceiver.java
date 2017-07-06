package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;

/**
 * @author dmillerw
 */
public class CircuitRedstoneReceiver extends TileToolContainer {

    public static final String KEY_REDSTONE_LEVEL = "RedstoneLevel";

    @Override
    public void initialize() {
        setName("Redstone Emitter");

        registerOutput(KEY_REDSTONE_LEVEL, DataType.NUMBER);
    }

    public void updateLevel() {
        updateOutput(KEY_REDSTONE_LEVEL, world.isBlockPowered(pos) ? 1 : 0);
    }
}
