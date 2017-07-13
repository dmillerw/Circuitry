package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Value;

/**
 * @author dmillerw
 */
public class TileRedstoneReceiver extends TileToolContainer {

    public static final String KEY_REDSTONE_LEVEL = "RedstoneLevel";

    public int redstoneLevel = 0;

    @Override
    public void initialize() {
        setName("redstone_receiver");

        registerInput(DataType.NUMBER, KEY_REDSTONE_LEVEL);
    }

    @Override
    public void onInputChange(String port, Value value) {
        if (port.equals(KEY_REDSTONE_LEVEL)) {
            redstoneLevel = value.getNumber().intValue();

            markDirtyAndNotify();
            notifyNeighbors();
        }
    }
}
