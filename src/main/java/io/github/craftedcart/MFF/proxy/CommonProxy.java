package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.handler.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public abstract class CommonProxy implements IProxy {

    File depsPath = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "mods/CraftedCart");

    public abstract void registerRenders();

    public void init() throws IOException, FontFormatException {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMFF.instance, new GuiHandler());
    }

    @Override
    public void getDeps() throws IOException {
        //No-Op (Yet?)
    }
}
