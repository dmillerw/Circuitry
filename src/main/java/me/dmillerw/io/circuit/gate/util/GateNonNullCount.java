package me.dmillerw.io.circuit.gate.util;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.gate.BaseGate;

import java.util.Arrays;

/**
 * @author dmillerw
 */
public class GateNonNullCount extends BaseGate {

    private static final String[] INPUTS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

    public GateNonNullCount() {
        super("non_null_count", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        Arrays.stream(INPUTS).forEach(k -> parentTile.registerInput(DataType.NUMBER, k));
        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        final int[] bucket = {0};
        Arrays.stream(INPUTS).forEach(k -> {
            Port port = parentTile.getInput(k);
            if (!port.getValue().isNull()) bucket[0]++;
        });

        parentTile.updateOutput("Out", bucket[0]);
    }
}
