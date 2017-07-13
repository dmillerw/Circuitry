package me.dmillerw.io.client.gui;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class GuiDebugger extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/debugger_debug.png");

    private static final int WIDTH = 221;
    private static final int HEIGHT = 235;

    private TileToolContainer circuitTile;

    private String[] outputs = new String[0];
    private String[] inputs = new String[0];

    private int guiLeft;
    private int guiTop;

    public GuiDebugger(TileToolContainer circuitTile) {
        this.circuitTile = circuitTile;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.outputs = circuitTile.outputs.keySet().toArray(new String[0]);
        this.inputs = circuitTile.inputs.keySet().toArray(new String[0]);

        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        fontRenderer.drawString("OUTPUTS: " + circuitTile.getName(), guiLeft + 8, guiTop + 10, 4210752);
        fontRenderer.drawString("INPUTS: " + circuitTile.getName(), guiLeft + 8, guiTop + 132, 4210752);

        final int distance = 20;

        int outStartX = guiLeft + 10;
        int outStartY = guiTop + 25;
        int inStartX = guiLeft + 10;
        int inStartY = guiTop + 147;

        for (int i=0; i<4; i++) {
            if (i < outputs.length) {
                Port port = circuitTile.getOutput(outputs[i]);

                fontRenderer.drawString(port.getName() + " - [" + port.getType().toString() + "]", outStartX, outStartY + distance * i, i % 2 == 0 ? 4210752 : 0xFFFFFF);
                fontRenderer.drawString(port.getValue().toString(), outStartX, outStartY + 10 + distance * i, i % 2 == 0 ? 4210752 : 0xFFFFFF);
            }

            if (i < inputs.length) {
                Port port = circuitTile.getInput(inputs[i]);

                fontRenderer.drawString(port.getName() + " - [" + port.getType().toString() + "]", inStartX, inStartY + distance * i, i % 2 == 0 ? 4210752 : 0xFFFFFF);
                fontRenderer.drawString(port.getValue().toString(), inStartX, inStartY + 10 + distance * i, i % 2 == 0 ? 4210752 : 0xFFFFFF);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
