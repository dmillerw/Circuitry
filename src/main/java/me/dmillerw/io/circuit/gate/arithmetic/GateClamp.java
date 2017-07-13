package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateClamp extends BaseGate {

    public GateClamp() {
        super("clamp", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "A", "Min", "Max");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double in = parentTile.getInput("A").getDouble();
        double min = parentTile.getInput("Min").getDouble();
        double max = parentTile.getInput("Max").getDouble();

        if (in < min) in = min;
        if (in > max) in = max;

        parentTile.updateOutput("Out", in);
    }
}
