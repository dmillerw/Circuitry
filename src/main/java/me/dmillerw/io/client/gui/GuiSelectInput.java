package me.dmillerw.io.client.gui;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.client.gui.widget.GuiButtonTooltip;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.server.SResetConnection;
import me.dmillerw.io.network.packet.server.SUpdateLinkingTool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * @author dmillerw
 */
public class GuiSelectInput extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/linking_input.png");

    private static final int WIDTH = 255;
    private static final int HEIGHT = 144;
    private static final int MAX_BUTTONS = 10;

    private TileToolContainer circuitTile;
    private ItemStack linkingTool;

    private Port[] destInputPorts = new Port[0];
    private GuiButtonExt[] destInputButtons = new GuiButtonExt[0];
    private GuiButtonExt[] destResetButtons = new GuiButtonExt[0];

    private int guiLeft;
    private int guiTop;

    public GuiSelectInput(TileToolContainer circuitTile) {
        this.circuitTile = circuitTile;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;

        final int xDistance = 107;
        final int yDistance = 19;

        this.destInputPorts = circuitTile.inputs.values().toArray(new Port[0]);
        this.destInputButtons = new GuiButtonExt[MAX_BUTTONS];
        this.destResetButtons = new GuiButtonExt[MAX_BUTTONS];

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 2; ++j) {
                int index = j + i * 2;

                GuiButtonTooltip input = new GuiButtonTooltip(index, guiLeft + 27 + xDistance * j, guiTop + 47 + yDistance * i, 83, 14, "");
                this.destInputButtons[index] = input;

                if (index < destInputPorts.length) {
                    final String name = destInputPorts[index].getName();
                    String display = name;
                    boolean enabled = true;

                    if (circuitTile.isPortListening(name)) {
                        display = TextFormatting.RED + display;
                        enabled = false;
                    }

                    input.displayString = display;
                    input.enabled = enabled;

                    input.setTooltip(destInputPorts[index].getType().toString());
                } else {
                    input.enabled = false;
                }

                this.buttonList.add(input);

                GuiButtonTooltip reset = new GuiButtonTooltip(MAX_BUTTONS + index, guiLeft + 8 + xDistance * j, guiTop + 47 + yDistance * i, 14, 14, "R");
                this.destResetButtons[index] = reset;

                this.buttonList.add(reset);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        fontRenderer.drawString(circuitTile.getName(), guiLeft + 8, guiTop + 8, 4210752);

        fontRenderer.drawString(circuitTile.getNickname(), guiLeft + 8, guiTop + 21, 4210752);

        final BlockPos pos = circuitTile.getPos();
        fontRenderer.drawString("X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ(), guiLeft + 8, guiTop + 34, 4210752);

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton button = this.buttonList.get(i);
            if (button instanceof GuiButtonTooltip) ((GuiButtonTooltip) button).drawTooltip(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id < MAX_BUTTONS) {
            SUpdateLinkingTool packet = new SUpdateLinkingTool();
            packet.type = SUpdateLinkingTool.Type.SET_TARGET;
            packet.target = circuitTile.getPosition();
            packet.port = destInputPorts[button.id].getName();
            packet.dataType = destInputPorts[button.id].getType();

            PacketHandler.INSTANCE.sendToServer(packet);

            mc.player.sendChatMessage("Linking started");

            mc.displayGuiScreen(null);
        } else {
            int id = button.id - MAX_BUTTONS;

            SResetConnection packet = new SResetConnection();
            packet.target = circuitTile.getPosition();
            packet.input = destInputPorts[id].getName();

            PacketHandler.INSTANCE.sendToServer(packet);

            destInputButtons[id].displayString = destInputPorts[id].getName();
            destInputButtons[id].enabled = true;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
