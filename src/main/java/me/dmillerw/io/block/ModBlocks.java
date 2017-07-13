package me.dmillerw.io.block;

import me.dmillerw.io.block.item.ItemGateContainer;
import me.dmillerw.io.block.tile.circuit.BlockConstantValue;
import me.dmillerw.io.block.tile.circuit.BlockRedstoneEmitter;
import me.dmillerw.io.block.tile.circuit.BlockRedstoneReceiver;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author dmillerw
 */
@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModBlocks {

    public static final BlockCable cable = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":cable")
    public static final ItemBlock cable_item = null;

    public static final BlockRedstoneEmitter redstone_emitter = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":redstone_emitter")
    public static final ItemBlock redstone_emitter_item = null;

    public static final BlockConstantValue constant_value = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":constant_value")
    public static final ItemBlock constant_value_item = null;

    public static final BlockGateContainer gate = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":gate")
    public static final ItemGateContainer gate_item = null;

    public static final BlockRedstoneReceiver redstone_receiver = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":redstone_receiver")
    public static final ItemBlock redstone_receiver_item = null;

    public static final BlockScreen screen = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":screen")
    public static final ItemBlock screen_item = null;

    public static final BlockPlayerTracker player_tracker = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":player_tracker")
    public static final ItemBlock player_tracker_item = null;

    @Mod.EventBusSubscriber
    public static class Loader {

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new BlockCable().setRegistryName(ModInfo.MOD_ID, "cable"),
                    new BlockRedstoneEmitter().setRegistryName(ModInfo.MOD_ID, "redstone_emitter"),
                    new BlockConstantValue().setRegistryName(ModInfo.MOD_ID, "constant_value"),
                    new BlockGateContainer().setRegistryName(ModInfo.MOD_ID, "gate"),
                    new BlockRedstoneReceiver().setRegistryName(ModInfo.MOD_ID, "redstone_receiver"),
                    new BlockScreen().setRegistryName(ModInfo.MOD_ID, "screen"),
                    new BlockPlayerTracker().setRegistryName(ModInfo.MOD_ID, "player_tracker")
            );
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new ItemBlock(ModBlocks.cable).setRegistryName(ModInfo.MOD_ID, "cable"),
                    new ItemBlock(ModBlocks.redstone_emitter).setRegistryName(ModInfo.MOD_ID, "redstone_emitter"),
                    new ItemBlock(ModBlocks.constant_value).setRegistryName(ModInfo.MOD_ID, "constant_value"),
                    new ItemGateContainer(ModBlocks.gate).setRegistryName(ModInfo.MOD_ID, "gate"),
                    new ItemBlock(ModBlocks.redstone_receiver).setRegistryName(ModInfo.MOD_ID, "redstone_receiver"),
                    new ItemBlock(ModBlocks.screen).setRegistryName(ModInfo.MOD_ID, "screen"),
                    new ItemBlock(ModBlocks.player_tracker).setRegistryName(ModInfo.MOD_ID, "player_tracker")
            );
        }
    }
}
