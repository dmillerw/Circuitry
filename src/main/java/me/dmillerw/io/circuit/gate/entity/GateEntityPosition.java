package me.dmillerw.io.circuit.gate.entity;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author dmillerw
 */
public class GateEntityPosition extends BaseGate {

    public GateEntityPosition() {
        super("position_entity", Category.ENTITY);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerInput("Entity", DataType.ENTITY);
        parentTile.registerOutput("Position", DataType.VECTOR);
    }

    @Override
    public void tick(TileGateContainer tileGateContainer, int lifespan) {
        int entityId = tileGateContainer.getInput("Entity").value.getContainedEntityId();
        Entity entity = tileGateContainer.getWorld().getEntityByID(entityId);

        Vec3d oldPos = tileGateContainer.getOutput("Position").value.getVector();
        Vec3d newPos = Vec3d.ZERO;
        if (entity != null)
            newPos = entity.getPositionVector();

        if (!oldPos.equals(newPos)) {
            tileGateContainer.updateOutput("Position", newPos);
        }
    }
}
