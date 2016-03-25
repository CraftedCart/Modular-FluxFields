package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.modularfluxfields.container.ContainerCrystalConstructor;
import io.github.craftedcart.modularfluxfields.reference.PowerConf;
import io.github.craftedcart.modularfluxfields.tileentity.TECrystalConstructor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 14/12/2015 (DD/MM/YYYY)
 */

public class GuiCrystalConstructor extends GuiContainer {

    private IInventory playerInv;
    private TECrystalConstructor te;

    public GuiCrystalConstructor(IInventory playerInv, TECrystalConstructor te) {
        super(new ContainerCrystalConstructor(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

        this.xSize = 205;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("modularfluxfields:textures/gui/container/crystalConstructor.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        //Draw power value
        double power = te.power;
        double maxPower = this.te.maxPower;
        double powerUsage = this.te.powerUsage;

        drawRect(this.guiLeft, this.guiTop - 24, this.guiLeft + xSize, this.guiTop - 22, 0xFF212121);
        drawRect(this.guiLeft, this.guiTop - 24, (int) (this.guiLeft + (double) xSize * power / maxPower), this.guiTop - 22, 0xFF2196F3);

        this.fontRendererObj.drawString(
                String.format("%s: %07.2f / %04.0f %s",
                        StatCollector.translateToLocal("gui.modularfluxfields:power"), power, maxPower, StatCollector.translateToLocal("gui.modularfluxfields:fe")),
                guiLeft, guiTop - 34, 0xFAFAFA, false);

        //Draw progress value
        int progress = te.progress;
        int maxProgress = te.maxProgress;
        double powerMultiplier = te.powerMultiplier; //Needed to help calculate time remaining

        final float ticksLeft;
        if (power >= PowerConf.crystalConstructorUsage * powerMultiplier) {
            ticksLeft = (progress == 0 || maxProgress - progress - partialTicks < 0) ? maxProgress : maxProgress - progress - partialTicks;
        } else {
            ticksLeft = maxProgress - progress;
        }

        drawRect(this.guiLeft, this.guiTop - 8, this.guiLeft + xSize, this.guiTop - 6, 0xFF212121);
        if (maxProgress != 0) {
            drawRect(this.guiLeft, this.guiTop - 8, (int) (this.guiLeft + xSize * (1 - ticksLeft / maxProgress)), this.guiTop - 6, 0xFF4CAF50);
        }

        final int minsLeft = (int) Math.floor(ticksLeft / 20 / 60);
        final int secsLeft = (int) Math.floor(ticksLeft / 20 - minsLeft * 60);
        final int partSecsLeft = (int) (Math.abs(Math.floor(ticksLeft / 20) - (ticksLeft / 20)) * 100);

        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.modularfluxfields:remaining") + ": " + String.format("%02d : %02d . %02d", minsLeft, secsLeft, partSecsLeft),
                this.guiLeft, this.guiTop - 18, 0xFAFAFA, false);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);

    }

}
