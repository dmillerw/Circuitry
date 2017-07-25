package me.dmillerw.io.client.gui.config.element;

import me.dmillerw.io.lib.cls.IntTuple;

/**
 * @author dmillerw
 */
public class Spacer extends Element {

    public static Spacer of(int thickness) {
        return new Spacer(0, 0, thickness);
    }

    public int x;
    public int y;
    public int thickness;

    public Spacer(int x, int y, int thickness) {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
    }

    @Override
    public IntTuple getPosition() {
        return new IntTuple(x, y);
    }

    @Override
    public void setPosition(int posX, int posY) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getHeight() {
        return thickness;
    }
}
