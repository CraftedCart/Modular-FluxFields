package io.github.craftedcart.modularfluxfields.item;

import io.github.craftedcart.modularfluxfields.creativetab.ModCreativeTab;
import io.github.craftedcart.modularfluxfields.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModItem extends Item {

    public ModItem() {

        super();
        this.setCreativeTab(ModCreativeTab.modularfluxfields_TAB);

    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

}
