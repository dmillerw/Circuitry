package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateModulo extends BaseGate {

    public GateModulo() {
        super("modulo", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "A", "B");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double a = parentTile.getInput("A").getDouble();
        double b = parentTile.getInput("B").getDouble();

        if (b == 0) parentTile.updateOutput("Out", 0);
        else parentTile.updateOutput("Out", a % b);
    }
}
