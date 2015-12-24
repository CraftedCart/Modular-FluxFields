package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.block.*;
import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModBlocks {

    //Reference Blocks
    public static final ModBlock forcefield = new BlockForcefield();
    public static final ModBlock ffProjector = new BlockFFProjector();
    public static final ModBlock powerCube = new BlockPowerCube();
    public static final ModBlock crystalRefinery = new BlockCrystalRefinery();
    public static final ModBlock crystalConstructor = new BlockCrystalConstructor();
    //Ores
    public static final ModBlock oreAmethyst = new BlockOreAmethyst();
    public static final ModBlock oreRuby = new BlockOreRuby();
    //Generators
    public static final ModBlock creativePowerGenerator = new BlockCreativePowerGenerator();
    public static final ModBlock solarPowerGenerator1 = new BlockSolarPowerGenerator1();
    public static final ModBlock solarPowerGenerator8 = new BlockSolarPowerGenerator8();
    public static final ModBlock solarPowerGenerator64 = new BlockSolarPowerGenerator64();
    public static final ModBlock solarPowerGenerator512 = new BlockSolarPowerGenerator512();
    public static final ModBlock solarPowerGenerator4096 = new BlockSolarPowerGenerator4096();
    public static final ModBlock solarPowerGenerator32768 = new BlockSolarPowerGenerator32768();
    public static final ModBlock solarPowerGenerator262144 = new BlockSolarPowerGenerator262144();


    public static void init() {

        //Register Blocks
        GameRegistry.registerBlock(forcefield, Names.BlockForcefield);
        GameRegistry.registerBlock(ffProjector, Names.BlockFFProjector);
        GameRegistry.registerBlock(powerCube, Names.BlockPowerCube);
        GameRegistry.registerBlock(crystalRefinery, Names.BlockCrystalRefinery);
        GameRegistry.registerBlock(crystalConstructor, Names.BlockCrystalConstructor);
        //Ores
        GameRegistry.registerBlock(oreAmethyst, Names.BlockOreAmethyst);
        OreDictionary.registerOre(Names.BlockOreAmethyst, new ItemStack(oreAmethyst));
        GameRegistry.registerBlock(oreRuby, Names.BlockOreRuby);
        OreDictionary.registerOre(Names.BlockOreRuby, new ItemStack(oreRuby));
        //Generators
        GameRegistry.registerBlock(creativePowerGenerator, Names.BlockCreativePowerGenerator);
        GameRegistry.registerBlock(solarPowerGenerator1, Names.BlockSolarPowerGenerator1);
        GameRegistry.registerBlock(solarPowerGenerator8, Names.BlockSolarPowerGenerator8);
        GameRegistry.registerBlock(solarPowerGenerator64, Names.BlockSolarPowerGenerator64);
        GameRegistry.registerBlock(solarPowerGenerator512, Names.BlockSolarPowerGenerator512);
        GameRegistry.registerBlock(solarPowerGenerator4096, Names.BlockSolarPowerGenerator4096);
        GameRegistry.registerBlock(solarPowerGenerator32768, Names.BlockSolarPowerGenerator32768);
        GameRegistry.registerBlock(solarPowerGenerator262144, Names.BlockSolarPowerGenerator262144);

    }

    public static void registerRenders() {

        //Register Block Renders
        registerRender(forcefield);
        registerBlockRenderAsItem(ffProjector, Names.BlockFFProjector);
        registerBlockRenderAsItem(powerCube, Names.BlockPowerCube);
        registerRender(crystalRefinery);
        registerRender(crystalConstructor);
        //Ores
        registerRender(oreAmethyst);
        registerRender(oreRuby);

    }

    private static void registerRender(Block block) {

        //Register Block Render
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                Item.getItemFromBlock(block),
                0,
                new ModelResourceLocation(block.getUnlocalizedName().substring(5),
                "inventory"));

    }

    private static void registerBlockRenderAsItem(ModBlock block, String itemID) {

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + itemID, "inventory"));

    }

}
