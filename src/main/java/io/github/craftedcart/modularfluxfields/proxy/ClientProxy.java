package io.github.craftedcart.modularfluxfields.proxy;

import io.github.craftedcart.modularfluxfields.client.render.blocks.*;
import io.github.craftedcart.modularfluxfields.handler.ConfigurationHandler;
import io.github.craftedcart.modularfluxfields.init.ModBlocks;
import io.github.craftedcart.modularfluxfields.init.ModItems;
import io.github.craftedcart.modularfluxfields.init.ModKeyBindings;
import io.github.craftedcart.modularfluxfields.init.ModModels;
import io.github.craftedcart.modularfluxfields.item.ItemForceManipulator;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerCube;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerGenerator;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerRelay;
import io.github.craftedcart.mcliquidui.util.DependencyUtils;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */
public class ClientProxy extends CommonProxy {

    private File depsPath = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "mods/CraftedCart");

    @Override
    public void getConfig(FMLPreInitializationEvent e) {
        ConfigurationHandler.initCommon(e.getSuggestedConfigurationFile());
        ConfigurationHandler.initClient();
    }

    @Override
    public void registerRenders() {

        ShaderUtils.init();

        ModItems.registerRenders();
        ModBlocks.registerRenders();

        //TileEntity Special Renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TEPowerCube.class, new TERendererPowerCube());
        ClientRegistry.bindTileEntitySpecialRenderer(TEFFProjector.class, new TERendererFFProjector());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPowerGenerator.class, new TERendererPowerGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPowerRelay.class, new TERendererPowerRelay());

    }

    @Override
    public void init() throws IOException, FontFormatException {
        super.init();

        LogHelper.info(String.format("OpenGL Version: %s", GL11.glGetString(GL11.GL_VERSION)));

        if (!Loader.isModLoaded("MCLiquidUI")) {
            LogHelper.fatal("You're missing MCLiquidUI - Download that first before launching Minecraft!");
        }

        ModKeyBindings.init();
        ModModels.init();

        MinecraftForge.EVENT_BUS.register(new ItemForceManipulator.renderWorldOverlayHandler() );
    }

}
