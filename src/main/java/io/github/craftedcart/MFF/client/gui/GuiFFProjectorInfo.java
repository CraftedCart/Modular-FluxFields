package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.io.IOException;

/**
 * Created by CraftedCart on 30/11/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorInfo extends GuiContainer {

    private IInventory playerInv;
    private TEFFProjector te;
    private EntityPlayer player;

    public GuiFFProjectorInfo(EntityPlayer player, IInventory playerInv, TEFFProjector te) {
        super(new ContainerFFProjectorInfo(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

        this.xSize = 256;
        this.ySize = 178;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorInfo.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        //Draw power value
        double power = this.te.power;

        this.drawRect(this.guiLeft - 24, this.guiTop - 8, this.guiLeft + xSize + 24, this.guiTop - 6, 0xFF212121);
        this.drawRect(this.guiLeft - 24, this.guiTop - 8, (int) (this.guiLeft - 24 + (double) (xSize + 48) * power / PowerConf.ffProjectorMaxPower), this.guiTop - 6, 0xFF2196F3);

        this.fontRendererObj.drawString("Power: " + String.format("%012.2f", power) + " / " + String.format("%09.2f", PowerConf.ffProjectorMaxPower) + " FE",
                this.guiLeft - 24, this.guiTop - 18, 0xFAFAFA, false);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        double power = this.te.power;
        double upkeep = this.te.blockList.size() * PowerConf.ffProjectorUsagePerBlock;

        int seconds = this.te.uptime / 20;

        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem/60;
        int sec = rem%60;
        String hrStr = (hr<10 ? "0" : "")+hr;
        String mnStr = (mn<10 ? "0" : "")+mn;
        String secStr = (sec<10 ? "0" : "")+sec;

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 75, 4, 0x404040); //Draw block name
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 84, 0x404040); //Draw inventory name
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:info"), 15, 18, 0x404040); //Draw tab name

        //Info stats
        this.fontRendererObj.drawString(String.format("%012.2f", power) + " / " + String.format("%09.2f", PowerConf.ffProjectorMaxPower) + " FE",
                15, 27, 0x404040, false); //Draw power level
        this.fontRendererObj.drawString(String.format("%.2f FE / t - %.2f FE / s", upkeep, upkeep * 20),
                15, 36, 0x404040, false); //Draw power upkeep
        this.fontRendererObj.drawString(String.format("XYZ Size: %d, %d, %d", this.te.maxX - this.te.minX + 1, this.te.maxY - this.te.minY + 1, this.te.maxZ - this.te.minZ + 1),
                15, 45, 0x404040, false); //Draw size
        this.fontRendererObj.drawString(String.format("Owner: %s", te.getTileData().getString("ownerName")),
                15, 54, 0x404040, false); //Draw owner
        this.fontRendererObj.drawString(String.format("Uptime: %s : %s : %s", hrStr, mnStr, secStr),
                15, 63, 0x404040, false); //Draw uptime

    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {

        try {
            super.mouseClicked(x, y, btn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Minus offsets
        x -= this.guiLeft;
        y -= this.guiTop;

        if (x >= 1 && x <= 15 && y >= 1 && y <= 13) {
            //Info button clicked
            //NO-OP, We're already on the info tab
        } else if (x >= 16 && x <= 29 && y >= 1 && y <= 13) {
            //Sizing button clicked
            NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Sizing_TILE_ENTITY_GUI));
        } else if (x >= 30 && x <= 43 && y >= 1 && y <= 13) {
            //Security button clicked
        } else if (x >= 44 && x <= 57 && y >= 1 && y <= 13) {
            //Upgrades button clicked
        } else if (x >= 58 && x <= 71 && y >= 1 && y <= 13) {
            //Power Usage button clicked
        }

    }

}
