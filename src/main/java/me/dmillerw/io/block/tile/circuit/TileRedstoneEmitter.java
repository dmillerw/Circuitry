package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;

/**
 * @author dmillerw
 */
public class TileRedstoneEmitter extends TileToolContainer {

    public static final String KEY_REDSTONE_LEVEL = "RedstoneLevel";

    @Override
    public void initialize() {
        setName("redstone_emitter");

        registerOutput(DataType.NUMBER, KEY_REDSTONE_LEVEL);
    }

    public void updateLevel() {
        updateOutput(KEY_REDSTONE_LEVEL, world.isBlockPowered(pos) ? 1 : 0);
    }
}
