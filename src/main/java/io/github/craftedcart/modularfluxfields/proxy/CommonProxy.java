package io.github.craftedcart.modularfluxfields.proxy;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public abstract class CommonProxy implements IProxy {

    public abstract void getConfig(FMLPreInitializationEvent e);

    public abstract void registerRenders();

    public void init() throws IOException, FontFormatException {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModModularFluxFields.instance, new GuiHandler());
    }

    @Override
    public void getDeps() throws IOException {
        //No-Op (Yet?)
    }
}
