package me.dmillerw.io.client.gui.config.element;

import me.dmillerw.io.client.gui.config.GuiConfig;
import me.dmillerw.io.lib.cls.IntTuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * @author dmillerw
 */
public abstract class Element {

    protected GuiConfig parentGui;
    public final void setParentGui(GuiConfig gui) {
        this.parentGui = gui;
    }

    public abstract IntTuple getPosition();
    public abstract void setPosition(int posX, int posY);
    public abstract int getHeight();

    public boolean onMouseClick(int mouseX, int mouseY, int mouseButton) { return false; }
    public boolean onKeyTyped(int keycode, char key) { return false; }
    public void draw(int mouseX, int mouseY, float partialTicks) {}

    public static Minecraft mc() { return Minecraft.getMinecraft(); }
    public static FontRenderer fontRenderer() { return mc().fontRenderer; }
}
