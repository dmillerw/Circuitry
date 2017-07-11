package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;

/**
 * @author dmillerw
 */
public class TileScreen extends TileToolContainer {

    @Override
    public void initialize() {
        registerInput("Value", DataType.NUMBER);
    }
}
