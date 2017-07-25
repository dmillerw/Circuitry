package me.dmillerw.io.client.gui.config;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class Config {

    private NBTTagCompound backingTag;

    public Config(NBTTagCompound backingTag) {
        this.backingTag = backingTag;
    }

    public NBTTagCompound getTag() {
        return backingTag;
    }

    public Config setRawTag(String key, NBTBase tag) {
        this.backingTag.setTag(key, tag);
        return this;
    }

    public NBTBase getRawTag(String key) {
        return this.backingTag.getTag(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return hasValue(key) ? backingTag.getBoolean(key) : defaultValue;
    }

    public boolean hasValue(String key) {
        return backingTag != null && backingTag.hasKey(key);
    }

    public String getString(String key, String defaultValue) {
        return hasValue(key) ? backingTag.getString(key) : defaultValue;
    }
}
