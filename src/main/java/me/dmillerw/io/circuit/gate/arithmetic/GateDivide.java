package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateDivide extends BaseGate {

    public GateDivide() {
        super("divide", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "A", "B");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double out = 0;
        try {
            out = parentTile.getInput("A").getDouble() / parentTile.getInput("B").getDouble();
        } catch (Exception ignore) {
        }

        parentTile.updateOutput("Out", out);
    }
}
