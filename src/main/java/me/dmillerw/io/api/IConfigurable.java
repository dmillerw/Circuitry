package me.dmillerw.io.api;

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
    public void getElements(LinkedList<Element> elements);

    public NBTTagCompound getConfiguration();
    public void handleUpdate(NBTTagCompound tag);
}
