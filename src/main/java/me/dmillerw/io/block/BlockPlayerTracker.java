package me.dmillerw.io.block;

import me.dmillerw.io.block.tile.TilePlayerTracker;
import me.dmillerw.io.block.tile.core.BlockToolContainer;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockPlayerTracker extends BlockToolContainer implements ITileEntityProvider {

    public BlockPlayerTracker() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":player_tracker");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TilePlayerTracker tile = (TilePlayerTracker) worldIn.getTileEntity(pos);
            if (tile != null) tile.setTrackingPlayer(playerIn);
        }

        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePlayerTracker();
    }
}
