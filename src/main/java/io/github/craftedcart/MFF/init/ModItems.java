package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.item.ItemPrism;
import io.github.craftedcart.MFF.item.ModItem;
import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.reference.Reference;
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

    public static void init() {

        //Register Items
        GameRegistry.registerItem(prism, Names.ItemPrism);

    }

    public static void registerRenders() {

        //Register Item Renders
        registerRender(prism);

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
