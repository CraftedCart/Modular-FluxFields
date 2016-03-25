package io.github.craftedcart.modularfluxfields.proxy;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.GuiUtils;
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
import io.github.craftedcart.modularfluxfields.utility.DependencyUtils;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        ConfigurationHandler.initClient(e.getSuggestedConfigurationFile());
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

        GuiUtils.init();
        ModKeyBindings.init();
        ModModels.init();

        MinecraftForge.EVENT_BUS.register(new ItemForceManipulator.renderWorldOverlayHandler() );
    }

    @Override
    public void getDeps() throws IOException {
        super.getDeps();

        //Download Deps
        if (new File(depsPath, "slick-util.jar").exists()) {
            LogHelper.info("Found mods/CraftedCart/slick-util.jar - modularfluxfields won't download it");
        } else {
            LogHelper.info("Couldn't find mods/CraftedCart/slick-util.jar - modularfluxfields will now download the dependency from http://slick.ninjacave.com/slick-util.jar");
            DependencyUtils.downloadFileWithWindow("http://slick.ninjacave.com/slick-util.jar", new File(depsPath, "slick-util.jar"));
        }

        //Load Deps
        LaunchClassLoader loader = (LaunchClassLoader) DependencyUtils.class.getClassLoader();

        LogHelper.info("Loading slick-util.jar");
        loader.addURL(new File(depsPath, "slick-util.jar").toURI().toURL());

    }
}
