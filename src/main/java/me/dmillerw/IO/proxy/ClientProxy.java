package me.dmillerw.io.proxy;

import me.dmillerw.io.block.tile.TileScreen;
import me.dmillerw.io.client.model.BaseModelLoader;
import me.dmillerw.io.client.render.RenderTileScreen;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author dmillerw
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ModelLoaderRegistry.registerLoader(new BaseModelLoader());

        ClientRegistry.bindTileEntitySpecialRenderer(TileScreen.class, new RenderTileScreen());
    }
}
