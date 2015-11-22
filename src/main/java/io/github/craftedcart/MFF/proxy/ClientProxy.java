package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        ModItems.registerRenders();
        ModBlocks.registerRenders();

    }

}
