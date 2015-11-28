package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.item.*;
import io.github.craftedcart.MFF.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModItems {

    //Reference Items
    public static final ModItem prism = new ItemPrism();
    //Crystals
    public static final ModItem refinedAmethyst = new ItemRefinedAmethyst();
    public static final ModItem rawAmethyst = new ItemRawAmethyst();
    //Upgrades
    public static final ModItem speedUpgrade = new ItemSpeedUpgrade();
    public static final ModItem efficiencyUpgrade = new ItemEfficiencyUpgrade();

    public static void init() {

        //Register Items
        GameRegistry.registerItem(prism, Names.ItemPrism);
        //Crystals
        GameRegistry.registerItem(refinedAmethyst, Names.ItemRefinedAmethyst);
        GameRegistry.registerItem(rawAmethyst, Names.ItemRawAmethyst);
        //Upgrades
        GameRegistry.registerItem(speedUpgrade, Names.ItemSpeedUpgrade);
        GameRegistry.registerItem(efficiencyUpgrade, Names.ItemEfficiencyUpgrade);

    }

    public static void registerRenders() {

        //Register Item Renders
        registerRender(prism);
        //Crystals
        registerRender(refinedAmethyst);
        registerRender(rawAmethyst);
        //Upgrades
        registerRender(speedUpgrade);
        registerRender(efficiencyUpgrade);

    }

    public static void registerRender(Item item) {

        //Register Item Render
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                item,
                0,
                new ModelResourceLocation(item.getUnlocalizedName().substring(5),
                "inventory"));

    }

}
