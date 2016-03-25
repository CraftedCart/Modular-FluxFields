package io.github.craftedcart.modularfluxfields.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by CraftedCart on 25/03/2016 (DD/MM/YYYY)
 */
public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraft) {
        //No-Op
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiMFFSettings.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement runtimeOptionCategoryElement) {
        return null;
    }

}
