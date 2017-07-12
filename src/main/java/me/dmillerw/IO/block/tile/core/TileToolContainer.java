package me.dmillerw.io.block.tile.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.dmillerw.io.api.IGridMember;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.NullValue;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.data.Value;
import me.dmillerw.io.circuit.grid.ConnectivityGrid;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.client.CAddListener;
import me.dmillerw.io.network.packet.client.CRemoveListener;
import me.dmillerw.io.network.packet.client.CUpdatePorts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

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
    private String nickname;

    private boolean initialized = false;

    private Map<String, Value> queuedUpdates = Maps.newHashMap();

    // Stored in NBT on world load, and used in place of zero values once a port is registered
    private Map<String, Port> cachedInputs = Maps.newHashMap();
    private Map<String, Port> cachedOutputs = Maps.newHashMap();

    public Map<String, Port> inputs = Maps.newHashMap();
    public Map<String, Port> outputs = Maps.newHashMap();

    /**
     * A circuit can be set to listen to any number of ports available on its network, but one port can only ever
     * listen to one other port on the network, but another port on the network can be listened to by multiple
     * ports
     * <p>
     * Listeners are stored so that an input can only ever expect to receive an update from one other port on the network
     * With the main key of the Map being our input port, and the value being a Pair defining which circuit and output
     * we're listening to
     * <p>
     * Map is also a BiMap, to allow for easier lookup of what (if any) input is listening for a specific output
     * <p>
     * Registering a listener will also immediately send the newly registered listener the last value available from
     * the output port
     */
    private BiMap<String, Pair<UUID, String>> listeners = HashBiMap.create();

    public boolean registerListener(TileToolContainer toolContainer, String output, String input) {
        if (!listeners.containsKey(input)) {
            if (!world.isRemote) {
                if (!grid.contains(toolContainer))
                    return false;

                CAddListener packet = new CAddListener();
                packet.target = this.pos;
                packet.destInput = input;
                packet.sourceUuid = toolContainer.uuid;
                packet.sourceOutput = output;

                PacketHandler.sendToAllWatching(packet, this);

                this.updateInput(input, toolContainer.getOutput(output).value);
            }

            listeners.put(input, Pair.of(toolContainer.uuid, output));

            return true;
        } else {
            return false;
        }
    }

    public final void raw_registerListener(UUID uuid, String output, String input) {
        if (!world.isRemote) return;
        listeners.put(input, Pair.of(uuid, output));
    }

    public void removeListener(String input) {
        if (listeners.containsKey(input)) {
            if (!world.isRemote) {
                CRemoveListener packet = new CRemoveListener();
                packet.target = this.getPosition();
                packet.destInput = input;

                PacketHandler.sendToAllWatching(packet, this);

                updateInput(input, NullValue.NULL);
            }

            listeners.remove(input);
        }
    }

    public boolean isPortListening(String input) {
        return listeners.containsKey(input);
    }

    public Port getListeningPort(TileToolContainer toolContainer, String origin) {
        Pair<UUID, String> pair = Pair.of(toolContainer.getUuid(), origin);
        return inputs.get(listeners.inverse().get(pair));
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
            if (getGrid() == null) {
                setGrid(ConnectivityGrid.createOrJoin(this));
            } else {
                for (Map.Entry<String, Value> update : queuedUpdates.entrySet()) {
                    grid.propagateOutputUpdate(this, update.getKey(), update.getValue());
                }
            }
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

        compound.setString("Nickname", nickname == null ? "" : nickname);

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
        for (String input : listeners.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();

            tag.setString("Input", input);

            Pair<UUID, String> pair = listeners.get(input);

            tag.setLong("UuidMost", pair.getLeft().getMostSignificantBits());
            tag.setLong("UuidLeast", pair.getLeft().getLeastSignificantBits());
            tag.setString("Output", pair.getRight());

            listening.appendTag(tag);
        }

        compound.setTag(NBT_KEY_LISTENERS, listening);
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        uuid = new UUID(compound.getLong(NBT_KEY_UUID_MOST), compound.getLong(NBT_KEY_UUID_LEAST));

        nickname = compound.getString("Nickname");

        // Data
        NBTTagList inputs = compound.getTagList("Inputs", TAG_COMPOUND);
        for (int i = 0; i < inputs.tagCount(); i++) {
            Port port = Port.fromNbt(inputs.getCompoundTagAt(i));
            cachedInputs.put(port.name, port);
        }

        NBTTagList outputs = compound.getTagList("Outputs", TAG_COMPOUND);
        for (int i = 0; i < outputs.tagCount(); i++) {
            Port port = Port.fromNbt(outputs.getCompoundTagAt(i));
            cachedOutputs.put(port.name, port);
        }

        // Listening
        NBTTagList nbtListening = compound.getTagList(NBT_KEY_LISTENERS, TAG_COMPOUND);
        for (int i = 0; i < nbtListening.tagCount(); i++) {
            NBTTagCompound listener = nbtListening.getCompoundTagAt(i);

            String input = listener.getString("Input");
            UUID uuid = new UUID(listener.getLong("UuidMost"), listener.getLong("UuidLeast"));
            String output = listener.getString("Output");

            listeners.put(input, Pair.of(uuid, output));
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean hasInputs() {
        return inputs.size() > 0;
    }

    public boolean hasOutputs() {
        return outputs.size() > 0;
    }

    public final void updateInput(String port, Value rawValue) {
        Port p = inputs.get(port);
        if (p == null) {
            p = cachedInputs.get(port);
            if (p == null)
                throw new RuntimeException(getName() + ": No INPUT called " + port);
        }

        Value value;
        if (rawValue == null)
            value = NullValue.NULL;
        else
            value = Value.of(p.type, rawValue);

        if (!value.equals(p.value)) {
            p.setValue(value);

            inputs.put(port, p);

            if (world.isRemote)
                return;

            PacketHandler.sendToAllWatching(CUpdatePorts.input(this, port), this);

            onInputChange(port, value);
        }
    }

    public final void updateOutput(String port, Object rawValue) {
        Port p = outputs.get(port);
        if (p == null) {
            p = cachedOutputs.get(port);
            if (p == null)
                throw new RuntimeException(getName() + ": No OUTPUT called " + port);
        }

        Value value;
        if (rawValue == null)
            value = NullValue.NULL;
        else
            value = Value.of(p.type, rawValue);

        if (!value.equals(p.value)) {
            p.setValue(value);

            outputs.put(port, p);

            if (world.isRemote)
                return;

            PacketHandler.sendToAllWatching(CUpdatePorts.output(this, port), this);

            queuedUpdates.put(port, value);
        }
    }

    public final void resetOutputs() {
        for (Port p : outputs.values()) {
            updateOutput(p.name, NullValue.NULL);
        }
    }

    public void initialize() {

    }

    public void onInputChange(String port, Value value) {

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
    public boolean canConnectTo(IGridMember otherMember, EnumFacing facing) {
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
                .map(m -> ((TileToolContainer) m).getUuid())
                .collect(Collectors.toSet());

        Set<String> removedNodes = Sets.newHashSet();

        Iterator<Pair<UUID, String>> iterator = listeners.values().iterator();
        while (iterator.hasNext()) {
            Pair<UUID, String> pair = iterator.next();

            // If one of the nodes we're listening to no longer exists, we need to remove it, and ensure that all the
            // ports that were listening to said node have their values reset
            if (!nodes.contains(pair.getKey())) {
                // Key is the port we were listening to, value is the port we're listening from
                String input = listeners.inverse().get(pair);

                updateInput(input, NullValue.NULL);

                removedNodes.add(input);
            }
        }

        for (String input : removedNodes) {
            removeListener(input);
        }
    }
}
