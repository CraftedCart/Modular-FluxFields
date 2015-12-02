package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;

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
