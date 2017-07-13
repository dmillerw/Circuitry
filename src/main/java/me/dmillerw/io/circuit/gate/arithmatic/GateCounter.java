package me.dmillerw.io.circuit.gate.arithmatic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateCounter extends BaseGate {

    public GateCounter() {
        super("counter", Category.ARITHMATIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput("A", DataType.NUMBER);
        parentTile.registerInput("Increment", DataType.NUMBER);
        parentTile.registerInput("Decrement", DataType.NUMBER);
        parentTile.registerInput("Reset", DataType.NUMBER);

        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        Port portInc = parentTile.getInput("Increment");
        Port portDec = parentTile.getInput("Decrement");
        Port portRes = parentTile.getInput("Reset");

        int increment = portInc.getInt();
        if (increment > 0 && portInc.hasValueChanged()) {
            increment = 1;
        } else {
            increment = 0;
        }

        int decrement = portDec.getInt();
        if (decrement > 0 && portDec.hasValueChanged()) {
            decrement = 1;
        } else {
            decrement = 0;
        }

        int reset = portRes.getInt();
        if (reset > 0 && portRes.hasValueChanged()) {
            reset = 1;
        } else {
            reset = 0;
        }

        int mod = parentTile.getInput("A").getInt();
        int value = parentTile.getGateState().getInteger("Value");

        if (increment > 0) {
            value += mod;
        }

        if (decrement > 0) {
            value -= mod;
        }

        if (reset > 0) {
            value = 0;
        }

        parentTile.getGateState().setInteger("Value", value);

        parentTile.updateOutput("Out", value);
    }
}
