package io.github.craftedcart.MFF.client.gui.guiutils;

import net.minecraft.client.gui.GuiScreen;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public abstract class UIDisplay extends GuiScreen {

    protected UIRootComponent rootComponent = new UIRootComponent(this);
    protected boolean isFirstFrame = true;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiUtils.setup(true);

        if (isFirstFrame) {
            isFirstFrame = false;
            onInit();
        }

        GuiUtils.calcDelta();

        rootComponent.onUpdate();
    }

    public UIRootComponent getRootComponent() {
        return rootComponent;
    }

    public abstract void onInit(); //Override me!
}
