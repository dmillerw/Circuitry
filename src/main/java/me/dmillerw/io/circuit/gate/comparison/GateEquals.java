package me.dmillerw.io.circuit.gate.comparison;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateEquals extends BaseGate {

    public GateEquals() {
        super("equals", Category.COMPARISON);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput("A", DataType.NUMBER);
        parentTile.registerInput("B", DataType.NUMBER);

        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double a = parentTile.getInput("A").getDouble();
        double b = parentTile.getInput("B").getDouble();

        parentTile.updateOutput("Out", a == b ? 1 : 0);
    }
}
