package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.block.BlockFFProjector;
import io.github.craftedcart.MFF.block.BlockForcefield;
import io.github.craftedcart.MFF.block.BlockPowerSphere;
import io.github.craftedcart.MFF.block.ModBlock;
import io.github.craftedcart.MFF.client.render.blocks.TERendererPowerSphere;
import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TEPowerSphere;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModBlocks {

    //Reference Blocks
    public static final ModBlock forcefield = new BlockForcefield();
    public static final ModBlock ffProjector = new BlockFFProjector();
    public static final ModBlock powerSphere = new BlockPowerSphere();


    public static void init() {

        //Register Blocks
        GameRegistry.registerBlock(forcefield, Names.BlockForcefield);
        GameRegistry.registerBlock(ffProjector, Names.BlockFFProjector);
        GameRegistry.registerBlock(powerSphere, Names.BlockPowerSphere);
        ClientRegistry.bindTileEntitySpecialRenderer(TEPowerSphere.class, new TERendererPowerSphere());

    }

    public static void registerRenders() {

        //Register Block Renders
        registerRender(forcefield);
        registerRender(ffProjector);
        registerRender(powerSphere);

    }

    private static void registerRender(Block block) {

        //Register Block Render
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                Item.getItemFromBlock(block),
                0,
                new ModelResourceLocation(block.getUnlocalizedName().substring(5),
                "inventory"));

    }

}
