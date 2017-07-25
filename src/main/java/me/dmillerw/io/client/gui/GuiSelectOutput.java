package me.dmillerw.io.client.gui;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.client.gui.widget.GuiButtonTooltip;
import me.dmillerw.io.item.ItemLinkingTool;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packet.server.SCreateConnection;
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
public class GuiSelectOutput extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/linking_output.png");

    private static final int WIDTH = 187;
    private static final int HEIGHT = 144;
    private static final int MAX_BUTTONS = 10;

    private TileToolContainer circuitTile;
    private ItemStack linkingTool;

    private Port[] destOutputPorts = new Port[0];
    private GuiButtonExt[] destOutputButtons = new GuiButtonExt[0];

    private int guiLeft;
    private int guiTop;

    public GuiSelectOutput(TileToolContainer circuitTile, ItemStack linkingTool) {
        this.circuitTile = circuitTile;
        this.linkingTool = linkingTool;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;

        final int xDistance = 88;
        final int yDstance = 19;

        this.destOutputPorts = circuitTile.outputs.values().toArray(new Port[0]);
        this.destOutputButtons = new GuiButtonExt[MAX_BUTTONS];

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 2; ++j) {
                int index = j + i * 2;

                GuiButtonTooltip output = new GuiButtonTooltip(index, guiLeft + 8 + xDistance * j, guiTop + 47 + yDstance * i, 83, 14, "");
                this.destOutputButtons[index] = output;

                if (index < destOutputPorts.length) {
                    final Port port = destOutputPorts[index];
                    String display = port.getName();
                    boolean enabled = true;

                    if (port.getType() != ItemLinkingTool.getTargetDataType(linkingTool)) {
                        display = TextFormatting.RED + display;
                        enabled = false;
                    }

                    output.displayString = display;
                    output.enabled = enabled;
                    output.setTooltip(destOutputPorts[index].getType().toString());
                } else {
                    output.enabled = false;
                }

                this.buttonList.add(output);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        String nickname = circuitTile.getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            fontRenderer.drawString(nickname + " (" + circuitTile.getName() + ")", guiLeft + 8, guiTop + 8, 4210752);
        } else {
            fontRenderer.drawString(circuitTile.getName(), guiLeft + 8, guiTop + 21, 4210752);
        }

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
            final Port port = destOutputPorts[button.id];

            SCreateConnection packet = new SCreateConnection();
            packet.sourcePosition = circuitTile.getPosition();
            packet.sourcePort = port.getName();
            packet.destPosition = ItemLinkingTool.getTargetPosition(linkingTool);
            packet.destPort = ItemLinkingTool.getTargetPort(linkingTool);

            PacketHandler.INSTANCE.sendToServer(packet);

            SUpdateLinkingTool reset = new SUpdateLinkingTool();
            reset.type = SUpdateLinkingTool.Type.RESET;

            PacketHandler.INSTANCE.sendToServer(reset);

            mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
