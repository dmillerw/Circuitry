package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.circuit.gate.GateRegistry;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author dmillerw
 */
public class TileGateContainer extends TileToolContainer {

    private String gate;
    public void setGate(String gate) {
        this.gate = gate;
    }

    @Override
    public void writeToDisk(NBTTagCompound compound) {
        super.writeToDisk(compound);

        compound.setString("Gate", gate);
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        gate = compound.getString("Gate");
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
    public void triggerInputChange(String port, Object value) {
        super.triggerInputChange(port, value);

        if (gate == null || gate.isEmpty())
            throw new RuntimeException();

        BaseGate gate = GateRegistry.INSTANCE.getGate(this.gate);
        gate.calculateOutput(this);
    }
}
