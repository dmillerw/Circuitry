package me.dmillerw.io.proxy;

import me.dmillerw.io.IO;
import me.dmillerw.io.block.tile.TileCable;
import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.block.tile.TilePlayerTracker;
import me.dmillerw.io.block.tile.TileScreen;
import me.dmillerw.io.block.tile.circuit.TileRedstoneReceiver;
import me.dmillerw.io.block.tile.circuit.TileRedstoneEmitter;
import me.dmillerw.io.circuit.grid.GridRegistry;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.GuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(GridRegistry.class);

        GameRegistry.registerTileEntity(TileCable.class, ModInfo.MOD_ID + ":cable");
        GameRegistry.registerTileEntity(TileGateContainer.class, ModInfo.MOD_ID + ":gate");
        GameRegistry.registerTileEntity(TileScreen.class, ModInfo.MOD_ID + ":screen");
        GameRegistry.registerTileEntity(TilePlayerTracker.class, ModInfo.MOD_ID + ":player_tracker");

        GameRegistry.registerTileEntity(TileRedstoneReceiver.class, ModInfo.MOD_ID + ":redstone_emitter");
        GameRegistry.registerTileEntity(TileRedstoneEmitter.class, ModInfo.MOD_ID + ":redstone_receiver");

        NetworkRegistry.INSTANCE.registerGuiHandler(IO.INSTANCE, new GuiHandler());
    }
}
