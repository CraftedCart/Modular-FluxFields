package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSave;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 22/11/2015 (DD/MM/YYYY)
 */

public class GuiFFProjector extends GuiScreen {

    TEFFProjector projector;
    BlockPos pos;

    int x1;
    int y1;
    int z1;
    int x2;
    int y2;
    int z2;

    GuiButton x1Take1;
    GuiButton x1Take16;
    GuiButton x1Take32;
    GuiButton x1Add1;
    GuiButton x1Add16;
    GuiButton x1Add32;

    GuiButton y1Take1;
    GuiButton y1Take16;
    GuiButton y1Take32;
    GuiButton y1Add1;
    GuiButton y1Add16;
    GuiButton y1Add32;

    GuiButton z1Take1;
    GuiButton z1Take16;
    GuiButton z1Take32;
    GuiButton z1Add1;
    GuiButton z1Add16;
    GuiButton z1Add32;

    GuiButton x2Take1;
    GuiButton x2Take16;
    GuiButton x2Take32;
    GuiButton x2Add1;
    GuiButton x2Add16;
    GuiButton x2Add32;

    GuiButton y2Take1;
    GuiButton y2Take16;
    GuiButton y2Take32;
    GuiButton y2Add1;
    GuiButton y2Add16;
    GuiButton y2Add32;

    GuiButton z2Take1;
    GuiButton z2Take16;
    GuiButton z2Take32;
    GuiButton z2Add1;
    GuiButton z2Add16;
    GuiButton z2Add32;

    GuiButton save;

