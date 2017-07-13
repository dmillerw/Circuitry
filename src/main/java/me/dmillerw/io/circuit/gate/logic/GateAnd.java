package me.dmillerw.io.circuit.gate.logic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.gate.BaseGate;

import java.util.Arrays;

/**
 * @author dmillerw
 */
public class GateAnd extends BaseGate {

    private static final String[] INPUTS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

    public GateAnd() {
        super("and", Category.LOGIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        Arrays.stream(INPUTS).forEach(k -> parentTile.registerInput(k, DataType.NUMBER));
        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        final double[] first = {Double.MIN_VALUE};
        Arrays.stream(INPUTS).forEach(k -> {
            Port port = parentTile.getInput(k);
            if (!port.getValue().isNull()) {
                if (first[0] == Integer.MIN_VALUE) {
                    first[0] = port.getDouble();
                } else {
                    if (first[0] != port.getDouble()) {
                        parentTile.updateOutput("Out", 0);
                        return;
                    }
                }
            }
        });

        parentTile.updateOutput("Out", 1);
    }
}
