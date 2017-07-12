package me.dmillerw.io.item;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import me.dmillerw.io.network.GuiHandler;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.server.SRequestOutputGui;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author dmillerw
 */
public class ItemLinkingTool extends Item {

    public static boolean hasTargetSet(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) return false;
        return tag.hasKey("TargetPosition") && tag.hasKey("TargetType");
    }

    public static BlockPos getTargetPosition(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) return null;
        return tag.hasKey("TargetPosition") ? BlockPos.fromLong(tag.getLong("TargetPosition")) : null;
    }

    public static String getTargetPort(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) return null;
        return tag.hasKey("TargetPort") ? tag.getString("TargetPort") : null;
    }

    public static DataType getTargetDataType(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) return null;
        return tag.hasKey("TargetType") ? DataType.values()[tag.getInteger("TargetType")] : null;
    }

    public static void setTarget(ItemStack itemStack, BlockPos blockPos, String port, DataType dataType) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        tag.setLong("TargetPosition", blockPos.toLong());
        tag.setString("TargetPort", port);
        tag.setInteger("TargetType", dataType.ordinal());
        itemStack.setTagCompound(tag);
    }

    public static void reset(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        tag.removeTag("TargetPosition");
        tag.removeTag("TargetPort");
        tag.removeTag("TargetType");
        itemStack.setTagCompound(tag);
    }

    public ItemLinkingTool() {
        super();

        setMaxStackSize(1);
        setMaxDamage(0);

        setUnlocalizedName(ModInfo.MOD_ID + ":linking_tool");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ItemLinkingTool.hasTargetSet(stack)) {
            tooltip.add("Position: " + getTargetPosition(stack));
            tooltip.add("Port: " + getTargetPort(stack));
            tooltip.add("Type: " + getTargetDataType(stack));
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (hand != EnumHand.MAIN_HAND)
            return EnumActionResult.PASS;

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileToolContainer) {
            ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
            if (ItemLinkingTool.hasTargetSet(held)) {
                if (ItemLinkingTool.getTargetPosition(held).equals(pos)) {
                    player.sendMessage(new TextComponentString("Linking failed, cannot link to self"));
                } else {
                    SRequestOutputGui packet = new SRequestOutputGui();
                    packet.target = pos;

                    PacketHandler.INSTANCE.sendToServer(packet);
                }
            } else {
                GuiHandler.Gui.SELECT_INPUT.openGui(player, pos);
            }
        }

        return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
    }
}
