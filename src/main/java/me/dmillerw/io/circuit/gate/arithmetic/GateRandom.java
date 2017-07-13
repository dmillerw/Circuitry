package me.dmillerw.io.circuit.gate.arithmetic;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author dmillerw
 */
public class GateRandom extends BaseGate {

    public GateRandom() {
        super("random", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.NUMBER, "Min", "Max");

        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void tick(TileGateContainer tileGateContainer, int lifespan) {
        update(tileGateContainer);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        update(parentTile);
    }

    private void update(TileGateContainer parentTile) {
        int min = parentTile.getInput("Min").getInt();
        int max = parentTile.getInput("Max").getInt();

        parentTile.updateOutput("Out", ThreadLocalRandom.current().nextInt(min, max + 1));
    }
}
