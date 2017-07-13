package me.dmillerw.io;

import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author dmillerw
 */
@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION)
public class IO {

    @Mod.Instance(ModInfo.MOD_ID)
    public static IO INSTANCE;

    @SidedProxy(
            serverSide = "me.dmillerw.io.proxy.CommonProxy",
            clientSide = "me.dmillerw.io.proxy.ClientProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit(event);
    }
}
