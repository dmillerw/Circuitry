package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;

/**
 * @author dmillerw
 */
public class CircuitConstantValue extends TileToolContainer {

    @Override
    public void initialize() {
        setName("Constant Value");

        registerOutput("Out", DataType.NUMBER);

        updateOutput("Out", 15);
    }

    @Override
    public void onInputChange(String port, Object value) {
        // NO-OP
    }
}
