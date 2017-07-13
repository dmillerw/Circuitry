package me.dmillerw.io.circuit.gate.entity;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.EntityId;
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
        parentTile.registerInput(DataType.ENTITY, "Entity");
        parentTile.registerOutput(DataType.VECTOR, "Position");
    }

    @Override
    public void tick(TileGateContainer tileGateContainer, int lifespan) {
        EntityId entityId = tileGateContainer.getInput("Entity").getEntity();
        Entity entity = tileGateContainer.getWorld().getEntityByID(entityId.getEntityId());

        Vec3d oldPos = tileGateContainer.getOutput("Position").getVector();
        Vec3d newPos = Vec3d.ZERO;
        if (entity != null)
            newPos = entity.getPositionVector();

        if (!oldPos.equals(newPos)) {
            tileGateContainer.updateOutput("Position", newPos);
        }
    }
}
