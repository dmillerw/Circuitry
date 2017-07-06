package me.dmillerw.io.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class ModTab extends CreativeTabs {

    public static final ModTab TAB = new ModTab(ModInfo.MOD_ID);

    public ModTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.STICK);
    }
}
