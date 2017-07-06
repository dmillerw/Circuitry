package me.dmillerw.io.circuit.grid;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import me.dmillerw.io.api.IGridMember;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author dmillerw
 */
public class GridRegistry {

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        GridRegistry.INSTANCE.activeGrids.forEachValue((l -> {
            Iterator<ConnectivityGrid> iterator = l.iterator();
            while (iterator.hasNext()) {
                ConnectivityGrid grid = iterator.next();
                grid.tick();

                if (grid.getMembers().isEmpty()) iterator.remove();
            }

            return true;
        }));
    }

    public static void markDirty(ConnectivityGrid grid) {
        Iterator<IGridMember> iterator = grid.getMembers().values().iterator();
        while (iterator.hasNext()) {
            iterator.next().onGridUpdate(ConnectivityGrid.UpdateType.SELF);
        }
    }

    public static final GridRegistry INSTANCE = new GridRegistry();

    private TIntSet initialized = new TIntHashSet();
    private final TIntObjectMap<List<ConnectivityGrid>> activeGrids = new TIntObjectHashMap<>();

    public ConnectivityGrid getGrid(int dimensionId, UUID uuid) {
        if (activeGrids.containsKey(dimensionId)) {
            for (ConnectivityGrid grid : activeGrids.get(dimensionId)) {
                if (grid.uuid == uuid)
                    return grid;
            }
        }

        return null;
    }

    public ConnectivityGrid getNewGrid(boolean register) {
        ConnectivityGrid grid = new ConnectivityGrid();
        grid.setUuid(UUID.randomUUID());

        if (register)
            add(grid);

        return grid;
    }

    public void add(ConnectivityGrid grid) {
        if (!activeGrids.containsKey(grid.getDimensionId()))
            activeGrids.put(grid.getDimensionId(), new ArrayList<>());

        List<ConnectivityGrid> grids = activeGrids.get(grid.getDimensionId());
        grids.add(grid);
    }

    public void remove(ConnectivityGrid grid) {
        if (activeGrids.containsKey(grid.getDimensionId())) {
            List<ConnectivityGrid> grids = activeGrids.get(grid.getDimensionId());
            grids.remove(grid);
        }
    }
}
