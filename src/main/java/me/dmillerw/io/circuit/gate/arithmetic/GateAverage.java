package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.gate.BaseGate;

import java.util.Arrays;

/**
 * @author dmillerw
 */
public class GateAverage extends BaseGate {

    private static final String[] INPUTS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

    public GateAverage() {
        super("average", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, INPUTS);

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        final int[] count = {0};
        final double[] bucket = {0};
        Arrays.stream(INPUTS).forEach(k -> {
            Port port = parentTile.getInput(k);
            if (!port.getValue().isNull()) {
                count[0]++;
                bucket[0] += port.getDouble();
            }
        });

        parentTile.updateOutput("Out", bucket[0] / count[0]);
    }
}
