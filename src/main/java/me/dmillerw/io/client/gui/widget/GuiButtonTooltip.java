package me.dmillerw.io.client.gui.widget;

import me.dmillerw.io.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import scala.actors.threadpool.Arrays;

/**
 * @author dmillerw
 */
public class GuiButtonTooltip extends GuiButtonExt {

    private String tooltip;

    public GuiButtonTooltip(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public GuiButtonTooltip(int id, int xPos, int yPos, int width, int height, String displayString) {
        super(id, xPos, yPos, width, height, displayString);
    }

    public GuiButtonTooltip setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public void drawTooltip(Minecraft mc, int mouseX, int mouseY) {
        if (enabled && isMouseOver() && tooltip != null && !tooltip.isEmpty())
            RenderUtil.drawHoveringText(mc.displayWidth, mc.displayHeight, Arrays.asList(new String[] { tooltip }), mouseX, mouseY, mc.fontRenderer);
    }
}
