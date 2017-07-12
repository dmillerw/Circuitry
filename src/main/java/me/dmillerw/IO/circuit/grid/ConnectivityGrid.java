package me.dmillerw.io.circuit.grid;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.dmillerw.io.api.IGridMember;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.data.Value;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author dmillerw
 */
public class ConnectivityGrid {

    public static enum MemberType { CABLE, NODE }
    public static enum UpdateType { SELF, NEIGHBOR }

    public static ConnectivityGrid getGrid(IGridMember gridMember, UUID uuid) {
        ConnectivityGrid grid = GridRegistry.INSTANCE.getGrid(gridMember.getWorld().provider.getDimension(), uuid);
        if (grid == null) grid = createOrJoin(gridMember);
        return grid;
    }

    public static ConnectivityGrid createOrJoin(IGridMember gridMember) {
        Set<IGridMember> neighbors = getNeighbors(gridMember, false);
        Set<ConnectivityGrid> otherGrids = Sets.newHashSet();
        ConnectivityGrid newGrid = null;
        boolean hasJoinedGrid = false;

        if (neighbors.size() > 0) {
            neighbors.stream()
                    .filter(t -> t.getGrid() != null)
                    .forEach(t -> otherGrids.add(t.getGrid()));

            if (otherGrids.size() > 0) {
                ConnectivityGrid grid = null;
                for (ConnectivityGrid g : otherGrids) {
                    if (grid != null) {
                        FMLLog.info("Found existing grid: Merging " + g.uuid + " into " + grid.uuid);
                        grid.joinGrid(g);
                    } else {
                        FMLLog.info("Found existing grid: Attaching to " + g.uuid);
                        grid = g;
                    }
                }

                if (grid != null) {
                    int old = grid.members.size();
                    grid.addMember(gridMember);
                    FMLLog.info("Joined grid: Used to have " + old + " members, now has " + grid.members.size());
                }

                hasJoinedGrid = true;
                newGrid = grid;
            }
        }

        if (!hasJoinedGrid) {
            ConnectivityGrid grid = GridRegistry.INSTANCE.getNewGrid(true);
            grid.addMember(gridMember);
            newGrid = grid;

            FMLLog.info("Couldn't find an existing grid, creating " + grid.uuid);
        }

        return newGrid;
    }

    public static Set<IGridMember> getNeighbors(IGridMember gridMember, boolean cablesOnly) {
        Set<IGridMember> neighbors = Sets.newHashSet();
        if (cablesOnly && gridMember.getMemberType() != MemberType.CABLE)
            return neighbors;

        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity tile = gridMember.getWorld().getTileEntity(gridMember.getPosition().offset(facing));
            if (tile == null || !(tile instanceof IGridMember))
                continue;

            if (gridMember.canConnectTo((IGridMember) tile, facing))
                neighbors.add((IGridMember) tile);
        }

