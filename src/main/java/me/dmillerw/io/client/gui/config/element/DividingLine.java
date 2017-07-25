package me.dmillerw.io.client.gui.config.element;

import me.dmillerw.io.client.gui.config.GuiConfig;
import me.dmillerw.io.lib.cls.IntTuple;

import static net.minecraft.client.gui.Gui.drawRect;

/**
 * @author dmillerw
 */
public class DividingLine extends Element {

    public static DividingLine of(int thickness, int color) {
        return new DividingLine(0, 0, thickness, color);
    }

    public int x;
    public int y;

    public int thickness;

    public int color;

    public DividingLine(int x, int y, int thickness, int color) {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
        this.color = color;
    }

    @Override
    public IntTuple getPosition() {
        return new IntTuple(x, y);
    }

    @Override
    public void setPosition(int posX, int posY) {
        this.x = posX;
        this.y = posY;
    }

    @Override
    public int getHeight() {
        return thickness;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        drawRect(x, y, x + GuiConfig.ELEMENT_WIDTH, y + thickness, color);
    }
}
