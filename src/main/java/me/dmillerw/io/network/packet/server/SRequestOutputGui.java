package me.dmillerw.io.network.packet.server;

import io.netty.buffer.ByteBuf;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.item.ItemLinkingTool;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.client.COpenOutputGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author dmillerw
 */
public class SRequestOutputGui implements IMessage {

    public BlockPos target;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(target.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = BlockPos.fromLong(buf.readLong());
    }

    public static class Handler implements IMessageHandler<SRequestOutputGui, IMessage> {

        @Override
        public IMessage onMessage(SRequestOutputGui message, MessageContext ctx) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = ctx.getServerHandler().player;
                    World world = player.world;
                    ItemStack heldItem = player.getHeldItemMainhand();

                    TileToolContainer target = (TileToolContainer) world.getTileEntity(ItemLinkingTool.getTargetPosition(heldItem));
                    if (target != null) {
                        TileToolContainer requested = (TileToolContainer) world.getTileEntity(message.target);

                        if (requested.getGrid().contains(target)) {
                            COpenOutputGui packet = new COpenOutputGui();
                            packet.target = message.target;

                            PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
                        } else {
                            player.sendMessage(new TextComponentString("Cannot link, must be a part of the same network"));
                        }
                    }
                }
            });
            return null;
        }
    }
}
