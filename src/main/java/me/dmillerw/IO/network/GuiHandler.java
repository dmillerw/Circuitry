package me.dmillerw.io.network;

import me.dmillerw.io.IO;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.client.gui.GuiConfigurator;
import me.dmillerw.io.client.gui.GuiDebugger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class GuiHandler implements IGuiHandler {

    public static enum Gui {

        CONFIGURE_CONNECTION,
        DEBUGGER;

        public void openGui(EntityPlayer player, BlockPos pos) {
            player.openGui(IO.INSTANCE, ordinal(), player.world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (Gui.values()[id]) {
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (Gui.values()[id]) {
            case CONFIGURE_CONNECTION: return new GuiConfigurator((TileToolContainer) world.getTileEntity(new BlockPos(x, y, z)));
            case DEBUGGER: return new GuiDebugger((TileToolContainer) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }
}
