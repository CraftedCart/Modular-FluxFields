package io.github.craftedcart.modularfluxfields.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */
public interface IProxy {

    void getConfig(FMLPreInitializationEvent e);
    void registerRenders();
    void init() throws IOException, FontFormatException;
    void getDeps() throws IOException;

}
