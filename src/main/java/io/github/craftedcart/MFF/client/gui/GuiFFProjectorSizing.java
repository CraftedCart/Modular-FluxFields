package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSaveSizing;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.reference.FFProjectorConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.utility.LogHelper;
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

public class GuiFFProjectorSizing extends GuiContainer {

    private IInventory playerInv;
    private TEFFProjector te;
    private EntityPlayer player;

    int xMin;
    int xMax;
    int yMin;
    int yMax;
    int zMin;
    int zMax;
    String selected;

    int x1;
    int x2;
    int y1;
    int y2;
    int z1;
    int z2;

    int sliderPxWidth;
    int xDiff = FFProjectorConf.xSizeMax - FFProjectorConf.xSizeMin;
    int yDiff = FFProjectorConf.ySizeMax - FFProjectorConf.ySizeMin;
    int zDiff = FFProjectorConf.zSizeMax - FFProjectorConf.zSizeMin;

    public GuiFFProjectorSizing(EntityPlayer player, IInventory playerInv, TEFFProjector te) {
        super(new ContainerFFProjectorInfo(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

        this.xSize = 256;
        this.ySize = 178;

        x1 = this.te.minX;
        x2 = this.te.maxX;
        y1 = this.te.minY;
        y2 = this.te.maxY;
        z1 = this.te.minZ;
        z2 = this.te.maxZ;

        sliderPxWidth = xSize - 15;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSizing.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        //Draw power value
        double power = this.te.power;
        double maxPower = this.te.maxPower;
        double powerUsage = this.te.powerUsage;

        drawRect(this.guiLeft, this.guiTop - 8, this.guiLeft + xSize, this.guiTop - 6, 0xFF212121);
        drawRect(this.guiLeft, this.guiTop - 8, (int) (this.guiLeft + (double) xSize * power / maxPower), this.guiTop - 6, 0xFF2196F3);

        this.fontRendererObj.drawString(
                String.format("%s: %012.2f / %09.0f %s (%.2f %s / t)",
                        StatCollector.translateToLocal("gui.mff:power"), power, maxPower, StatCollector.translateToLocal("gui.mff:fe"), powerUsage, StatCollector.translateToLocal("gui.mff:fe")),
                guiLeft, guiTop - 18, 0xFAFAFA, false);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 75, 4, 0x404040); //Draw block name
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 84, 0x404040); //Draw inventory name
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:sizing"), 15, 18, 0x404040); //Draw tab name

        //Sliders & Sizing info
        double xMinPercent = (x1 + Math.abs(FFProjectorConf.xSizeMin)) / (double) xDiff;
        xMin = (int) (sliderPxWidth * xMinPercent + 11);
        double xMaxPercent = (x2 + Math.abs(FFProjectorConf.xSizeMin)) / (double) xDiff;
        xMax = (int) (sliderPxWidth * xMaxPercent + 11);
        double yMinPercent = (y1 + Math.abs(FFProjectorConf.ySizeMin)) / (double) yDiff;
        yMin = (int) (sliderPxWidth * yMinPercent + 11);
        double yMaxPercent = (y2 + Math.abs(FFProjectorConf.ySizeMin)) / (double) yDiff;
        yMax = (int) (sliderPxWidth * yMaxPercent + 11);
        double zMinPercent = (z1 + Math.abs(FFProjectorConf.zSizeMin)) / (double) zDiff;
        zMin = (int) (sliderPxWidth * zMinPercent + 11);
        double zMaxPercent = (z2 + Math.abs(FFProjectorConf.zSizeMin)) / (double) zDiff;
        zMax = (int) (sliderPxWidth * zMaxPercent + 11);

        //Draw x slider
        drawRect(11, 32, xSize - 4, 30, 0xFF212121); //Draw Slider Background
        drawRect(xMin, 32, xMax, 30, 0xFFF44336); //Draw Selected Area
        drawRect(xMin, 27, xMin + 1, 34, 0xFFD32F2F); //Draw Left Handle
        drawRect(xMax, 27, xMax - 1, 34, 0xFFD32F2F); //Draw Right Handle
        this.fontRendererObj.drawString(String.format("X1: %d - X2: %d", x1, x2), 11, 36, 0x404040); //Draw x text

        //Draw y slider
        drawRect(11, 49, xSize - 4, 47, 0xFF212121); //Draw Slider Background
        drawRect(yMin, 49, yMax, 47, 0xFF4CAF50); //Draw Selected Area
        drawRect(yMin, 44, yMin + 1, 51, 0xFF388E3C); //Draw Left Handle
        drawRect(yMax, 44, yMax - 1, 51, 0xFF388E3C); //Draw Right Handle
        this.fontRendererObj.drawString(String.format("Y1: %d - Y2: %d", y1, y2), 11, 53, 0x404040); //Draw y text

        //Draw z slider
        drawRect(11, 66, xSize - 4, 64, 0xFF212121); //Draw Slider Background
        drawRect(zMin, 66, zMax, 64, 0xFF2196F3); //Draw Selected Area
        drawRect(zMin, 61, zMin + 1, 68, 0xFF1976D2); //Draw Left Handle
        drawRect(zMax, 61, zMax - 1, 68, 0xFF1976D2); //Draw Right Handle
        this.fontRendererObj.drawString(String.format("Z1: %d - Z2: %d", z1, z2), 11, 70, 0x404040); //Draw z text

    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();

        NetworkHandler.network.sendToServer(new MessageFFProjectorGuiSaveSizing(this.te.getPos(), x1, y1, z1, x2, y2, z2));

    }

