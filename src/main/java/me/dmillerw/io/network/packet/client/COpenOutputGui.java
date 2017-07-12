package me.dmillerw.io.network.packet.client;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.network.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author dmillerw
 */
public class COpenOutputGui implements IMessage {

    public BlockPos target;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());
    }

    public static class Handler implements IMessageHandler<COpenOutputGui, IMessage> {

        @Override
        public IMessage onMessage(COpenOutputGui message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileToolContainer tile = (TileToolContainer) mc.world.getTileEntity(message.target);
                    if (tile != null) {
                        GuiHandler.Gui.SELECT_OUTPUT.openGui(mc.player, message.target);
                    }
                }
            });
            return null;
        }
    }
}
