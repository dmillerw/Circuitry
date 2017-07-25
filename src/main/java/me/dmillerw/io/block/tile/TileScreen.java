package me.dmillerw.io.block.tile;

import me.dmillerw.io.block.tile.core.TileToolContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.client.gui.config.element.Element;
import me.dmillerw.io.client.gui.config.element.data.CheckBox;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public class TileScreen extends TileToolContainer {

    @Override
    public void initialize() {
        registerInput(DataType.NUMBER, "Value");
    }

    @Override
    public void getElements(LinkedList<Element> elements) {
        super.getElements(elements);

        elements.add(CheckBox.construct("Round", true).setLabel("Round"));
    }
}
