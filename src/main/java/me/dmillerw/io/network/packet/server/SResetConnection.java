package me.dmillerw.io.network.packet.server;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static net.minecraftforge.fml.common.network.ByteBufUtils.readUTF8String;
import static net.minecraftforge.fml.common.network.ByteBufUtils.writeUTF8String;

/**
 * @author dmillerw
 */
public class SResetConnection implements IMessage {

    public BlockPos target;
    public String input;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());
        writeUTF8String(buf, input);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());
        input = readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<SResetConnection, IMessage> {

        @Override
        public IMessage onMessage(SResetConnection message, MessageContext ctx) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = ctx.getServerHandler().player.world;
                    TileToolContainer target = (TileToolContainer) world.getTileEntity(message.target);
                    if (target != null) target.removeListener(message.input);
                }
            });

            return null;
        }
    }
}
