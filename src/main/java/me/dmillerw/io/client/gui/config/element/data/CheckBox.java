package me.dmillerw.io.client.gui.config.element.data;

import me.dmillerw.io.lib.cls.IntTuple;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class CheckBox extends ElementWithValue<Boolean> {

    public static final ResourceLocation WIDGETS = new ResourceLocation("io:textures/gui/widgets.png");

    public static CheckBox construct(String key, boolean value) {
        return new CheckBox(key, value, "");
    }

    public static CheckBox construct(String key, boolean value, String label) {
        return new CheckBox(key, value, label);
    }

    private int x;
    private int y;

    private String label;

    private boolean value;

    public CheckBox(String key, boolean value, String label) {
        super(key);

        this.value = value;
        this.label = label;
    }

    public CheckBox setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void loadFromNBTTag(NBTBase tag) {
        value = ((NBTTagByte)tag).getByte() > 0;
    }

    @Override
    public NBTBase toNBTTag() {
        return new NBTTagByte((byte) (value ? 1 : 0));
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
        return 14;
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= x && mouseX <= x + 14 && mouseY >= y && mouseY <= y + 14) {
            value = !value;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);

        boolean inBounds = mouseX >= x && mouseX <= x + 14 && mouseY >= y && mouseY <= y + 14;
        int u = this.value ? 28 : 0;
        int v = inBounds ? 14 : 0;

        GlStateManager.color(1, 1, 1, 1);

        bindTexture(WIDGETS);
        parentGui.drawTexturedModalRect(x, y, u, v, 14, 14);

        if (label != null && !label.isEmpty()) {
            fontRenderer().drawString(label, x + 18, y + 2.5f, 4210752, false);
        }
    }
}
