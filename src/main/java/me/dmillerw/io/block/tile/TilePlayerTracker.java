package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author dmillerw
 */
public class TilePlayerTracker extends TileToolContainer {

    @Override
    public void initialize() {
        registerOutput("Player", DataType.ENTITY);
    }

    public void setTrackingPlayer(EntityPlayer player) {
        updateOutput("Player", player.getEntityId());
    }
}
