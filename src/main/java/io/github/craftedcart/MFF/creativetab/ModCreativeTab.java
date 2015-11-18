package io.github.craftedcart.MFF.creativetab;

import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModCreativeTab {

    public static final CreativeTabs MFF_TAB = new CreativeTabs(Reference.MOD_ID) {

        @Override
        public Item getTabIconItem() {
            return ModItems.prism;
        }

    };

}
