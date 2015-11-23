package io.github.craftedcart.MFF;

import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.init.ModTileEntities;
import io.github.craftedcart.MFF.proxy.IProxy;
import io.github.craftedcart.MFF.reference.Reference;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class ModMFF
{

    @Mod.Instance
    public static ModMFF instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        ModItems.init();
        ModBlocks.init();
        ModTileEntities.init();
        NetworkHandler.init();

        LogHelper.info("Pre-Init Complete");

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.registerRenders();
        proxy.init();

        LogHelper.info("Init Complete");

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        LogHelper.info("Post-Init Complete");

    }

}
