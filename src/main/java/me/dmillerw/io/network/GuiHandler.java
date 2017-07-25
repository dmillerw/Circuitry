package me.dmillerw.io.network;

import me.dmillerw.io.IO;
import me.dmillerw.io.api.IConfigurable;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.client.gui.GuiDebugger;
import me.dmillerw.io.client.gui.GuiSelectInput;
import me.dmillerw.io.client.gui.GuiSelectOutput;
import me.dmillerw.io.client.gui.config.GuiConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class GuiHandler implements IGuiHandler {

    public static enum Gui {

        DEBUGGER,
        CONFIG,
        SELECT_INPUT,
        SELECT_OUTPUT;

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
        final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        final ItemStack mainHeld = player.getHeldItemMainhand();

        switch (Gui.values()[id]) {
            case DEBUGGER:
                return new GuiDebugger((TileToolContainer) tile);
            case CONFIG:
                return new GuiConfig((IConfigurable) tile);
            case SELECT_INPUT:
                return new GuiSelectInput((TileToolContainer) tile);
            case SELECT_OUTPUT:
                return new GuiSelectOutput((TileToolContainer) tile, mainHeld);
            default: return null;
        }
    }
}
