package me.dmillerw.io.block.tile.core;

import com.google.common.collect.Maps;
import me.dmillerw.io.api.IGridMember;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.grid.ConnectivityGrid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

/**
 * @author dmillerw
 */
public abstract class TileToolContainer extends TileCore implements ITickable, IGridMember {

    private static final String NBT_KEY_UUID_MOST = "UUID_MOST_SIG";
    private static final String NBT_KEY_UUID_LEAST = "UUID_LEAST_SIG";
    private static final String NBT_KEY_LISTENERS = "LISTENERS";
    private static final String NBT_KEY_CONNECTIONS = "CONNECTIONS";
    private static final String NBT_KEY_OUTPUT = "OUTPUT";
    private static final String NBT_KEY_INPUT = "INPUT";

    private ConnectivityGrid grid;

    private UUID uuid = UUID.randomUUID();

    private String name;

    private boolean initialized = false;

    private Map<String, Port> cachedInputs = Maps.newHashMap();
    private Map<String, Port> cachedOutputs = Maps.newHashMap();

    public Map<String, Port> inputs = Maps.newHashMap();
    public Map<String, Port> outputs = Maps.newHashMap();

    /**
     * A circuit can be set to listen to any number of ports available on its network, but one port can only ever
     * listen to one other port on the network, but another port on the network can be listened to by multiple
     * ports
     *
     * Listeners are sorted by a circuit's UUID, and from there stored in a Map where the source output port is the key
     * and our port that's listening is the value
     *
     * Registering a listener will also immediately send the newly registered listener the last value available from
     * the output port
     */
    private Map<UUID, Map<String, String>> listeningPorts = Maps.newHashMap();

    public void registerListener(TileToolContainer toolContainer, String output, String input) {
        Map<String, String> map = listeningPorts.get(toolContainer.uuid);
        if (map == null) map = Maps.newHashMap();
        map.put(output, input);
        listeningPorts.put(toolContainer.uuid, map);

        this.updateInput(input, toolContainer.getOutput(output).value);
    }

    public Port getListeningPort(TileToolContainer toolContainer, String origin) {
        Map<String, String> ports = listeningPorts.get(toolContainer.uuid);
        if (ports == null) return null;
        return inputs.get(ports.get(origin));
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final void registerInput(String key, DataType type) {
        if (cachedInputs.containsKey(key)) {
            inputs.put(key, cachedInputs.get(key));
        } else {
            inputs.put(key, Port.create(key, type));
        }
    }

    public final void registerOutput(String key, DataType type) {
        if (cachedOutputs.containsKey(key)) {
            outputs.put(key, cachedOutputs.get(key));
        } else {
            outputs.put(key, Port.create(key, type));
        }
    }

    public final Port getInput(String key) {
        return inputs.get(key);
    }

    public final Port getOutput(String key) {
        return outputs.get(key);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (getGrid() == null)
                setGrid(ConnectivityGrid.createOrJoin(this));
        }

        if (!initialized) {
            initialize();
            initialized = true;
        }
    }

