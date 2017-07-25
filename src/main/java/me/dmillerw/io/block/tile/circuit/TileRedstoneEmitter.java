package me.dmillerw.io.block.tile.circuit;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.client.gui.config.Config;
import me.dmillerw.io.client.gui.config.element.Element;
import me.dmillerw.io.client.gui.config.element.data.TextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public class TileRedstoneEmitter extends TileToolContainer {

    public static final String KEY_REDSTONE_LEVEL = "RedstoneLevel";

    @Override
    public void initialize() {
        setName("redstone_emitter");

        registerOutput(DataType.NUMBER, KEY_REDSTONE_LEVEL);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getElements(LinkedList<Element> elements) {
        super.getElements(elements);

        elements.add(TextField.of("On", 12).setLabel("Value On"));
        elements.add(TextField.of("Off", 12).setLabel("Value Off"));
    }

    @Override
    public void onConfigurationUpdate() {
        super.onConfigurationUpdate();

        updateLevel();
    }

    public void updateLevel() {
        Config config = getConfiguration();

        double valueOn = Double.parseDouble(config.getString("On", "1"));
        double valueOff = Double.parseDouble(config.getString("Off", "0"));

        updateOutput(KEY_REDSTONE_LEVEL, world.isBlockPowered(pos) ? valueOn : valueOff);
    }
}
