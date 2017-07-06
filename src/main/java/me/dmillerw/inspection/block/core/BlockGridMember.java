package me.dmillerw.inspection.block.core;

import me.dmillerw.inspection.api.IGridMember;
import me.dmillerw.inspection.circuit.grid.ConnectivityGrid;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockGridMember extends Block {

    public BlockGridMember(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlockGridMember(Material materialIn) {
        super(materialIn);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote && hasTileEntity(state)) {
            IGridMember gridMember = (IGridMember) worldIn.getTileEntity(pos);
            if (gridMember != null) {
                gridMember.setGrid(ConnectivityGrid.createOrJoin(gridMember));
                System.out.println("");
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            IGridMember gridMember = (IGridMember) worldIn.getTileEntity(pos);
            if (gridMember != null) {
                ConnectivityGrid grid = gridMember.getGrid();
                if (grid != null) grid.onNeighborChanged();
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IGridMember gridMember = (IGridMember) worldIn.getTileEntity(pos);
            if (gridMember != null) {
                ConnectivityGrid grid = gridMember.getGrid();
                if (grid != null) grid.splitGridAtPoint(gridMember);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
}
