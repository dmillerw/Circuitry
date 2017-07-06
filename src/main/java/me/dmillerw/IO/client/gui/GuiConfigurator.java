package me.dmillerw.io.client.gui;

import com.google.common.collect.Maps;
import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author dmillerw
 */
public class GuiConfigurator extends GuiScreen {

    public static Map<BlockPos, Set<Pair<String, DataType>>> networkMembers = Maps.newHashMap();

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/configurator_debug.png");

    private static final int WIDTH = 118;
    private static final int HEIGHT = 174;

    private TileToolContainer circuitTile;

    private GuiButton buttonDetails;
    private GuiButton buttonLink;
    private GuiButton buttonConfig;

    private int guiLeft;
    private int guiTop;

    public GuiConfigurator(TileToolContainer circuitTile) {
        this.circuitTile = circuitTile;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.WIDTH) / 2;
        this.guiTop = (this.height - this.HEIGHT) / 2;

        this.buttonList.add(this.buttonDetails = new GuiButtonExt(0, guiLeft + 8, guiTop +  88, 102, 17, "Details"));
        this.buttonList.add(this.buttonLink = new GuiButtonExt(1, guiLeft + 8, guiTop + 115, 102, 17, "Link"));
        this.buttonList.add(this.buttonConfig = new GuiButtonExt(2, guiLeft + 8, guiTop + 142, 102, 17, "Configuration"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        int x = guiLeft + 27;
        int y = guiTop + 13;

        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 2);
        GlStateManager.translate(-82, -16, 0);

        RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.CRAFTING_TABLE), x, y);

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiLinking(circuitTile));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
