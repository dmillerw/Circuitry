package me.dmillerw.io.circuit.gate.arithmatic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
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
        int increment = parentTile.getInput("Increment").value.getNumber().intValue();
        if (increment > 0 && increment != parentTile.getInput("Increment").previousValue.getNumber().intValue()) {
            increment = 1;
        } else {
            increment = 0;
        }

        int decrement = parentTile.getInput("Decrement").value.getNumber().intValue();
        if (decrement > 0 && decrement != parentTile.getInput("Decrement").previousValue.getNumber().intValue()) {
            decrement = 1;
        } else {
            decrement = 0;
        }

        int reset = parentTile.getInput("Reset").value.getNumber().intValue();
        if (reset > 0 && reset != parentTile.getInput("Reset").previousValue.getNumber().intValue()) {
            reset = 1;
        } else {
            reset = 0;
        }

        int mod = parentTile.getInput("A").value.getNumber().intValue();
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
