package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEPowerCube;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class GuiPowerCube extends GuiScreen {

    TEPowerCube pc;

    public GuiPowerCube(TEPowerCube pc) {
        this.pc = pc;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        double power = pc.power;
        double maxPower = pc.maxPower;

        drawRect(this.width / 2 - 150, this.height / 2 - 1, this.width / 2 + 150, this.height / 2 + 1, 0xFF212121);
        drawRect(this.width / 2 - 150, this.height / 2 - 1, (int) (this.width / 2 - 150 + (double) 300 * power / maxPower), this.height / 2 + 1, 0xFF2196F3);

        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:power") + ": " + String.format("%012.2f", power) + " / " + String.format("%09.2f", maxPower) + " " + StatCollector.translateToLocal("gui.mff:fe"),
                this.width / 2 - 150, this.height / 2 - 11, 0xFAFAFA, false);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
