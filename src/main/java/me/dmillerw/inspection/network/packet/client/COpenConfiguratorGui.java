package me.dmillerw.inspection.network.packet.client;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import me.dmillerw.inspection.circuit.data.DataType;
import me.dmillerw.inspection.client.gui.GuiConfigurator;
import me.dmillerw.inspection.network.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Set;

/**
 * @author dmillerw
 */
public class COpenConfiguratorGui implements IMessage {

    public BlockPos blockPos;
    public Map<BlockPos, Set<Pair<String, DataType>>> networkMembers = Maps.newHashMap();

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos.toLong());

        buf.writeBoolean(!networkMembers.isEmpty());
        buf.writeInt(networkMembers.size());

        for (BlockPos pos : networkMembers.keySet()) {
            buf.writeLong(pos.toLong());

            Set<Pair<String, DataType>> outputs = networkMembers.get(pos);

            buf.writeBoolean(!outputs.isEmpty());
            buf.writeInt(outputs.size());

            for (Pair<String, DataType> pair : outputs) {
                ByteBufUtils.writeUTF8String(buf, pair.getLeft());
                buf.writeByte(pair.getRight().ordinal());
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = BlockPos.fromLong(buf.readLong());

        networkMembers.clear();

        if (buf.readBoolean()) {
            int blockCount = buf.readInt();
            for (int i=0; i<blockCount; i++) {
                BlockPos pos = BlockPos.fromLong(buf.readLong());
                Set<Pair<String, DataType>> set = Sets.newHashSet();

                if (buf.readBoolean()) {
                    int outCount = buf.readInt();
                    for (int j=0; j<outCount; j++) {
                        set.add(Pair.of(ByteBufUtils.readUTF8String(buf), DataType.values()[buf.readByte()]));
                    }
                }

                networkMembers.put(pos, set);
            }
        }
    }

    public static class Handler implements IMessageHandler<COpenConfiguratorGui, IMessage> {

        @Override
        public IMessage onMessage(COpenConfiguratorGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    GuiConfigurator.networkMembers = message.networkMembers;
                    GuiHandler.Gui.CONFIGURE_CONNECTION.openGui(Minecraft.getMinecraft().player, message.blockPos);
                }
            });
            return null;
        }
    }
}
