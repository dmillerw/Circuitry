package me.dmillerw.inspection.block.tile.circuit;

import me.dmillerw.inspection.block.tile.core.TileToolContainer;
import me.dmillerw.inspection.circuit.data.DataType;

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
    public void triggerInputChange(String port, Object value) {
        // NO-OP
    }
}
