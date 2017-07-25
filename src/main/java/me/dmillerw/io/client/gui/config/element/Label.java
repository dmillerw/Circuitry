package me.dmillerw.io.client.gui.config.element;

import com.google.common.collect.Lists;
import me.dmillerw.io.client.gui.config.GuiConfig;
import me.dmillerw.io.lib.cls.IntTuple;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Label extends Element {

    public static Label of(String label) {
        return of(0xFFFFFFFF, label);
    }

    public static Label of(int textColor, String label) {
        return new Label(textColor, label);
    }

    public int x = 0;
    public int y = 0;

    private List<String> list;

    private boolean centered;
    private boolean shadow;
    private boolean multiline;

    private final int textColor;

    public Label(int textColor, String label) {
        this.list = Lists.newArrayList(label);
        this.centered = false;
        this.textColor = textColor;
    }

    public Label setMultiline() {
        this.multiline = true;

        List<String> list = Lists.newArrayList();
        list.addAll(Arrays.asList(this.list.get(0).split("\n")));

        this.list.clear();
        list.forEach((s) -> this.list.addAll(fontRenderer().listFormattedStringToWidth(s, GuiConfig.ELEMENT_WIDTH)));

        return this;
    }

    public Label setShadow() {
        this.shadow = true;
        return this;
    }

    public Label setCentered() {
        this.centered = true;
        return this;
    }

    public void drawLabel() {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        int origin = this.y + fontRenderer().FONT_HEIGHT / 2;

        if (multiline) {
            for (int i = 0; i < list.size(); i++) {
                int y = origin + (fontRenderer().FONT_HEIGHT + 4) * i;

                if (this.centered) {
                    this.drawCenteredString(fontRenderer(), list.get(i), this.x + GuiConfig.ELEMENT_WIDTH / 2, y, this.textColor);
                } else {
                    this.drawString(fontRenderer(), list.get(i), this.x, y, this.textColor);
                }
            }
        } else {
            String label = list.get(0);
            if (this.centered) {
                this.drawCenteredString(fontRenderer(), label, this.x + GuiConfig.ELEMENT_WIDTH / 2, origin, this.textColor);
            } else {
                this.drawString(fontRenderer(), label, this.x, origin, this.textColor);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        drawLabel();
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
        return fontRenderer().FONT_HEIGHT + (fontRenderer().FONT_HEIGHT + 4) * list.size();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        if (shadow)
            fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
        else
            fontRendererIn.drawString(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
    }

    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        if (shadow)
            fontRendererIn.drawStringWithShadow(text, (float) x, (float) y, color);
        else
            fontRendererIn.drawString(text, x, y, color);
    }
}