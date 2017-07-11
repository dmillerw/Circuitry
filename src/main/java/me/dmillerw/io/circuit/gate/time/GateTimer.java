package me.dmillerw.io.circuit.gate.time;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

/**
 * @author dmillerw
 */
public class GateTimer extends BaseGate {

    public GateTimer() {
        super("timer", Category.TIME);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput("Run", DataType.NUMBER);
        parentTile.registerInput("Reset", DataType.NUMBER);
        parentTile.registerOutput("Out", DataType.NUMBER);
    }

    @Override
    public void tick(TileGateContainer parentTile, int lifespan) {
        if (parentTile.getInput("Run").value.getNumber().intValue() > 0) {
            if (lifespan % 20 == 0) {
                int timer = parentTile.getGateState().getInteger("Timer");
                timer++;

                parentTile.updateOutput("Out", timer);

                parentTile.getGateState().setInteger("Timer", timer);
            }
        }
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        if (parentTile.getInput("Reset").value.getNumber().intValue() > 0) {
            parentTile.getGateState().setInteger("Timer", 0);
            parentTile.updateOutput("Out", 0);
        }
    }
}
