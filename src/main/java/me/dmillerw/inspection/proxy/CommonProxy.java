package me.dmillerw.inspection.proxy;

import me.dmillerw.inspection.Inspection;
import me.dmillerw.inspection.block.tile.TileCable;
import me.dmillerw.inspection.block.tile.TileGateContainer;
import me.dmillerw.inspection.block.tile.circuit.CircuitConstantValue;
import me.dmillerw.inspection.block.tile.circuit.CircuitRedstoneEmitter;
import me.dmillerw.inspection.circuit.grid.GridRegistry;
import me.dmillerw.inspection.lib.ModInfo;
import me.dmillerw.inspection.network.GuiHandler;
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
        GameRegistry.registerTileEntity(CircuitConstantValue.class, ModInfo.MOD_ID + ":constant_value");

        NetworkRegistry.INSTANCE.registerGuiHandler(Inspection.INSTANCE, new GuiHandler());
    }
}
