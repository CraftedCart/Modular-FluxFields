package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.client.render.blocks.TERendererFFProjector;
import io.github.craftedcart.MFF.client.render.blocks.TERendererPowerCube;
import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.tileentity.TEPowerCube;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        ModItems.registerRenders();
        ModBlocks.registerRenders();

        //TileEntity Special Renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TEPowerCube.class, new TERendererPowerCube());
        ClientRegistry.bindTileEntitySpecialRenderer(TEFFProjector.class, new TERendererFFProjector());

    }

}
