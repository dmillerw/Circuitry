package me.dmillerw.io.block;

import me.dmillerw.io.block.tile.core.BlockToolContainer;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockScreen extends BlockToolContainer implements ITileEntityProvider {

    public BlockScreen() {
        super(Material.IRON);

        setUnlocalizedName(ModInfo.MOD_ID + ":screen");

        setCreativeTab(ModTab.TAB);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
