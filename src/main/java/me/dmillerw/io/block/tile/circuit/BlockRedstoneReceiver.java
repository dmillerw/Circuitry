package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.BlockToolContainer;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockRedstoneReceiver extends BlockToolContainer implements ITileEntityProvider {

    public BlockRedstoneReceiver() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":redstone_receiver");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (!worldIn.isRemote) {
            CircuitRedstoneReceiver tile = (CircuitRedstoneReceiver) worldIn.getTileEntity(pos);
            tile.updateLevel();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CircuitRedstoneReceiver();
    }
}
