package me.dmillerw.io.circuit.gate.time;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
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
        parentTile.registerInput(DataType.NUMBER, "Run");
        parentTile.registerInput(DataType.NUMBER, "Reset");
        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void tick(TileGateContainer parentTile, int lifespan) {
        Port portRun = parentTile.getInput("Run");
        Port portRes = parentTile.getInput("Reset");

        if (portRes.getInt() > 0) {
            return;
        }

        if (portRun.getInt() > 0) {
            if (lifespan % 20 == 0) {
                int timer = parentTile.getGateState().getInteger("Timer");
                if (portRes.getInt() > 0) {
                    timer = 0;
                } else {
                    timer++;
                }

                parentTile.updateOutput("Out", timer);
                parentTile.getGateState().setInteger("Timer", timer);
            }
        }
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        if (parentTile.getInput("Reset").getInt() > 0) {
            parentTile.getGateState().setInteger("Timer", 0);
            parentTile.updateOutput("Out", 0);
        }
    }
}
