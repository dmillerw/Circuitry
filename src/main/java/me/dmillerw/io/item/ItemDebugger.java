package me.dmillerw.io.item;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import me.dmillerw.io.network.GuiHandler;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.client.CUpdatePorts;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
        if (tile != null && tile instanceof TileToolContainer) {
            if (!world.isRemote) {
                CUpdatePorts packet = new CUpdatePorts();
                packet.target = pos;
                packet.inputs = ((TileToolContainer) tile).inputs.values().toArray(new Port[0]);
                packet.outputs = ((TileToolContainer) tile).outputs.values().toArray(new Port[0]);

                PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
            }

            GuiHandler.Gui.DEBUGGER.openGui(player, pos);
        }

        return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
    }
}
