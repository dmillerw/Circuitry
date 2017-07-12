package me.dmillerw.io.network.packet.server;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.item.ItemLinkingTool;
import me.dmillerw.io.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static net.minecraftforge.fml.common.network.ByteBufUtils.readUTF8String;
import static net.minecraftforge.fml.common.network.ByteBufUtils.writeUTF8String;

/**
 * @author dmillerw
 */
public class SUpdateLinkingTool implements IMessage {

    public static enum Type {SET_TARGET, RESET}

    public Type type;
    public BlockPos target;
    public String port;
    public DataType dataType;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        if (type == Type.SET_TARGET) {
            buf.writeLong(target.toLong());
            writeUTF8String(buf, port);
            buf.writeInt(dataType.ordinal());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Type.values()[buf.readInt()];
        if (type == Type.SET_TARGET) {
            target = BlockPos.fromLong(buf.readLong());
            port = readUTF8String(buf);
            dataType = DataType.values()[buf.readInt()];
        }
    }

    public static class Handler implements IMessageHandler<SUpdateLinkingTool, IMessage> {

        @Override
        public IMessage onMessage(SUpdateLinkingTool message, MessageContext ctx) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = ctx.getServerHandler().player;
                    ItemStack held = player.getHeldItemMainhand();
                    if (held.isEmpty())
                        return;

                    if (held.getItem() != ModItems.linking_tool)
                        return;

                    if (message.type == Type.SET_TARGET)
                        ItemLinkingTool.setTarget(held, message.target, message.port, message.dataType);
                    else
                        ItemLinkingTool.reset(held);


                    ((EntityPlayerMP) player).updateHeldItem();
                }
            });
            return null;
        }
    }
}
