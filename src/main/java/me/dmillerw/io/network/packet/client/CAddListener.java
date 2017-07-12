package me.dmillerw.io.network.packet.client;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

import static net.minecraftforge.fml.common.network.ByteBufUtils.readUTF8String;
import static net.minecraftforge.fml.common.network.ByteBufUtils.writeUTF8String;

/**
 * @author dmillerw
 */
public class CAddListener implements IMessage {

    public BlockPos target;

    public String destInput;
    public UUID sourceUuid;
    public String sourceOutput;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());
        writeUTF8String(buf, destInput);
        buf.writeLong(sourceUuid.getMostSignificantBits());
        buf.writeLong(sourceUuid.getLeastSignificantBits());
        writeUTF8String(buf, sourceOutput);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());
        destInput = readUTF8String(buf);
        sourceUuid = new UUID(buf.readLong(), buf.readLong());
        sourceOutput = readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<CAddListener, IMessage> {

        @Override
        public IMessage onMessage(CAddListener message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileToolContainer tile = (TileToolContainer) mc.world.getTileEntity(message.target);
                    if (tile != null) {
                        tile.raw_registerListener(message.sourceUuid, message.sourceOutput, message.destInput);
                    }
                }
            });
            return null;
        }
    }
}