    public GuiFFProjector(TEFFProjector projector, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.projector = projector;
        this.pos = pos;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        //Draw XYZ 1/2 Pos Text
        this.fontRendererObj.drawString("X1: " + x1, this.width / 2 - 112, this.height / 2 - 62, 0xFAFAFA, false);
        this.fontRendererObj.drawString("Y1: " + y1, this.width / 2 - 112, this.height / 2 - 38, 0xFAFAFA, false);
        this.fontRendererObj.drawString("Z1: " + z1, this.width / 2 - 112, this.height / 2 - 14, 0xFAFAFA, false);
        this.fontRendererObj.drawString("X2: " + x2, this.width / 2 - 112, this.height / 2 + 8, 0xFAFAFA, false);
        this.fontRendererObj.drawString("Y2: " + y2, this.width / 2 - 112, this.height / 2 + 34, 0xFAFAFA, false);
        this.fontRendererObj.drawString("Z2: " + z2, this.width / 2 - 112, this.height / 2 + 58, 0xFAFAFA, false);

        try {

            //Draw power value
            Field f = projector.getClass().getField("power");
            double power = f.getDouble(projector);
            double usage = ((x2 - x1) * (y2 - y1) + (z2 - z1 - 2) * (y2 - y1) + (x2 - x1 - 2) * (z2 - z1 - 2)) * 2 * PowerConf.ffProjectorUsagePerBlock;

            this.drawRect(this.width / 2 - 113, this.height / 2 + 99, this.width / 2 + 180, this.height / 2 + 101, 0xFF212121);
            this.drawRect(this.width / 2 - 113, this.height / 2 + 99, (int) (this.width / 2 - 113 + power / PowerConf.ffProjectorMaxPower * 293), this.height / 2 + 101, 0xFF2196F3);

            this.fontRendererObj.drawString("Power: " + String.format("%012.2f", power) + " / " + String.format("%012.2f", PowerConf.ffProjectorMaxPower) + " FE",
                    this.width / 2 - 112, this.height / 2 + 90, 0xFAFAFA, false);

            //Draw power usage
            this.fontRendererObj.drawString("Usage to sustain: " + String.format("%.2f", usage) + " FE / t", this.width / 2 - 112, this.height / 2 + 104, 0xFAFAFA, false);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void initGui() {

        //XYZ 1
        this.buttonList.add(this.x1Take1 = new GuiButton(0, this.width / 2 + 76, this.height / 2 - 68, 32, 20, "- 1"));
        this.buttonList.add(this.x1Take16 = new GuiButton(1, this.width / 2 + 40, this.height / 2 - 68, 32, 20, "- 16"));
        this.buttonList.add(this.x1Take32 = new GuiButton(2, this.width / 2 + 4, this.height / 2 - 68, 32, 20, "- 32"));

        this.buttonList.add(this.x1Add1 = new GuiButton(3, this.width / 2 + 112, this.height / 2 - 68, 32, 20, "+ 1"));
        this.buttonList.add(this.x1Add16 = new GuiButton(4, this.width / 2 + 148, this.height / 2 - 68, 32, 20, "+ 16"));
        this.buttonList.add(this.x1Add32 = new GuiButton(5, this.width / 2 + 184, this.height / 2 - 68, 32, 20, "+ 32"));

        this.buttonList.add(this.y1Take1 = new GuiButton(6, this.width / 2 + 76, this.height / 2 - 44, 32, 20, "- 1"));
        this.buttonList.add(this.y1Take16 = new GuiButton(7, this.width / 2 + 40, this.height / 2 - 44, 32, 20, "- 16"));
        this.buttonList.add(this.y1Take32 = new GuiButton(8, this.width / 2 + 4, this.height / 2 - 44, 32, 20, "- 32"));

        this.buttonList.add(this.y1Add1 = new GuiButton(9, this.width / 2 + 112, this.height / 2 - 44, 32, 20, "+ 1"));
        this.buttonList.add(this.y1Add16 = new GuiButton(10, this.width / 2 + 148, this.height / 2 - 44, 32, 20, "+ 16"));
        this.buttonList.add(this.y1Add32 = new GuiButton(11, this.width / 2 + 184, this.height / 2 - 44, 32, 20, "+ 32"));

        this.buttonList.add(this.z1Take1 = new GuiButton(12, this.width / 2 + 76, this.height / 2 - 20, 32, 20, "- 1"));
        this.buttonList.add(this.z1Take16 = new GuiButton(13, this.width / 2 + 40, this.height / 2 - 20, 32, 20, "- 16"));
        this.buttonList.add(this.z1Take32 = new GuiButton(14, this.width / 2 + 4, this.height / 2 - 20, 32, 20, "- 32"));

        this.buttonList.add(this.z1Add1 = new GuiButton(15, this.width / 2 + 112, this.height / 2 - 20, 32, 20, "+ 1"));
        this.buttonList.add(this.z1Add16 = new GuiButton(16, this.width / 2 + 148, this.height / 2 - 20, 32, 20, "+ 16"));
        this.buttonList.add(this.z1Add32 = new GuiButton(17, this.width / 2 + 184, this.height / 2 - 20, 32, 20, "+ 32"));

        //XYZ 2
        this.buttonList.add(this.x2Take1 = new GuiButton(0, this.width / 2 + 76, this.height / 2 + 4, 32, 20, "- 1"));
        this.buttonList.add(this.x2Take16 = new GuiButton(1, this.width / 2 + 40, this.height / 2 + 4, 32, 20, "- 16"));
        this.buttonList.add(this.x2Take32 = new GuiButton(2, this.width / 2 + 4, this.height / 2 + 4, 32, 20, "- 32"));

        this.buttonList.add(this.x2Add1 = new GuiButton(3, this.width / 2 + 112, this.height / 2 + 4, 32, 20, "+ 1"));
        this.buttonList.add(this.x2Add16 = new GuiButton(4, this.width / 2 + 148, this.height / 2 + 4, 32, 20, "+ 16"));
        this.buttonList.add(this.x2Add32 = new GuiButton(5, this.width / 2 + 184, this.height / 2 + 4, 32, 20, "+ 32"));

        this.buttonList.add(this.y2Take1 = new GuiButton(6, this.width / 2 + 76, this.height / 2 + 28, 32, 20, "- 1"));
        this.buttonList.add(this.y2Take16 = new GuiButton(7, this.width / 2 + 40, this.height / 2 + 28, 32, 20, "- 16"));
        this.buttonList.add(this.y2Take32 = new GuiButton(8, this.width / 2 + 4, this.height / 2 + 28, 32, 20, "- 32"));

        this.buttonList.add(this.y2Add1 = new GuiButton(9, this.width / 2 + 112, this.height / 2 + 28, 32, 20, "+ 1"));
        this.buttonList.add(this.y2Add16 = new GuiButton(10, this.width / 2 + 148, this.height / 2 + 28, 32, 20, "+ 16"));
        this.buttonList.add(this.y2Add32 = new GuiButton(11, this.width / 2 + 184, this.height / 2 + 28, 32, 20, "+ 32"));

        this.buttonList.add(this.z2Take1 = new GuiButton(12, this.width / 2 + 76, this.height / 2 + 52, 32, 20, "- 1"));
        this.buttonList.add(this.z2Take16 = new GuiButton(13, this.width / 2 + 40, this.height / 2 + 52, 32, 20, "- 16"));
        this.buttonList.add(this.z2Take32 = new GuiButton(14, this.width / 2 + 4, this.height / 2 + 52, 32, 20, "- 32"));

        this.buttonList.add(this.z2Add1 = new GuiButton(15, this.width / 2 + 112, this.height / 2 + 52, 32, 20, "+ 1"));
        this.buttonList.add(this.z2Add16 = new GuiButton(16, this.width / 2 + 148, this.height / 2 + 52, 32, 20, "+ 16"));
        this.buttonList.add(this.z2Add32 = new GuiButton(17, this.width / 2 + 184, this.height / 2 + 52, 32, 20, "+ 32"));

        this.buttonList.add(this.save = new GuiButton(13, this.width / 2 + 184, this.height / 2 + 84, 32, 20, "Save"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.x1Add1 && x1 < x2) {
            x1++;
        } else if (button == this.x1Add16 && x1 + 15 < x2) {
            x1 += 16;
        } else if (button == this.x1Add32  && x1 + 31 < x2) {
            x1 += 32;
        } else if (button == this.x1Take1) {
            x1--;
        } else if (button == this.x1Take16) {
            x1 -= 16;
        } else if (button == this.x1Take32) {
            x1 -= 32;
        } else if (button == this.y1Add1 && y1 < y2) {
            y1++;
        } else if (button == this.y1Add16 && y1 + 15 < y2) {
            y1 += 16;
        } else if (button == this.y1Add32 && y1 + 31 < y2) {
            y1 += 32;
        } else if (button == this.y1Take1) {
            y1--;
        } else if (button == this.y1Take16) {
            y1 -= 16;
        } else if (button == this.y1Take32) {
            y1 -= 32;
        } else if (button == this.z1Add1 && z1 < z2) {
            z1++;
        } else if (button == this.z1Add16 && z1 + 15 < z2) {
            z1 += 16;
        } else if (button == this.z1Add32 && z1 + 31 < z2) {
            z1 += 32;
        } else if (button == this.z1Take1) {
            z1--;
        } else if (button == this.z1Take16) {
            z1 -= 16;
        } else if (button == this.z1Take32) {
            z1 -= 32;
        } else if (button == this.x2Add1) {
            x2++;
        } else if (button == this.x2Add16) {
            x2 += 16;
        } else if (button == this.x2Add32) {
            x2 += 32;
        } else if (button == this.x2Take1 && x2 > x1) {
            x2--;
        } else if (button == this.x2Take16 && x2 - 15 > x1) {
            x2 -= 16;
        } else if (button == this.x2Take32 && x2 - 31 > x1) {
            x2 -= 32;
        } else if (button == this.y2Add1) {
            y2++;
        } else if (button == this.y2Add16) {
            y2 += 16;
        } else if (button == this.y2Add32) {
            y2 += 32;
        } else if (button == this.y2Take1 && y2 > y1) {
            y2--;
        } else if (button == this.y2Take16 && y2 - 15 > y1) {
            y2 -= 16;
        } else if (button == this.y2Take32 && y2 - 32 > y1) {
            y2 -= 32;
        } else if (button == this.z2Add1) {
            z2++;
        } else if (button == this.z2Add16) {
            z2 += 16;
        } else if (button == this.z2Add32) {
            z2 += 32;
        } else if (button == this.z2Take1 && z2 > z1) {
            z2--;
        } else if (button == this.z2Take16 && z2 - 15 > z1) {
            z2 -= 16;
        } else if (button == this.z2Take32 && z2 - 32 > z1) {
            z2 -= 32;
        } else if (button == this.save) {

            NetworkHandler.network.sendToServer(new MessageFFProjectorGuiSave(pos, x1, y1, z1, x2, y2, z2));

            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null)
                this.mc.setIngameFocus();
        }

    }

}
