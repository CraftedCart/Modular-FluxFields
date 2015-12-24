package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.item.*;
import io.github.craftedcart.MFF.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModItems {

    //Reference Items
    public static final ModItem prism = new ItemPrism();
    public static final ModItem crystalSheet = new ItemCrystalSheet();
    public static final ModItem aluriteIngot = new ItemAluriteIngot();
    public static final ModItem aluriteBase = new ItemAluriteBase();
    public static final ModItem powerTransceiver = new ItemPowerTransceiver();
    //Crystals
    public static final ModItem refinedAmethyst = new ItemRefinedAmethyst();
    public static final ModItem rawAmethyst = new ItemRawAmethyst();
    public static final ModItem refinedRuby = new ItemRefinedRuby();
    public static final ModItem rawRuby = new ItemRawRuby();
    //Upgrades
    public static final ModItem speedUpgrade = new ItemSpeedUpgrade();
    public static final ModItem efficiencyUpgrade = new ItemEfficiencyUpgrade();

    public static void init() {

        //Register Items
        GameRegistry.registerItem(prism, Names.ItemPrism);
        GameRegistry.registerItem(crystalSheet, Names.ItemCrystalSheet);
        GameRegistry.registerItem(aluriteIngot, Names.ItemAluriteIngot);
        OreDictionary.registerOre("ingotAlurite", aluriteIngot);
        GameRegistry.registerItem(aluriteBase, Names.ItemAluriteBase);
        GameRegistry.registerItem(powerTransceiver, Names.ItemPowerTransceiver);
        //Crystals
        GameRegistry.registerItem(refinedAmethyst, Names.ItemRefinedAmethyst);
        GameRegistry.registerItem(rawAmethyst, Names.ItemRawAmethyst);
        OreDictionary.registerOre("gemAmethyst", rawAmethyst);
        GameRegistry.registerItem(refinedRuby, Names.ItemRefinedRuby);
        GameRegistry.registerItem(rawRuby, Names.ItemRawRuby);
        OreDictionary.registerOre("gemRuby", rawRuby);
        //Upgrades
        GameRegistry.registerItem(speedUpgrade, Names.ItemSpeedUpgrade);
        GameRegistry.registerItem(efficiencyUpgrade, Names.ItemEfficiencyUpgrade);

    }

    public static void registerRenders() {

        //Register Item Renders
        registerRender(prism);
        registerRender(crystalSheet);
        registerRender(aluriteIngot);
        registerRender(aluriteBase);
        registerRender(powerTransceiver);
        //Crystals
        registerRender(refinedAmethyst);
        registerRender(rawAmethyst);
        registerRender(refinedRuby);
        registerRender(rawRuby);
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
