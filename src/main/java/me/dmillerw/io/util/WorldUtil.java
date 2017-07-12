package me.dmillerw.io.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class WorldUtil {

    public static Entity getEntity(World world, int entityId) {
        return world.getEntityByID(entityId);
    }
}
