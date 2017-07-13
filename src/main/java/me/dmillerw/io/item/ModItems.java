package me.dmillerw.io.item;

import me.dmillerw.io.lib.ModInfo;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author dmillerw
 */
@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

    public static final ItemDebugger debugger = null;
    public static final ItemLinkingTool linking_tool = null;

    @Mod.EventBusSubscriber
    public static class Loader {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new ItemDebugger().setRegistryName(ModInfo.MOD_ID, "debugger"),
                    new ItemLinkingTool().setRegistryName(ModInfo.MOD_ID, "linking_tool")
            );
        }
    }
}
