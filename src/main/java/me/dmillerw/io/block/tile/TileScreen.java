package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.client.CUpdatePorts;

/**
 * @author dmillerw
 */
public class TileScreen extends TileToolContainer {

    @Override
    public void initialize() {
        registerInput("Value", DataType.NUMBER);
    }

    @Override
    public void triggerInputChange(String port, Object value) {
        PacketHandler.INSTANCE.sendToDimension(CUpdatePorts.from(this), this.world.provider.getDimension());

    }
}
