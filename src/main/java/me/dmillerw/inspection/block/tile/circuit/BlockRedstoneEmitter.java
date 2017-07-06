package me.dmillerw.inspection.block.tile.circuit;

import me.dmillerw.inspection.block.tile.core.BlockToolContainer;
import me.dmillerw.inspection.lib.ModInfo;
import me.dmillerw.inspection.lib.ModTab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockRedstoneEmitter extends BlockToolContainer implements ITileEntityProvider {

    public BlockRedstoneEmitter() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":redstone_emitter");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        CircuitRedstoneEmitter circuit = (CircuitRedstoneEmitter) blockAccess.getTileEntity(pos);
        return MathHelper.clamp(circuit.redstoneLevel, 0, 15);
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        CircuitRedstoneEmitter circuit = (CircuitRedstoneEmitter) blockAccess.getTileEntity(pos);
        return MathHelper.clamp(circuit.redstoneLevel, 0, 15);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CircuitRedstoneEmitter();
    }
}
