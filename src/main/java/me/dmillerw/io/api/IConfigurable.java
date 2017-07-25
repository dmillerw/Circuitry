package me.dmillerw.io.api;

import me.dmillerw.io.client.gui.config.Config;
import me.dmillerw.io.client.gui.config.element.Element;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;

/**
 * @author dmillerw
 */
public interface IConfigurable {

    @SideOnly(Side.CLIENT)
    void getElements(LinkedList<Element> elements);

    Config getConfiguration();

    void onConfigurationUpdate(NBTTagCompound tag);
}
