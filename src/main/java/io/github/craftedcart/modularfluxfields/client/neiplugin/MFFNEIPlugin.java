package io.github.craftedcart.modularfluxfields.client.neiplugin;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import io.github.craftedcart.modularfluxfields.block.ModBlock;
import io.github.craftedcart.modularfluxfields.item.ModItem;
import io.github.craftedcart.modularfluxfields.reference.Reference;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 20/12/2015 (DD/MM/YYYY)
 */
public class MFFNEIPlugin {

    public static void addSubsets() {

        //Add Modular FluxFields subset
        API.addSubset(Reference.MOD_NAME, new ItemFilter() {
            @Override
            public boolean matches(ItemStack itemStack) {
                return itemStack.getItem() instanceof ModItem || ModBlock.getBlockFromItem(itemStack.getItem()) instanceof ModBlock;
            }
        });

    }

    public static void addRecipeHandlers() {

        //Add Crystal Constructor crafting handler
        API.registerRecipeHandler(new NEICrystalConstructorCraftingHandler());
        API.registerUsageHandler(new NEICrystalConstructorCraftingHandler());
        //Add Crystal Refinery crafting handler
        API.registerRecipeHandler(new NEICrystalRefineryCraftingHandler());
        API.registerUsageHandler(new NEICrystalRefineryCraftingHandler());

    }

}