        return neighbors;
    }

    public UUID uuid;
    public final void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private Map<BlockPos, IGridMember> members = Maps.newHashMap();

    private int dimensionId;
    private WeakReference<World> world;

    private boolean shouldRefresh = false;

    public Map<BlockPos, IGridMember> getMembers() {
        return this.members;
    }

    public void markDirty() {
        shouldRefresh = true;
    }

    public boolean tick() {
        if (members.isEmpty())
            return false;

        if (shouldRefresh) {
            refreshGrid();
            shouldRefresh = false;
        }

        return true;
    }

    public void refreshGrid() {
        if (members.isEmpty()) {
            GridRegistry.INSTANCE.remove(this);
            return;
        }

        Map<BlockPos, IGridMember> temp = (Map<BlockPos, IGridMember>) ((HashMap) members).clone();
        for (BlockPos pos : temp.keySet()) {
            TileEntity tile = getWorld().getTileEntity(pos);
            if (tile == null || !(tile instanceof IGridMember)) {
                this.members.remove(pos);
            } else {
                IGridMember gridMember = (IGridMember) tile;
                if (gridMember.getGrid() != this) {
                    this.members.remove(pos);
                }
            }
        }

        GridRegistry.markDirty(this);
    }

    public void addMember(IGridMember member) {
        if (member != null) {
            if (members.isEmpty()) {
                dimensionId = member.getWorld().provider.getDimension();
                world = new WeakReference<World>(member.getWorld());
            } else {
                if (member.getWorld().provider.getDimension() != dimensionId) {
                    throw new RuntimeException("Cannot add member from a different dimension to a grid!");
                }
            }

            if (!contains(member))
                members.put(member.getPosition(), member);

            member.setGrid(this);

            markDirty();
        }
    }

    public boolean contains(IGridMember gridMember) {
        return members.containsKey(gridMember.getPosition());
    }

    public void joinGrid(ConnectivityGrid grid) {
        grid.members.values().forEach(this::addMember);
        GridRegistry.INSTANCE.remove(grid);
    }

    public void splitGridAtPoint(IGridMember splitPoint) {
        FMLLog.info("Splitting " + uuid);

        Set<ConnectivityGrid> newGrids = Sets.newHashSet();
        for (IGridMember neighbor : getNeighbors(splitPoint, false)) {
            boolean belongsToGrid = false;
            if (newGrids.size() > 0) {
                for (ConnectivityGrid grid : newGrids) {
                    if (grid.contains(neighbor)) {
                        FMLLog.info("Neighbor is joining " + uuid);
                        belongsToGrid = true;
                        break;
                    }
                }
            }

            if (!belongsToGrid) {
                ConnectivityGrid grid = GridRegistry.INSTANCE.getNewGrid(false);
                grid.buildUpFromMember(neighbor, grid, splitPoint);

                FMLLog.info("Built new grid: " + grid.uuid + " with " + grid.members.size() + " members");

                newGrids.add(grid);
            }
        }

        if (newGrids.size() > 0) {
            FMLLog.info("Created " + newGrids.size() + " new grid(s)");
            for (ConnectivityGrid grid : newGrids) {
                GridRegistry.INSTANCE.add(grid);
                grid.markDirty();
            }
        }

        markDirty();
    }

    public void onNeighborChanged() {
        getMembers().values().forEach(m -> m.onGridUpdate(ConnectivityGrid.UpdateType.NEIGHBOR));
    }

    public void buildUpFromMember(IGridMember gridMember, ConnectivityGrid target, IGridMember ignoredMember) {
        Set<IGridMember> checkedMembers = Sets.newHashSet();
        Set<IGridMember> members = target.crawlGrid(gridMember, checkedMembers, ignoredMember, false);

        boolean gridIsMe = target == this;

        ConnectivityGrid grid = gridIsMe ? this : GridRegistry.INSTANCE.getNewGrid(false);
        for (IGridMember member : members) {
            if (gridMember == ignoredMember) continue;
            if (!contains(member)) target.addMember(member);
        }

        grid.addMember(gridMember);
    }

    public Set<IGridMember> crawlGrid(IGridMember origin, Set<IGridMember> checkedMembers, IGridMember ignoredMember, boolean cablesOnly) {
        if (checkedMembers == null) checkedMembers = Sets.newHashSet();
        if (!checkedMembers.contains(origin)) checkedMembers.add(origin);

        Set<IGridMember> neighbors = ConnectivityGrid.getNeighbors(origin, cablesOnly);
        for (IGridMember neighbor : neighbors) {
            if (neighbor == ignoredMember) continue;
            if (!checkedMembers.contains(neighbor)) {
                checkedMembers.add(neighbor);

                crawlGrid(neighbor, checkedMembers, ignoredMember, cablesOnly);
            }
        }

        return checkedMembers;
    }

    public void propagateOutputUpdate(TileToolContainer origin, String port, Value value) {
        members.values().forEach(m -> {
            if (m.getMemberType() == MemberType.NODE) {
                if (m instanceof TileToolContainer) {
                    Port listener = ((TileToolContainer) m).getListeningPort(origin, port);
                    if (listener != null) {
                        ((TileToolContainer) m).updateInput(listener.name, value);
                    }
                }
            }
        });
    }

    public final int getDimensionId() {
        return dimensionId;
    }

    public final World getWorld() {
        if (world == null) return null;
        return world.get();
    }
}