    @Override
    protected void mouseClickMove(int x, int y, int btn, long timeSinceLastClick) {

        super.mouseClickMove(x, y, btn, timeSinceLastClick);

        //Minus offsets
        x -= this.guiLeft;
        //y -= this.guiTop;


        if (selected != null) {
            if (selected.equals("x1")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (xDiff * percent - Math.abs(FFProjectorConf.xSizeMin));
                if (sliderAsValue > x2) {
                    x1 = x2;
                    selected = "x2";
                } else if (sliderAsValue < FFProjectorConf.xSizeMin) {
                    x1 = FFProjectorConf.xSizeMin;
                } else if (sliderAsValue > FFProjectorConf.xSizeMax) {
                    x1 = FFProjectorConf.xSizeMax;
                } else {
                    x1 = sliderAsValue;
                }

            } else if (selected.equals("x2")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (xDiff * percent - Math.abs(FFProjectorConf.xSizeMin));
                if (sliderAsValue < x1) {
                    x2 = x1;
                    selected = "x1";
                } else if (sliderAsValue < FFProjectorConf.xSizeMin) {
                    x2 = FFProjectorConf.xSizeMin;
                } else if (sliderAsValue > FFProjectorConf.xSizeMax) {
                    x2 = FFProjectorConf.xSizeMax;
                } else {
                    x2 = sliderAsValue;
                }
            } else if (selected.equals("y1")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (yDiff * percent - Math.abs(FFProjectorConf.ySizeMin));
                if (sliderAsValue > y2) {
                    y1 = y2;
                    selected = "y2";
                } else if (sliderAsValue < FFProjectorConf.ySizeMin) {
                    y1 = FFProjectorConf.ySizeMin;
                } else if (sliderAsValue > FFProjectorConf.ySizeMax) {
                    y1 = FFProjectorConf.ySizeMax;
                } else {
                    y1 = sliderAsValue;
                }

            } else if (selected.equals("y2")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (yDiff * percent - Math.abs(FFProjectorConf.ySizeMin));
                if (sliderAsValue < y1) {
                    y2 = y1;
                    selected = "y1";
                } else if (sliderAsValue < FFProjectorConf.ySizeMin) {
                    y2 = FFProjectorConf.ySizeMin;
                } else if (sliderAsValue > FFProjectorConf.ySizeMax) {
                    y2 = FFProjectorConf.ySizeMax;
                } else {
                    y2 = sliderAsValue;
                }
            } else if (selected.equals("z1")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (zDiff * percent - Math.abs(FFProjectorConf.zSizeMin));
                if (sliderAsValue > z2) {
                    z1 = z2;
                    selected = "z2";
                } else if (sliderAsValue < FFProjectorConf.zSizeMin) {
                    z1 = FFProjectorConf.zSizeMin;
                } else if (sliderAsValue > FFProjectorConf.zSizeMax) {
                    z1 = FFProjectorConf.zSizeMax;
                } else {
                    z1 = sliderAsValue;
                }

            } else if (selected.equals("z2")) {

                double percent = (x - 11) / (double) sliderPxWidth;
                int sliderAsValue = (int) (zDiff * percent - Math.abs(FFProjectorConf.zSizeMin));
                if (sliderAsValue < z1) {
                    z2 = z1;
                    selected = "z1";
                } else if (sliderAsValue < FFProjectorConf.zSizeMin) {
                    z2 = FFProjectorConf.zSizeMin;
                } else if (sliderAsValue > FFProjectorConf.zSizeMax) {
                    z2 = FFProjectorConf.zSizeMax;
                } else {
                    z2 = sliderAsValue;
                }
            }

        }

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

        if (y >= 28 && y <= 34 && x > xMin - 2 && x < xMin + 2) {
            //X1 slider clicked on
            selected = "x1";
        } else if (y >= 28 && y <= 34 && x > xMax - 2 && x < xMax + 2) {
            //X2 slider clicked on
            selected = "x2";
        } else if (y >= 45 && y <= 51 && x > yMin - 2 && x < yMin + 2) {
            //Y1 slider clicked on
            selected = "y1";
        } else if (y >= 45 && y <= 51 && x > yMax - 2 && x < yMax + 2) {
            //Y2 slider clicked on
            selected = "y2";
        } else if (y >= 62 && y <= 68 && x > zMin - 2 && x < zMin + 2) {
            //Y1 slider clicked on
            selected = "z1";
        } else if (y >= 62 && y <= 68 && x > zMax - 2 && x < zMax + 2) {
            //Y2 slider clicked on
            selected = "z2";
        } else {
            selected = null;
        }

        //Check tabs
        if (x >= 1 && x <= 15 && y >= 1 && y <= 13) {
            //Info button clicked
            NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI));
        //} else if (x >= 16 && x <= 29 && y >= 1 && y <= 13) {
            //Sizing button clicked
            //NO-OP, We're already on the sizing tab
        } else if (x >= 30 && x <= 43 && y >= 1 && y <= 13) {
            NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Security_TILE_ENTITY_GUI));
            //Security button clicked
        } else if (x >= 44 && x <= 57 && y >= 1 && y <= 13) {
            //Upgrades button clicked
        } else if (x >= 58 && x <= 71 && y >= 1 && y <= 13) {
            //Power Usage button clicked
            player.openGui(ModMFF.instance, GuiHandler.FFProjector_PowerStats_TILE_ENTITY_GUI, te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
        }

    }

}
