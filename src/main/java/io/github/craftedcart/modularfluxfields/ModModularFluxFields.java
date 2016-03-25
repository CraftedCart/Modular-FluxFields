package io.github.craftedcart.modularfluxfields;

import io.github.craftedcart.modularfluxfields.client.neiplugin.MFFNEIPlugin;
import io.github.craftedcart.modularfluxfields.eventhandler.PreventFFBlockBreak;
import io.github.craftedcart.modularfluxfields.handler.NetworkHandler;
import io.github.craftedcart.modularfluxfields.init.*;
import io.github.craftedcart.modularfluxfields.proxy.IProxy;
import io.github.craftedcart.modularfluxfields.reference.Reference;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import io.github.craftedcart.modularfluxfields.worldgeneration.OreGeneration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "after:NotEnoughItems")
public class ModModularFluxFields
{

    private boolean shouldLoadNEIPlugin = false;

    @Mod.Instance
    public static ModModularFluxFields instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {

        proxy.getDeps();

        proxy.getConfig(event);

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
            LogHelper.info("NEI not found, or this is running on a server. Modular FluxFields won't load the NEI plugin");
        }

        LogHelper.info("Pre-Init Complete");

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException, FontFormatException {

        FMLCommonHandler.instance().bus().register(new ModKeyBindings());

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
    public void postInit(FMLPostInitializationEvent event) {

        LogHelper.info("Post-Init Complete");

    }

}
