package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerCrystalRefinery;
import io.github.craftedcart.MFF.tileentity.TECrystalRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class GuiCrystalRefinery extends GuiContainer {

    private IInventory playerInv;
    private TECrystalRefinery te;

    public GuiCrystalRefinery(IInventory playerInv, TECrystalRefinery te) {
        super(new ContainerCrystalRefinery(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/crystalRefinery.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }

}
