package me.dmillerw.inspection.circuit.gate.util;

import me.dmillerw.inspection.block.tile.TileGateContainer;
import me.dmillerw.inspection.circuit.data.DataType;
import me.dmillerw.inspection.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateConstantValue extends BaseGate {

    public GateConstantValue() {
        super("constant_value", Category.ARITHMATIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        super.initialize(parentTile);

        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        super.calculateOutput(parentTile);

        parentTile.updateOutput("Out", 1);
    }
}
