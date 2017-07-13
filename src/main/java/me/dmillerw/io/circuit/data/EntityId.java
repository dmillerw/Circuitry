package me.dmillerw.io.circuit.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class EntityId {

    public static final EntityId NULL = new EntityId(0, new ResourceLocation(""));

    private final int entityId;
    private final ResourceLocation entity;

    public EntityId(Entity entity) {
        this.entityId = entity.getEntityId();
        this.entity = EntityList.getKey(entity.getClass());
    }

    public EntityId(int entityId, ResourceLocation entity) {
        this.entityId = entityId;
        this.entity = entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public ResourceLocation getEntity() {
        return entity;
    }
}
