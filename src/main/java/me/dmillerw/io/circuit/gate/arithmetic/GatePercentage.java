package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GatePercentage extends BaseGate {

    public GatePercentage() {
        super("percentage", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "Value", "Max");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double value = parentTile.getInput("Value").getDouble();
        double max = parentTile.getInput("Max").getDouble();

        if (max == 0) parentTile.updateOutput("Out", 0);
        else parentTile.updateOutput("Out", value / max * 100);
    }
}
