package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Value;

/**
 * @author dmillerw
 */
public class CircuitConstantValue extends TileToolContainer {

    @Override
    public void initialize() {
        setName("Constant Value");

        registerOutput(DataType.NUMBER, "Out");

        updateOutput("Out", 15);
    }

    @Override
    public void onInputChange(String port, Value value) {
        // NO-OP
    }
}
