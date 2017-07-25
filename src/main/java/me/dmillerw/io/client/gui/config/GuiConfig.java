package me.dmillerw.io.client.gui.config;

import com.google.common.collect.Lists;
import me.dmillerw.io.api.IConfigurable;
import me.dmillerw.io.client.gui.config.element.Element;
import me.dmillerw.io.client.gui.config.element.data.ElementWithValue;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.server.SUpdateConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author dmillerw
 */
public class GuiConfig extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/configurator_debug.png");

    public static final int BUTTON_CANCEL = 0;
    public static final int BUTTON_OKAY = 1;

    public static final int WIDTH = 187;
    public static final int HEIGHT = 236;

    public static final int SCROLL_X = 166;
    public static final int SCROLL_Y = 9;
    public static final int SCROLL_Y_END = 194;

    public static final int CONFIG_AREA_X = 9;
    public static final int CONFIG_AREA_Y = 9;
    public static final int CONFIG_AREA_WIDTH = 150;
    public static final int CONFIG_AREA_HEIGHT = 200;

    public static final int HORIZONTAL_MARGIN = 5;
    public static final int VERTICAL_MARGIN = 5;

    public static final int VERTICAL_PADDING = 5;

    public static final int ACTUAL_CONFIG_AREA_HEIGHT = CONFIG_AREA_HEIGHT - (VERTICAL_MARGIN * 2);
    public static final int ELEMENT_WIDTH = CONFIG_AREA_WIDTH - (HORIZONTAL_MARGIN * 2);

    private LinkedList<Element> elements = Lists.newLinkedList();

    private void addElements(Collection<Element> newElements) {
        newElements.forEach((e) -> e.setParentGui(this));
        elements.addAll(newElements);
    }

    private IConfigurable configurable;

    private int guiLeft;
    private int guiTop;

    private int scrollingIndex = 0;

    private int totalHeight;
    private int spillover;

    private boolean mouseButtonDown = false;

    public GuiConfig(IConfigurable configurable) {
        this.configurable = configurable;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;

        buttonList.add(new GuiButtonExt(BUTTON_CANCEL, guiLeft + 8,  guiTop + 214, 82, 14, "Cancel"));
        buttonList.add(new GuiButtonExt(BUTTON_OKAY,   guiLeft + 97, guiTop + 214, 82, 14, "Okay"));

        LinkedList<Element> list = Lists.newLinkedList();
        configurable.getElements(list);
        addElements(list);

        if (configurable.getConfiguration() != null) {
            elements.forEach((e) -> {
                if (e instanceof ElementWithValue) {
                    NBTBase tag = configurable.getConfiguration().getTag(((ElementWithValue) e).key);
                    if (tag != null) {
                        ((ElementWithValue) e).loadFromNBTTag(tag);
                    }
                }
            });
        }

        final int[] height = {0};
        elements.forEach((e) -> height[0] += e.getHeight() + 4);
        this.totalHeight = height[0];

        if (totalHeight > ACTUAL_CONFIG_AREA_HEIGHT)
            this.spillover = totalHeight - ACTUAL_CONFIG_AREA_HEIGHT;
        else
            this.spillover = 0;

        updatePositions();
    }

    private void updatePositions() {
        final int[] yPos = {guiTop + CONFIG_AREA_Y + VERTICAL_MARGIN - scrollingIndex};
        elements.forEach((e) -> {
            e.setPosition(guiLeft + CONFIG_AREA_X + HORIZONTAL_MARGIN, yPos[0]);
            yPos[0] += e.getHeight() + 4;
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int delta = Mouse.getDWheel();
        if (delta != 0) onMouseScroll(delta);

        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        if (spillover > 0) {
            if (mouseButtonDown)
                updateScroll(mouseY);

            float progress = (float)scrollingIndex / (float)spillover;
            int scrollPos = (int)((float)(SCROLL_Y_END - SCROLL_Y) * progress);

            drawTexturedModalRect(guiLeft + SCROLL_X, guiTop + SCROLL_Y + scrollPos, 232, 0, 12, 15);
        } else {
            drawTexturedModalRect(guiLeft + SCROLL_X, guiTop + SCROLL_Y, 244, 0, 12, 15);
        }

        doGlScissor(guiLeft + CONFIG_AREA_X, guiTop + CONFIG_AREA_Y, CONFIG_AREA_WIDTH, CONFIG_AREA_HEIGHT);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        elements.forEach((e) -> e.draw(mouseX, mouseY, partialTicks));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void onMouseScroll(int delta) {
        if (spillover > 0 && delta != 0) {
            if (delta > 0) {
                scrollingIndex -= 10;
                if (scrollingIndex < 0)
                    scrollingIndex = 0;

            } else if (delta < 0) {
                scrollingIndex += 10;
                if (scrollingIndex > spillover)
                    scrollingIndex = spillover;
            }

            updatePositions();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (Element element : elements)
            if (element.onKeyTyped(keyCode, typedChar)) break;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX >= guiLeft + SCROLL_X && mouseX <= guiLeft + SCROLL_X + 12 && mouseY >= guiTop + SCROLL_Y && mouseY <= guiTop + SCROLL_Y_END + 15) {
            mouseButtonDown = true;

            updateScroll(mouseY);
        } else {
            mouseButtonDown = false;
        }

        for (Element element : elements)
            if (element.onMouseClick(mouseX, mouseY, mouseButton)) break;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        mouseButtonDown = false;
    }

    private void updateScroll(int mouseY) {
        float point = ((mouseY - (guiTop + SCROLL_Y)) / (float)(SCROLL_Y_END - SCROLL_Y));
        point = MathHelper.clamp(point, 0, 1);
        scrollingIndex = (int) ((float)spillover * point);

        updatePositions();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == BUTTON_CANCEL) {
            mc.displayGuiScreen(null);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            elements.forEach((e) -> {
                if (e instanceof ElementWithValue) {
                    tag.setTag(((ElementWithValue) e).key, ((ElementWithValue) e).toNBTTag());
                }
            });

            SUpdateConfig packet = new SUpdateConfig();
            packet.target = ((TileEntity)configurable).getPos();
            packet.tag = tag;

            PacketHandler.INSTANCE.sendToServer(packet);

            mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;

        if (k == 0) {
            k = 1000;
        }

        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }

        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

}
