package me.dmillerw.inspection.proxy;

import me.dmillerw.inspection.client.model.BaseModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author dmillerw
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ModelLoaderRegistry.registerLoader(new BaseModelLoader());
    }
}
