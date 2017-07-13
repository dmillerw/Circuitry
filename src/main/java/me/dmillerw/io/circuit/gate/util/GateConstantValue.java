package me.dmillerw.io.circuit.gate.util;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateConstantValue extends BaseGate {

    public GateConstantValue() {
        super("constant_value", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        parentTile.updateOutput("Out", 60);
    }
}
