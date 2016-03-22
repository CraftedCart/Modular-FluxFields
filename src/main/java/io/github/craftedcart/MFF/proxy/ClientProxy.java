package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.client.gui.guiutils.GuiUtils;
import io.github.craftedcart.MFF.client.render.blocks.*;
import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.init.ModKeyBindings;
import io.github.craftedcart.MFF.item.ItemForceManipulator;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.tileentity.TEPowerCube;
import io.github.craftedcart.MFF.tileentity.TEPowerGenerator;
import io.github.craftedcart.MFF.tileentity.TEPowerRelay;
import io.github.craftedcart.MFF.utility.DependencyUtils;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public class ClientProxy extends CommonProxy {

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
        GuiUtils.init();
        ModKeyBindings.init();

        MinecraftForge.EVENT_BUS.register(new ItemForceManipulator.renderWorldOverlayHandler() );
    }

    @Override
    public void getDeps() throws IOException {
        super.getDeps();

        //Download Deps
        if (new File(depsPath, "slick-util.jar").exists()) {
            LogHelper.info("Found mods/CraftedCart/slick-util.jar - MFF won't download it");
        } else {
            LogHelper.info("Couldn't find mods/CraftedCart/slick-util.jar - MFF will now download the dependency from http://slick.ninjacave.com/slick-util.jar");
            DependencyUtils.downloadFileWithWindow("http://slick.ninjacave.com/slick-util.jar", new File(depsPath, "slick-util.jar"));
        }

        //Load Deps
        LaunchClassLoader loader = (LaunchClassLoader) DependencyUtils.class.getClassLoader();

        LogHelper.info("Loading slick-util.jar");
        loader.addURL(new File(depsPath, "slick-util.jar").toURI().toURL());

    }
}
