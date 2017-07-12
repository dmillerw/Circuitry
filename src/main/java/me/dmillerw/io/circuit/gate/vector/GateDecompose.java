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
        parentTile.registerInput("In", DataType.VECTOR);

        parentTile.registerOutput("X", DataType.NUMBER);
        parentTile.registerOutput("Y", DataType.NUMBER);
        parentTile.registerOutput("Z", DataType.NUMBER);
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        Vec3d in = parentTile.getInput("In").value.getVector();

        parentTile.updateOutput("X", in.x);
        parentTile.updateOutput("Y", in.y);
        parentTile.updateOutput("Z", in.z);
    }
}
