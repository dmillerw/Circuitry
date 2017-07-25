package me.dmillerw.io.client.gui.config.element.data;

import me.dmillerw.io.client.gui.config.GuiConfig;
import me.dmillerw.io.lib.cls.IntTuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

/**
 * @author dmillerw
 */
public class TextField extends ElementWithValue<String> {

    public static TextField of(String key, int height) {
        return new TextField(key, new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, GuiConfig.ELEMENT_WIDTH, height));
    }

    private GuiTextField backingElement;
    private String label;
    private int modifier = 0;

    public TextField(String key, GuiTextField element) {
        super(key);

        this.backingElement = element;
    }

    public TextField setLabel(String label) {
        this.label = label;

        int size = Minecraft.getMinecraft().fontRenderer.getStringWidth(label);
        backingElement.width -= size + 5;
        modifier = size;

        return this;
    }

    @Override
    public String getValue() {
        return backingElement.getText();
    }

    @Override
    public NBTBase toNBTTag() {
        return new NBTTagString(getValue());
    }

    @Override
    public void loadFromNBTTag(NBTBase tag) {
        backingElement.setText(((NBTTagString)tag).getString());
    }

    @Override
    public IntTuple getPosition() {
        return new IntTuple(backingElement.x, backingElement.y);
    }

    @Override
    public void setPosition(int posX, int posY) {
        backingElement.x = posX;
        backingElement.y = posY;
    }

    @Override
    public int getHeight() {
        return backingElement.height;
    }

    @Override
    public boolean onKeyTyped(int keycode, char key) {
        boolean result = backingElement.textboxKeyTyped(key, keycode);
        return result && backingElement.isFocused();
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int mouseButton) {
        boolean result = backingElement.mouseClicked(mouseX, mouseY, mouseButton);
        return result && backingElement.isFocused();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawStringWithShadow(label, backingElement.x, backingElement.y + getHeight() - 9, 0xFFFFFF);

        backingElement.x += (modifier + 5);
        backingElement.drawTextBox();
        backingElement.x -= (modifier + 5);
    }
}
