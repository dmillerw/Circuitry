package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateSign extends BaseGate {

    public GateSign() {
        super("sign", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "A");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        double a = parentTile.getInput("A").getDouble();

        double out = 0;
        if (a > 0) out = 1;
        else if (a < 0) out = -1;

        parentTile.updateOutput("Out", out);
    }
}
