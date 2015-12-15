package io.github.craftedcart.MFF.init;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CraftedCart on 15/12/2015 (DD/MM/YYYY)
 */

public class ModCraftingRecipes {

    public static void init() {

        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.crystalSheet, "ER", "AD", 'E', Items.emerald, 'R', ModItems.refinedRuby, 'A', ModItems.refinedAmethyst, 'D', Items.diamond));

    }

}
