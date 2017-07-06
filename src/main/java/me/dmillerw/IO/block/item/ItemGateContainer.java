package me.dmillerw.io.block.item;

import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.circuit.gate.GateRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemGateContainer extends ItemBlock {

    public ItemGateContainer(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.getTagCompound() == null)
            return;

        String gate = stack.getTagCompound().getString("Gate");
        BaseGate baseGate = GateRegistry.INSTANCE.getGate(gate);
        tooltip.add(baseGate.getCategory().toString());
        tooltip.add(baseGate.getKey());
    }
}
