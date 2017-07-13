package me.dmillerw.io.circuit.gate.vector;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;
import net.minecraft.util.math.Vec3d;

/**
 * @author dmillerw
 */
public class GateDecompose extends BaseGate {

    public GateDecompose() {
        super("vector_decompose", Category.VECTOR);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput(DataType.VECTOR, "In");

        parentTile.registerOutput(DataType.NUMBER, "X");
        parentTile.registerOutput(DataType.NUMBER, "Y");
        parentTile.registerOutput(DataType.NUMBER, "Z");
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        Vec3d in = parentTile.getInput("In").getVector();

        parentTile.updateOutput("X", in.x);
        parentTile.updateOutput("Y", in.y);
        parentTile.updateOutput("Z", in.z);
    }
}
