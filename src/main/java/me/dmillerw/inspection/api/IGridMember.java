package me.dmillerw.inspection.api;

import me.dmillerw.inspection.circuit.grid.ConnectivityGrid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public interface IGridMember {

    public ConnectivityGrid.MemberType getMemberType();

    public World getWorld();
    public BlockPos getPosition();

    public void setGrid(ConnectivityGrid grid);
    public ConnectivityGrid getGrid();

    public boolean canConnectTo(IGridMember otherMember);

    public void onGridUpdate(ConnectivityGrid.UpdateType updateType);
}
