package me.dmillerw.io.block.item;

import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.circuit.gate.GateRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author dmillerw
 */
public class ItemGateContainer extends ItemBlock {

    private static String getGate(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null)
            return "";

        NBTTagCompound tag = itemStack.getTagCompound();

        return tag.getString("Gate");
    }

    public ItemGateContainer(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() == null)
            return;

        BaseGate baseGate = GateRegistry.INSTANCE.getGate(getGate(stack));
        tooltip.add("Category: " + baseGate.getCategory().toString());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + getGate(stack);
    }
}
