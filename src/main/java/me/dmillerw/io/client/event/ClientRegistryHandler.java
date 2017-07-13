package me.dmillerw.io.client.event;

import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.item.ModItems;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author dmillerw
 */
@Mod.EventBusSubscriber
public class ClientRegistryHandler {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        forceState(ModBlocks.cable, new ModelResourceLocation(ModInfo.MOD_ID + ":cable"));

        registerItemModel(ModBlocks.cable_item);
        registerItemModel(ModBlocks.screen_item);
        registerItemModel(ModBlocks.gate_item);
        registerItemModel(ModBlocks.redstone_emitter_item);
        registerItemModel(ModBlocks.redstone_receiver_item);
        registerItemModel(ModBlocks.player_tracker_item);

        ModelLoader.setCustomModelResourceLocation(ModItems.debugger, 0, new ModelResourceLocation(ModItems.debugger.getRegistryName().toString()));
        ModelLoader.setCustomModelResourceLocation(ModItems.linking_tool, 0, new ModelResourceLocation(ModItems.linking_tool.getRegistryName().toString()));
    }

    private static void forceState(Block block, ModelResourceLocation location) {
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return location;
            }
        });
    }

    private static void registerItemModel(Item item) {
        ModelResourceLocation resourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, resourceLocation);
    }

    private static void registerItemModel(Item item, String tag, Enum<? extends IStringSerializable> variant) {
        ModelResourceLocation resourceLocation = new ModelResourceLocation(item.getRegistryName(), tag + "=" + ((IStringSerializable)variant).getName());
        ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), resourceLocation);
    }
}