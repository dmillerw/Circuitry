package me.dmillerw.inspection.block.tile.circuit;

import me.dmillerw.inspection.block.tile.core.BlockToolContainer;
import me.dmillerw.inspection.lib.ModInfo;
import me.dmillerw.inspection.lib.ModTab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockConstantValue extends BlockToolContainer implements ITileEntityProvider {

    public BlockConstantValue() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":constant_value");

        setCreativeTab(ModTab.TAB);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CircuitConstantValue();
    }
}
