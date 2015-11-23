package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.handler.GuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public abstract class CommonProxy implements IProxy {

    public abstract void registerRenders();

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMFF.instance, new GuiHandler());
    }

}
