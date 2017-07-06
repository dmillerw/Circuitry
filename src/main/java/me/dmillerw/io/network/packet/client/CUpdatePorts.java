package me.dmillerw.io.network.packet.client;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author dmillerw
 */
public class CUpdatePorts implements IMessage {

    private static void writePorts(ByteBuf buf, Port[] ports) {
        if (ports.length > 0) {
            buf.writeBoolean(true);
            buf.writeInt(ports.length);

            for (Port port : ports)
                port.writeToByteBuf(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    private static Port[] readPorts(ByteBuf buf) {
        if (buf.readBoolean()) {
            Port[] ports = new Port[buf.readInt()];

            for (int i=0; i<ports.length; i++) {
                ports[i] = Port.fromByteBuf(buf);
            }

            return ports;
        } else {
            return new Port[0];
        }
    }

    public BlockPos target;

    public Port[] inputs = new Port[0];
    public Port[] outputs = new Port[0];

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());

        CUpdatePorts.writePorts(buf, inputs);
        CUpdatePorts.writePorts(buf, outputs);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());

        inputs = CUpdatePorts.readPorts(buf);
        outputs = CUpdatePorts.readPorts(buf);
    }

    public static class Handler implements IMessageHandler<CUpdatePorts, IMessage> {

        @Override
        public IMessage onMessage(CUpdatePorts message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tile = mc.world.getTileEntity(message.target);
                    if (tile != null) {
                        for (Port port : message.inputs) ((TileToolContainer)tile).updateInput(port.name, port.value);
                        for (Port port : message.outputs) ((TileToolContainer)tile).updateOutput(port.name, port.value);
                    }
                }
            });
            return null;
        }
    }
}
