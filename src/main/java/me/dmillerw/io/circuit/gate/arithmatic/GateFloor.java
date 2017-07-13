package me.dmillerw.io.circuit.gate.arithmatic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateFloor extends BaseGate {

    public GateFloor() {
        super("floor", Category.ARITHMATIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput("In", DataType.NUMBER);
        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        parentTile.updateOutput("Out", Math.floor(parentTile.getInput("In").getDouble()));
    }
}