    @Override
    public void writeToDisk(NBTTagCompound compound) {
        super.writeToDisk(compound);

        compound.setLong(NBT_KEY_UUID_MOST, uuid.getMostSignificantBits());
        compound.setLong(NBT_KEY_UUID_LEAST, uuid.getLeastSignificantBits());

        // Data
        NBTTagList inputs = new NBTTagList();
        for (Port input : this.inputs.values()) {
            NBTTagCompound tag = new NBTTagCompound();
            input.writeToTag(tag);
            inputs.appendTag(tag);
        }
        compound.setTag("Inputs", inputs);

        NBTTagList outputs = new NBTTagList();
        for (Port output : this.outputs.values()) {
            NBTTagCompound tag = new NBTTagCompound();
            output.writeToTag(tag);
            outputs.appendTag(tag);
        }
        compound.setTag("Outputs", outputs);

        // Listeners
        NBTTagList listening = new NBTTagList();
        for (UUID uuid : listeningPorts.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();

            tag.setLong(NBT_KEY_UUID_MOST, uuid.getMostSignificantBits());
            tag.setLong(NBT_KEY_UUID_LEAST, uuid.getLeastSignificantBits());

            NBTTagList list = new NBTTagList();

            Map<String, String> connections = listeningPorts.get(uuid);
            for (String key : connections.keySet()) {
                NBTTagCompound tag1 = new NBTTagCompound();
                tag1.setString(NBT_KEY_OUTPUT, key);
                tag1.setString(NBT_KEY_INPUT, connections.get(key));
                list.appendTag(tag1);
            }

            tag.setTag(NBT_KEY_CONNECTIONS, list);

            listening.appendTag(tag);
        }

        compound.setTag(NBT_KEY_LISTENERS, listening);
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        uuid = new UUID(compound.getLong(NBT_KEY_UUID_MOST), compound.getLong(NBT_KEY_UUID_LEAST));

        // Data
        NBTTagList inputs = compound.getTagList("Inputs", TAG_COMPOUND);
        for (int i=0; i<inputs.tagCount(); i++) {
            Port port = Port.fromNbt(inputs.getCompoundTagAt(i));
            cachedInputs.put(port.name, port);
        }

        NBTTagList outputs = compound.getTagList("Outputs", TAG_COMPOUND);
        for (int i=0; i<outputs.tagCount(); i++) {
            Port port = Port.fromNbt(outputs.getCompoundTagAt(i));
            cachedOutputs.put(port.name, port);
        }

        // Listening
        NBTTagList nbtListening = compound.getTagList(NBT_KEY_LISTENERS, TAG_COMPOUND);
        for (int i=0; i<nbtListening.tagCount(); i++) {
            NBTTagCompound listener = nbtListening.getCompoundTagAt(i);
            UUID uuid = new UUID(listener.getLong(NBT_KEY_UUID_MOST), listener.getLong(NBT_KEY_UUID_LEAST));

            NBTTagList nbtConnections = listener.getTagList(NBT_KEY_CONNECTIONS, TAG_COMPOUND);
            Map<String, String> connections = Maps.newHashMap();

            for (int j=0; j<nbtConnections.tagCount(); j++) {
                NBTTagCompound connection = nbtConnections.getCompoundTagAt(j);
                connections.put(connection.getString(NBT_KEY_OUTPUT), connection.getString(NBT_KEY_INPUT));
            }

            listeningPorts.put(uuid, connections);
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }
    public String getName() {
        return this.name;
    }
    public boolean hasInputs() {
        return inputs.size() > 0;
    }
    public boolean hasOutputs() {
        return outputs.size() > 0;
    }

    public final void updateInput(String port, Object value) {
        Port p = inputs.get(port);
        if (p == null) {
            throw new RuntimeException();
        }

        p.value = value;

        inputs.put(port, p);

        if (world.isRemote)
            return;

        triggerInputChange(port, value);
    }

    public final void updateOutput(String port, Object value) {
        Port p = outputs.get(port);
        if (p == null) {
            throw new RuntimeException();
        }

        p.value = value;

        outputs.put(port, p);

        if (world.isRemote)
            return;

        grid.propagateOutputUpdate(this, port, value);
    }

    public final void resetOutputs() {
        for (Port p : outputs.values()) {
            updateOutput(p.name, p.type.zero);
        }
    }

    public void initialize() {

    }

    public void triggerInputChange(String port, Object value) {

    }

    /* GRID MEMBER */

    @Override
    public ConnectivityGrid.MemberType getMemberType() {
        return ConnectivityGrid.MemberType.NODE;
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
        return grid;
    }

    @Override
    public boolean canConnectTo(IGridMember otherMember) {
        return true;
    }

    @Override
    public void onGridUpdate(ConnectivityGrid.UpdateType updateType) {
        if (updateType == ConnectivityGrid.UpdateType.NEIGHBOR)
            return;

        // When there's a grid update, it generally means one of two things. Either a member of the grid was removed,
        // or the grid itself was changed. Either way, we need to search through to grid to see if any of the blocks we
        // were listening to are no longer there, and if so, reset the inputs listening to them
        Set<UUID> nodes = grid.getMembers().values().stream()
                .filter(m -> m.getMemberType() == ConnectivityGrid.MemberType.NODE)
                .filter(m -> m instanceof TileToolContainer)
                .map(m -> ((TileToolContainer)m).getUuid())
                .collect(Collectors.toSet());

        Iterator<UUID> listeners = listeningPorts.keySet().iterator();
        while (listeners.hasNext()) {
            UUID uuid = listeners.next();

            // If one of the nodes we're listening to no longer exists, we need to remove it, and ensure that all the
            // ports that were listening to said node have their values reset
            if (!nodes.contains(uuid)) {
                // Key is the port we were listening to, value is the port we're listening from
                Map<String, String> connection = listeningPorts.get(uuid);

                connection.values().forEach(name -> {
                    Port port = getInput(name);
                    updateInput(name, port.type.zero);
                });

                listeners.remove();
            }
        }
    }
}
