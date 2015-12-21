package io.github.craftedcart.MFF.client.neiplugin;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import io.github.craftedcart.MFF.block.ModBlock;
import io.github.craftedcart.MFF.item.ModItem;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 20/12/2015 (DD/MM/YYYY)
 */

public class MFFNEIPlugin {

    public static void addSubsets() {

        //Add MFF subset
        API.addSubset(Reference.MOD_NAME, new ItemFilter() {
            @Override
            public boolean matches(ItemStack itemStack) {
                if (itemStack.getItem() instanceof ModItem || ModBlock.getBlockFromItem(itemStack.getItem()) instanceof ModBlock) {
                    return true;
                }
                return false;
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
