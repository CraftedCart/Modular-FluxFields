package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerCrystalRefinery;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TECrystalRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.lang.reflect.Field;

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

        this.xSize = 205;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/crystalRefinery.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        try {

            //Draw power value
            Field fPower = te.getClass().getField("power");
            double power = fPower.getDouble(te);

            this.drawRect(this.guiLeft - 24, this.guiTop - 24, this.guiLeft + xSize + 24, this.guiTop - 22, 0xFF212121);
            this.drawRect(this.guiLeft - 24, this.guiTop - 24, (int) (this.guiLeft - 24 + (double) (xSize + 48) * power / PowerConf.crystalRefineryMaxPower), this.guiTop - 22, 0xFF2196F3);

            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:power") + ": " + String.format("%09.2f", power) + " / " + String.format("%09.2f", PowerConf.crystalRefineryMaxPower) + " FE",
                    this.guiLeft - 24, this.guiTop - 34, 0xFAFAFA, false);

            //Draw progress value
            Field fProgress = te.getClass().getField("progress");
            int progress = fProgress.getInt(te);
            Field fMaxProgress = te.getClass().getField("maxProgress");
            int maxProgress = fMaxProgress.getInt(te);
            Field fPowerMultiplier = te.getClass().getField("powerMultiplier");
            double powerMultiplier = fPowerMultiplier.getDouble(te);

            float ticksLeft;
            if (power >= PowerConf.crystalRefineryUsage * powerMultiplier) {
                ticksLeft = (progress == 0 || maxProgress - progress - partialTicks < 0) ? maxProgress : maxProgress - progress - partialTicks;
            } else {
                ticksLeft = maxProgress - progress;
            }

            this.drawRect(this.guiLeft - 24, this.guiTop - 8, this.guiLeft + xSize + 24, this.guiTop - 6, 0xFF212121);
            this.drawRect(this.guiLeft - 24, this.guiTop - 8, (int) (this.guiLeft - 24 + (xSize + 48) * (1 - ticksLeft / maxProgress)), this.guiTop - 6, 0xFF4CAF50);

            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:remaining") + ": " + String.format("%06.2f", (ticksLeft) / 20f) + "s",
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
