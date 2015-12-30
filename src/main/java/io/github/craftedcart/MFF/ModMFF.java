package io.github.craftedcart.MFF;

import io.github.craftedcart.MFF.client.gui.GuiUtils;
import io.github.craftedcart.MFF.client.neiplugin.MFFNEIPlugin;
import io.github.craftedcart.MFF.eventhandler.PreventFFBlockBreak;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.init.*;
import io.github.craftedcart.MFF.proxy.IProxy;
import io.github.craftedcart.MFF.reference.Reference;
import io.github.craftedcart.MFF.utility.LogHelper;
import io.github.craftedcart.MFF.worldgeneration.OreGeneration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "after:NotEnoughItems")
public class ModMFF
{

    private boolean shouldLoadNEIPlugin = false;

    @Mod.Instance
    public static ModMFF instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new PreventFFBlockBreak());

        ModItems.init();
        ModBlocks.init();
        ModEntityTracker.init();
        ModCraftingRecipes.init();
        ModTileEntities.init();
        NetworkHandler.init();

        //NEI
        if (event.getSide() == Side.CLIENT && Loader.isModLoaded("NotEnoughItems")) {
            LogHelper.info("NEI found! Attempting to load NEI plugin");
            shouldLoadNEIPlugin = true;
        } else {
            LogHelper.info("NEI not found, or this is running on a server. MFF won't load the NEI plugin");
        }

        LogHelper.info("Pre-Init Complete");

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.registerRenders();
        proxy.init();

        GameRegistry.registerWorldGenerator(new OreGeneration(), 0);

        //NEI
        if (shouldLoadNEIPlugin) {
            MFFNEIPlugin.addSubsets();
            MFFNEIPlugin.addRecipeHandlers();
        }

        LogHelper.info("Init Complete");

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws FontFormatException, IOException {

        GuiUtils.init();
        LogHelper.info("Post-Init Complete");

    }

}
