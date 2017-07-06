package me.dmillerw.inspection.block.tile.core;

import me.dmillerw.inspection.block.core.BlockGridMember;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockToolContainer extends BlockGridMember {

    public BlockToolContainer(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlockToolContainer(Material materialIn) {
        super(materialIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state)) {
            TileToolContainer tile = (TileToolContainer) worldIn.getTileEntity(pos);
            if (tile != null) tile.resetOutputs();
        }

        super.breakBlock(worldIn, pos, state);
    }
}
