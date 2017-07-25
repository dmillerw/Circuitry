package me.dmillerw.io.client.gui.config.element.data;

import me.dmillerw.io.client.gui.config.element.Element;
import net.minecraft.nbt.NBTBase;


/**
 * @author dmillerw
 */
public abstract class ElementWithValue<V> extends Element {

    public final String key;

    public ElementWithValue(String key) {
        this.key = key;
    }

    public abstract V getValue();

    public abstract void loadFromNBTTag(NBTBase tag);
    public abstract NBTBase toNBTTag();
}
