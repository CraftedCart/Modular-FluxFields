package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerCrystalConstructor;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TECrystalConstructor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.lang.reflect.Field;

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

        this.drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/crystalConstructor.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        try {

            //Draw power value
            Field fPower = te.getClass().getField("power");
            double power = fPower.getDouble(te);

            this.drawRect(this.guiLeft - 24, this.guiTop - 24, this.guiLeft + xSize + 24, this.guiTop - 22, 0xFF212121);
            this.drawRect(this.guiLeft - 24, this.guiTop - 24, (int) (this.guiLeft - 24 + (double) (xSize + 48) * power / PowerConf.crystalConstructorMaxPower), this.guiTop - 22, 0xFF2196F3);

            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:power") + ": " + String.format("%09.2f", power) + " / " + String.format("%09.2f", PowerConf.crystalConstructorMaxPower) + " FE",
                    this.guiLeft - 24, this.guiTop - 34, 0xFAFAFA, false);

            //Draw progress value
            Field fProgress = te.getClass().getField("progress");
            int progress = fProgress.getInt(te);
            Field fMaxProgress = te.getClass().getField("maxProgress");
            int maxProgress = fMaxProgress.getInt(te);
            Field fPowerMultiplier = te.getClass().getField("powerMultiplier");
            double powerMultiplier = fPowerMultiplier.getDouble(te);

            final float ticksLeft;
            if (power >= PowerConf.crystalConstructorUsage * powerMultiplier) {
                ticksLeft = (progress == 0 || maxProgress - progress - partialTicks < 0) ? maxProgress : maxProgress - progress - partialTicks;
            } else {
                ticksLeft = maxProgress - progress;
            }

            this.drawRect(this.guiLeft - 24, this.guiTop - 8, this.guiLeft + xSize + 24, this.guiTop - 6, 0xFF212121);
            if (maxProgress != 0) {
                this.drawRect(this.guiLeft - 24, this.guiTop - 8, (int) (this.guiLeft - 24 + (xSize + 48) * (1 - ticksLeft / maxProgress)), this.guiTop - 6, 0xFF4CAF50);
            }

            final int minsLeft = (int) Math.floor(ticksLeft / 20 / 60);
            final int secsLeft = (int) Math.floor(ticksLeft / 20 - minsLeft * 60);
            final int partSecsLeft = (int) (Math.abs(Math.floor(ticksLeft / 20) - (ticksLeft / 20)) * 100);

            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:remaining") + ": " + String.format("%02d : %02d . %02d", minsLeft, secsLeft, partSecsLeft),
                    this.guiLeft - 24, this.guiTop - 18, 0xFAFAFA, false);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);

    }

}
