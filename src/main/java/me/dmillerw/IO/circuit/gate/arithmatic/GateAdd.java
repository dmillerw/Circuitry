package me.dmillerw.io.circuit.gate.arithmatic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

import java.util.Arrays;

/**
 * @author dmillerw
 */
public class GateAdd extends BaseGate {

    private static final String[] INPUTS = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };

    public GateAdd() {
        super("add", Category.ARITHMATIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        Arrays.stream(INPUTS).forEach(k -> parentTile.registerInput(k, DataType.NUMBER));
        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        final double[] bucket = {0};
        Arrays.stream(INPUTS).forEach(k -> {
            bucket[0] += ((Number)parentTile.getInput(k).value).doubleValue();
        });

        parentTile.updateOutput("Out", bucket[0]);
    }
}
