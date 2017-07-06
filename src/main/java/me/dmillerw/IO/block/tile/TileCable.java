package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.BlockCable;
import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.block.property.ConnectionType;
import me.dmillerw.io.block.tile.core.TileCore;
import me.dmillerw.io.circuit.grid.ConnectivityGrid;
import me.dmillerw.io.api.IGridMember;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.Arrays;

/**
 * @author dmillerw
 */
public class TileCable extends TileCore implements ITickable, IGridMember {

    private ConnectivityGrid grid;
    private ConnectionType[] connectionMap = new ConnectionType[6];

    public TileCable() {
        Arrays.fill(connectionMap, ConnectionType.NONE);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (getGrid() == null)
                setGrid(ConnectivityGrid.createOrJoin(this));
        }
    }

    /* CORE */
    @Override
    public void writeToDisk(NBTTagCompound tag) {
        for (int i = 0; i < connectionMap.length; i++) {
            tag.setInteger("connectionMap#" + i, connectionMap[i].ordinal());
        }
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        for (int i = 0; i < connectionMap.length; i++) {
            connectionMap[i] = ConnectionType.getValues()[compound.getInteger("connectionMap#" + i)];
        }
    }

    public void updateState() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            final IBlockState state = world.getBlockState(pos.offset(facing));

            if (state.getBlock() == ModBlocks.cable) {
                connectionMap[facing.ordinal()] = ConnectionType.CABLE;
            } else if (world.getTileEntity(pos.offset(facing)) != null) {
                connectionMap[facing.ordinal()] = ConnectionType.BLOCK;
            } else {
                connectionMap[facing.ordinal()] = ConnectionType.NONE;
            }
        }

        markDirtyAndNotify();
    }

    public ConnectionType getConnectionType(EnumFacing facing) {
        return connectionMap[facing.ordinal()];
    }

    public IBlockState getRenderBlockstate(IBlockState state) {
        IExtendedBlockState eState = (IExtendedBlockState) state;

        for (int i = 0; i < connectionMap.length; i++) {
            eState = eState.withProperty(BlockCable.PROPERTIES[i], connectionMap[i]);
        }

        return eState;
    }

    public boolean isConnectedTo(EnumFacing direction) {
        return getConnectionType(direction) != ConnectionType.NONE;
    }

    /* GRID MEMBER */

    @Override
    public ConnectivityGrid.MemberType getMemberType() {
        return ConnectivityGrid.MemberType.CABLE;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPosition() {
        return pos;
    }

    @Override
    public void setGrid(ConnectivityGrid grid) {
        this.grid = grid;
    }

    @Override
    public ConnectivityGrid getGrid() {
        return this.grid;
    }

    @Override
    public boolean canConnectTo(IGridMember otherMember) {
        return true;
    }

    @Override
    public void onGridUpdate(ConnectivityGrid.UpdateType updateType) {

    }
}