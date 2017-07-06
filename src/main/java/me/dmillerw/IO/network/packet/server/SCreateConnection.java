package me.dmillerw.io.network.packet.server;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author dmillerw
 */
public class SCreateConnection implements IMessage {

    public BlockPos sourcePosition;
    public BlockPos destPosition;
    public String sourcePort;
    public String destPort;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(sourcePosition.toLong());
        buf.writeLong(destPosition.toLong());
        ByteBufUtils.writeUTF8String(buf, sourcePort);
        ByteBufUtils.writeUTF8String(buf, destPort);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sourcePosition = BlockPos.fromLong(buf.readLong());
        destPosition = BlockPos.fromLong(buf.readLong());
        sourcePort = ByteBufUtils.readUTF8String(buf);
        destPort = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<SCreateConnection, IMessage> {

        @Override
        public IMessage onMessage(SCreateConnection message, MessageContext ctx) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = ctx.getServerHandler().player.world;

                    TileToolContainer source = (TileToolContainer) world.getTileEntity(message.sourcePosition);
                    if (source == null) return;
                    TileToolContainer dest = (TileToolContainer) world.getTileEntity(message.destPosition);
                    if (dest == null) return;

                    dest.registerListener(source, message.sourcePort, message.destPort);
                }
            });
            return null;
        }
    }
}
