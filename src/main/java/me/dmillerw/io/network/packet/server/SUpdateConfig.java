package me.dmillerw.io.network.packet.server;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.api.IConfigurable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
public class SUpdateConfig implements IMessage {

    public BlockPos target;
    public NBTTagCompound tag = new NBTTagCompound();

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());
        tag = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<SUpdateConfig, IMessage> {

        @Override
        public IMessage onMessage(SUpdateConfig message, MessageContext ctx) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = ctx.getServerHandler().player.world;
                    TileEntity tile = world.getTileEntity(message.target);
                    if (tile != null && tile instanceof IConfigurable)
                        ((IConfigurable) tile).onConfigurationUpdate(message.tag);
                }
            });
            return null;
        }
    }
}
