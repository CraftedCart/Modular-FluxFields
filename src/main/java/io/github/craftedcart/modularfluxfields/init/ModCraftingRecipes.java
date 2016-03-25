package io.github.craftedcart.modularfluxfields.init;

import io.github.craftedcart.modularfluxfields.crafting.CrystalConstructorRecipeHandler;
import io.github.craftedcart.modularfluxfields.crafting.CrystalRefineryRecipeHandler;
import io.github.craftedcart.modularfluxfields.reference.CrystalConstructorTimings;
import io.github.craftedcart.modularfluxfields.reference.PowerConf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CraftedCart on 15/12/2015 (DD/MM/YYYY)
 */
public class ModCraftingRecipes {

    public static void init() {

        addCraftingTableRecipes();
        addCrystalRefineryRecipes();
        addCrystalConstructorRecipes();

    }

    private static void addCraftingTableRecipes() {

        //Add Crystal Sheet x1 Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.crystalSheet,
                "ER",
                "AD",

                'E', "gemEmerald",
                'R', "gemRuby",
                'A', "gemAmethyst",
                'D', "gemDiamond"));

        //Add Crystal Sheet x4 Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crystalSheet, 4),
                "ER",
                "AD",

                'E', "gemEmerald",
                'R', ModItems.refinedRuby,
                'A', ModItems.refinedAmethyst,
                'D', "gemDiamond"));

        //Add Prism Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.prism,
                " G ",
                " G ",
                "GSG",

                'G', "blockGlassColorless",
                'S', ModItems.crystalSheet));

        //Add Power Transceiver Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.powerTransceiver,
                " R ",
                "RPR",
                "IBI",

                'R', "dustRedstone",
                'P', ModItems.prism,
                'I', "ingotIron",
                'B', "blockIron"));

        //Add Solar (x1) Power Generator Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.solarPowerGenerator1,
                " P ",
                "ITI",
                "RBR",

                'P', ModItems.prism,
                'I', "ingotIron",
                'T', ModItems.powerTransceiver,
                'R', "dustRedstone",
                'B', "blockRedstone"));
    }

    private static void addCrystalRefineryRecipes() {

        //Add Refined Amethyst Recipe
        CrystalRefineryRecipeHandler.addRecipe(ModItems.rawAmethyst, new ItemStack(ModItems.refinedAmethyst), PowerConf.crystalRefineryBaseTime);
        //Add Refined Ruby Recipe
        CrystalRefineryRecipeHandler.addRecipe(ModItems.rawRuby, new ItemStack(ModItems.refinedRuby), PowerConf.crystalRefineryBaseTime);

    }

    private static void addCrystalConstructorRecipes() {

        //Add Alurite Base Recipe
        CrystalConstructorRecipeHandler.addRecipe(2, 2, new Item[]{
                ModItems.aluriteIngot, null          ,
                null                 , Items.redstone
        }, new ItemStack(ModItems.aluriteBase), CrystalConstructorTimings.aluriteBase);

        //Add Crystal Sheet x16 Recipe
        CrystalConstructorRecipeHandler.addRecipe(2, 2, new Item[]{
                Items.emerald           , ModItems.refinedRuby,
                ModItems.refinedAmethyst, Items.diamond
        }, new ItemStack(ModItems.crystalSheet, 16), CrystalConstructorTimings.crystalSheet);

        //Add Solar Power Generator x8 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator1), Item.getItemFromBlock(ModBlocks.solarPowerGenerator1), Item.getItemFromBlock(ModBlocks.solarPowerGenerator1),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator1), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator1),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator1), Item.getItemFromBlock(ModBlocks.solarPowerGenerator1), Item.getItemFromBlock(ModBlocks.solarPowerGenerator1),
        }, new ItemStack(ModBlocks.solarPowerGenerator8), CrystalConstructorTimings.combineSolarGenerators);

        //Add Solar Power Generator x64 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator8), Item.getItemFromBlock(ModBlocks.solarPowerGenerator8), Item.getItemFromBlock(ModBlocks.solarPowerGenerator8),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator8), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator8),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator8), Item.getItemFromBlock(ModBlocks.solarPowerGenerator8), Item.getItemFromBlock(ModBlocks.solarPowerGenerator8),
        }, new ItemStack(ModBlocks.solarPowerGenerator64), CrystalConstructorTimings.combineSolarGenerators);

        //Add Solar Power Generator x512 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator64), Item.getItemFromBlock(ModBlocks.solarPowerGenerator64), Item.getItemFromBlock(ModBlocks.solarPowerGenerator64),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator64), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator64),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator64), Item.getItemFromBlock(ModBlocks.solarPowerGenerator64), Item.getItemFromBlock(ModBlocks.solarPowerGenerator64),
        }, new ItemStack(ModBlocks.solarPowerGenerator512), CrystalConstructorTimings.combineSolarGenerators);

        //Add Solar Power Generator x4096 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator512), Item.getItemFromBlock(ModBlocks.solarPowerGenerator512), Item.getItemFromBlock(ModBlocks.solarPowerGenerator512),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator512), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator512),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator512), Item.getItemFromBlock(ModBlocks.solarPowerGenerator512), Item.getItemFromBlock(ModBlocks.solarPowerGenerator512),
        }, new ItemStack(ModBlocks.solarPowerGenerator4096), CrystalConstructorTimings.combineSolarGenerators);

        //Add Solar Power Generator x32768 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096), Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096), Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096), Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096), Item.getItemFromBlock(ModBlocks.solarPowerGenerator4096),
        }, new ItemStack(ModBlocks.solarPowerGenerator32768), CrystalConstructorTimings.combineSolarGenerators);

        //Add Solar Power Generator x262144 Recipe
        CrystalConstructorRecipeHandler.addRecipe(3, 3, new Item[]{
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768), Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768), Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768), Item.getItemFromBlock(ModBlocks.powerCube)           , Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768),
                Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768), Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768), Item.getItemFromBlock(ModBlocks.solarPowerGenerator32768),
        }, new ItemStack(ModBlocks.solarPowerGenerator262144), CrystalConstructorTimings.combineSolarGenerators);

    }

}
