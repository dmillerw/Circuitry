package me.dmillerw.io.circuit.gate.util;

import me.dmillerw.io.block.tile.TileGateContainer;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.gate.BaseGate;
import me.dmillerw.io.client.gui.config.element.Element;
import me.dmillerw.io.client.gui.config.element.Label;
import me.dmillerw.io.client.gui.config.element.data.TextField;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public class GateConstantValue extends BaseGate {

    public GateConstantValue() {
        super("constant_value", Category.ARITHMETIC);
    }

    @Override
    public void initialize(TileGateContainer parentTile) {
        parentTile.registerOutput(DataType.NUMBER, "Out");
    }

    @Override
    public void addElements(LinkedList<Element> elements) {
        elements.add(TextField.of("Value", 12).setLabel("Value"));
    }

    @Override
    public void calculateOutput(TileGateContainer parentTile) {
        String value = parentTile.getConfiguration().getString("Value");
        if (!value.isEmpty()) {
            parentTile.updateOutput("Out", Double.parseDouble(value));
        } else {
            parentTile.updateOutput("Out", 0);
        }
    }
}
