package me.dmillerw.io.proxy;

import me.dmillerw.io.IO;
import me.dmillerw.io.block.tile.TileCable;
import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.block.tile.circuit.CircuitConstantValue;
import me.dmillerw.io.block.tile.circuit.CircuitRedstoneEmitter;
import me.dmillerw.io.block.tile.circuit.CircuitRedstoneReceiver;
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

        GameRegistry.registerTileEntity(CircuitRedstoneEmitter.class, ModInfo.MOD_ID + ":redstone_emitter");
        GameRegistry.registerTileEntity(CircuitRedstoneReceiver.class, ModInfo.MOD_ID + ":redstone_receiver");
        GameRegistry.registerTileEntity(CircuitConstantValue.class, ModInfo.MOD_ID + ":constant_value");

        NetworkRegistry.INSTANCE.registerGuiHandler(IO.INSTANCE, new GuiHandler());
    }
}
