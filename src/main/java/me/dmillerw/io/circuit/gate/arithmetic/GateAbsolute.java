package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateAbsolute extends BaseGate {

    public GateAbsolute() {
        super("absolute", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "A");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        parentTile.updateOutput("Out", Math.abs(parentTile.getInput("A").getDouble()));
    }
}
