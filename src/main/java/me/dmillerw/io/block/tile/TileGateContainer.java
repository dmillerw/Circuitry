package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Value;
import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.circuit.gate.GateRegistry;
import me.dmillerw.io.client.gui.config.element.Element;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public class TileGateContainer extends TileToolContainer {

    private String gate;
    private NBTTagCompound state = new NBTTagCompound();
    private int totalTicks = 0;

    public void setGate(String gate) {
        this.gate = gate;
    }

    @Override
    public void writeToDisk(NBTTagCompound compound) {
        super.writeToDisk(compound);

        compound.setString("Gate", gate);
        compound.setTag("State", state);
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        gate = compound.getString("Gate");
        state = compound.getCompoundTag("State");
    }

    @Override
    public void initialize() {
        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);

        setName(gate.getKey());

        gate.initialize(this);
        gate.calculateOutput(this);

        setName(gate.getKey());
    }

    @Override
    public void update() {
        super.update();

        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        if (!world.isRemote) {
            totalTicks++;

            BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);
            gate.tick(this, totalTicks);
        }
    }

    @Override
    public void onInputChange(String port, Value value) {
        super.onInputChange(port, value);

        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);
        gate.calculateOutput(this);
    }

    @Override
    public void getElements(LinkedList<Element> elements) {
        super.getElements(elements);

        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);
        gate.addElements(elements);
    }

    @Override
    public void onConfigurationUpdate() {
        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);
        gate.calculateOutput(this);
    }

    public final NBTTagCompound getGateState() {
        return state;
    }
}
