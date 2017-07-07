package me.dmillerw.io.client.render;

import me.dmillerw.io.block.BlockScreen;
import me.dmillerw.io.block.tile.TileScreen;
import me.dmillerw.io.circuit.data.Port;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * @author dmillerw
 */
public class RenderTileScreen extends TileEntitySpecialRenderer<TileScreen> {

    @Override
    public void render(TileScreen te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        final int scale = 4;
        final float mod = 0.010416667F * scale;
        final float offset = (0.010416667F * (4 * scale)) - 0.010416667F;

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        float angleX = 0;
        float angleY = 0;

        switch (te.getWorld().getBlockState(te.getPos()).getValue(BlockScreen.FACING)) {
            case NORTH:
                angleX = 180;
                break;
            case EAST:
                angleX = 90;
                break;
            case WEST:
                angleX = 270;
                break;
            case UP:
                angleY = -90;
                break;
            case DOWN:
                angleY = 90;
                break;
            default:
                break;
        }

        GlStateManager.rotate(angleX, 0, 1, 0);
        GlStateManager.rotate(angleY, 1, 0, 0);

        GlStateManager.translate(mod / 2F, offset, 0.5001);

        FontRenderer fontrenderer = this.getFontRenderer();

        GlStateManager.scale(mod, -mod, 0.010416667F);

        String string = "0";
        if (te.inputs.size() > 0) {
            Port port = te.getInput("Value");
            string = port.value.toString();
        }

        GlStateManager.color(1, 1, 1, 1);

        setLightmapDisabled(true);

        fontrenderer.drawString(string, -fontrenderer.getStringWidth(string) / 2, 0, 0xFFFFFF);

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
