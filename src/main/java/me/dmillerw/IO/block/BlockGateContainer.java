package me.dmillerw.io.block;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.block.tile.core.BlockToolContainer;
import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.circuit.gate.GateRegistry;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockGateContainer extends BlockToolContainer implements ITileEntityProvider {

    public BlockGateContainer() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":gate");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileGateContainer tile = (TileGateContainer) worldIn.getTileEntity(pos);
        if (tile != null) {
            String gate = "add";
            if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Gate"))
                gate = stack.getTagCompound().getString("Gate");

            tile.setGate(gate);
        }
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (BaseGate gate : GateRegistry.INSTANCE.getAllGates()) {
            ItemStack itemStack = new ItemStack(itemIn);

            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("Gate", gate.getKey());
            itemStack.setTagCompound(tagCompound);

            list.add(itemStack);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileGateContainer();
    }
}
