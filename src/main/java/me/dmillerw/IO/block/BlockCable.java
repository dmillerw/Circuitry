package me.dmillerw.io.block;

import me.dmillerw.io.block.core.BlockGridMember;
import me.dmillerw.io.block.property.ConnectionType;
import me.dmillerw.io.block.tile.TileCable;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockCable extends BlockGridMember implements ITileEntityProvider {

    public static final double CABLE_SIZE = 0.4D;
    public static final double CONNECTOR_SIZE = 0.3D;
    public static final double CONNECTOR_DEPTH = 0.2D;

    public static final ConnectionType.Property DOWN = new ConnectionType.Property("down");
    public static final ConnectionType.Property UP = new ConnectionType.Property("up");
    public static final ConnectionType.Property NORTH = new ConnectionType.Property("north");
    public static final ConnectionType.Property SOUTH = new ConnectionType.Property("south");
    public static final ConnectionType.Property WEST = new ConnectionType.Property("west");
    public static final ConnectionType.Property EAST = new ConnectionType.Property("east");

    public static final ConnectionType.Property[] PROPERTIES = new ConnectionType.Property[]{
            DOWN, UP, NORTH, SOUTH, WEST, EAST
    };

    public BlockCable() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":cable");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        TileCable cable = (TileCable) worldIn.getTileEntity(pos);
        if (cable != null) {
            cable.updateState();
        }

        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileCable cable = (TileCable) worldIn.getTileEntity(pos);
        if (cable != null) {
            cable.updateState();
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileCable tile = (TileCable) source.getTileEntity(pos);
        AxisAlignedBB aabb = new AxisAlignedBB(.3, .3, .3, .7, .7, .7);

        if (tile != null) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (tile.isConnectedTo(facing)) {
                    aabb = aabb.addCoord(0.3 * facing.getFrontOffsetX(), 0.3 * facing.getFrontOffsetY(), 0.3 * facing.getFrontOffsetZ());
                }
            }
        }

        return aabb;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return FULL_BLOCK_AABB.expandXyz(-.25);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{
                DOWN, UP, NORTH, SOUTH, WEST, EAST
        });
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileCable cable = (TileCable) world.getTileEntity(pos);
        return cable.getRenderBlockstate(state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCable();
    }
}