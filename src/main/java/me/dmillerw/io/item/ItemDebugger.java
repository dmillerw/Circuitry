package me.dmillerw.io.item;

import me.dmillerw.io.api.IConfigurable;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import me.dmillerw.io.network.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class ItemDebugger extends Item {

    public ItemDebugger() {
        super();

        setMaxStackSize(1);
        setMaxDamage(0);

        setUnlocalizedName(ModInfo.MOD_ID + ":debugger");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof IConfigurable) {
            GuiHandler.Gui.CONFIG.openGui(player, pos);
        }

        return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
    }
}
