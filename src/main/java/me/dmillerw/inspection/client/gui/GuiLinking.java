package me.dmillerw.inspection.client.gui;

import me.dmillerw.inspection.block.tile.core.TileToolContainer;
import me.dmillerw.inspection.circuit.data.DataType;
import me.dmillerw.inspection.circuit.data.Port;
import me.dmillerw.inspection.lib.ModInfo;
import me.dmillerw.inspection.network.PacketHandler;
import me.dmillerw.inspection.network.packet.server.SCreateConnection;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Collection;

/**
 * @author dmillerw
 */
public class GuiLinking extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/linking_debug.png");

    private static final int WIDTH = 251;
    private static final int HEIGHT = 222;

    private TileToolContainer circuitTile;

    private int destSelectedInput = -1;
    private Port[] destInputPorts = new Port[0];
    private GuiButtonExt[] destInputButtons = new GuiButtonExt[0];

    private int sourceSelected;
    private BlockPos[] potentialSources = new BlockPos[0];
    private String[] sourceOutputPorts = new String[0];
    private GuiButtonExt[] sourceOutputButtons = new GuiButtonExt[0];

    private GuiButtonExt sourceButtonPrev;
    private GuiButtonExt sourceButtonNext;

    private int guiLeft;
    private int guiTop;

    public GuiLinking(TileToolContainer circuitTile) {
        this.circuitTile = circuitTile;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;

        final int distance = 19;

        this.destInputPorts = circuitTile.inputs.values().toArray(new Port[0]);
        this.destInputButtons = new GuiButtonExt[9];

        for (int i = 0; i < 9; i++) {
            GuiButtonExt button = new GuiButtonExt(i, guiLeft + 8, guiTop + 31 + distance * i, 101, 14, "");
            this.destInputButtons[i] = button;

            if (i < destInputPorts.length) {
                button.displayString = destInputPorts[i].name;
            } else {
                button.enabled = false;
            }

            this.buttonList.add(button);
        }

        if (GuiConfigurator.networkMembers.size() > 0) {
            sourceSelected = 0;
            potentialSources = GuiConfigurator.networkMembers.keySet().toArray(new BlockPos[0]);

            Collection<Pair<String, DataType>> outputs = GuiConfigurator.networkMembers.get(potentialSources[sourceSelected]);
            sourceOutputPorts = new String[outputs.size()];
            sourceOutputButtons = new GuiButtonExt[8];

            int index = 0;
            for (Pair<String, DataType> output : outputs) {
                sourceOutputPorts[index] = output.getKey();
                index++;
            }

            for (int i = 0; i < 8; i++) {
                GuiButtonExt button = new GuiButtonExt(9 + i, guiLeft + 141, guiTop + 68 + distance * i, 101, 14, "");
                this.sourceOutputButtons[i] = button;

                if (i < sourceOutputPorts.length) {
                    button.displayString = sourceOutputPorts[i];
                } else {
                    button.enabled = false;
                }

                this.buttonList.add(button);
            }
        }

        this.buttonList.add(sourceButtonPrev = new GuiButtonExt(18, guiLeft + 141, guiTop + 49, 40, 14, "PREV"));
        this.buttonList.add(sourceButtonNext = new GuiButtonExt(19, guiLeft + 204, guiTop + 49, 40, 14, "NEXT"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        if (potentialSources.length > 0) {
            BlockPos pos = potentialSources[sourceSelected];
            if (pos != null) {
                TileToolContainer tile = (TileToolContainer) mc.world.getTileEntity(pos);
                if (tile != null) {
                    fontRendererObj.drawString(tile.getName(), guiLeft + 182, guiTop + 8, 0xFFFFFF);
                    fontRendererObj.drawString("(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")", guiLeft + 182, guiTop + 28, 0xFFFFFF);
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id < 9) {
            destSelectedInput = button.id;

            updateButtonDisplays();
        } else if (button.id >= 9 && button.id < 18) {
            SCreateConnection packet = new SCreateConnection();
            packet.sourcePosition = potentialSources[sourceSelected];
            packet.sourcePort = sourceOutputPorts[button.id - 9];
            packet.destPosition = circuitTile.getPos();
            packet.destPort = destInputPorts[destSelectedInput].name;

            PacketHandler.INSTANCE.sendToServer(packet);

            mc.displayGuiScreen(null);
        } else if (button.id == 18 || button.id == 19) {
            if (button.id == 18) {
                if (sourceSelected > 0) sourceSelected--;
                else sourceSelected = potentialSources.length - 1;
            } else if (button.id == 19) {
                if (sourceSelected < potentialSources.length - 1) sourceSelected++;
                else sourceSelected = 0;
            }

            updateSourceInfo();
        }
    }

    private void updateSourceInfo() {
        if (potentialSources.length <= 0)
            return;

        Collection<Pair<String, DataType>> outputs = GuiConfigurator.networkMembers.get(potentialSources[sourceSelected]);
        sourceOutputPorts = new String[outputs.size()];

        int index = 0;
        for (Pair<String, DataType> output : outputs) {
            sourceOutputPorts[index] = output.getKey();
            index++;
        }

        for (int i = 0; i < sourceOutputButtons.length; i++) {
            GuiButtonExt button = sourceOutputButtons[i];

            if (i < sourceOutputPorts.length) {
                button.displayString = sourceOutputPorts[i];
            } else {
                button.enabled = false;
            }

            this.buttonList.add(button);
        }
    }

    private void updateButtonDisplays() {
        for (int i = 0; i < 9; i++) {
            GuiButton button = this.destInputButtons[i];
            if (i < destInputPorts.length) {
                if (i == destSelectedInput) {
                    button.displayString = " --> " + destInputPorts[i].name + " <-- ";
                } else {
                    button.displayString = destInputPorts[i].name;
                }
            } else {
                button.enabled = false;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
