package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.network.MessageRequestPowerStats;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by CraftedCart on 29/12/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorPowerStats extends GuiScreen {

    private IInventory playerInv;
    private TEFFProjector te;
    private EntityPlayer player;
    private List<Object[]> guiText = new ArrayList<Object[]>(); //Contains text X, Y, String, Colour

    final float blueR = 33f; final float blueG = 150f; final float blueB = 243f; //Blue RGB
    final float whiteR = 250f; final float whiteG = 250f; final float whiteB = 250f; //White RGB
    final float lightGreyR = 224f; final float lightGreyG = 224f; final float lightGreyB = 224f; //Light Grey RGB
    final float darkGreyR = 33f; final float darkGreyG = 33f; final float darkGreyB = 33f; //Dark Grey RGB
    final float blueGreyR = 96f; final float blueGreyG = 125f; final float blueGreyB = 139f; //Blue Grey RGB
    final float lightBlueGreyR = 144f; final float lightBlueGreyG = 164f; final float lightBlueGreyB = 174f; //Light Blue Grey RGB
    final float darkBlueGreyR = 69f; final float darkBlueGreyG = 90f; final float darkBlueGreyB = 100f; //Dark Blue Grey RGB

    private boolean lmbDown = false; //Left mouse button pressed down?

    private int highlightedSidebarButtonYOffset = -1; //-1: Nothing selected
    private double highlightedSidebarButtonTiming = 0;

    public GuiFFProjectorPowerStats(EntityPlayer player, IInventory playerInv, TEFFProjector te) {

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        NetworkHandler.network.sendToServer(new MessageRequestPowerStats(te.getPos(), player));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        guiText.clear();

        final int w = Display.getWidth(); //Window width
        final int h = Display.getHeight(); //Window height
        final int mx = Mouse.getX(); //Get mouse X
        final int my = h - Mouse.getY(); //Get mouse y
        final double deltaTime = GuiUtils.getDelta(); //Get the time between this frame and the last frame
        final boolean lmbClicked; //Was the left mouse button was clicked on this frame?
        if (lmbDown) {
            lmbClicked = false;
        } else {
            lmbClicked = Mouse.isButtonDown(0);
        }
        lmbDown = Mouse.isButtonDown(0);

        GuiUtils.setup(true);

        //Custom Gui Rendering Code
        GuiUtils.drawBackground(UIColor.matWhite());

        //<editor-fold desc="Draw sidebar">
        final int sidebarWidth = 150;

        GuiUtils.drawQuad( //Blue Grey Sidebar
                new PosXY(0, 24),
                new PosXY(sidebarWidth, h),
                UIColor.matBlueGrey()
        );

        GuiUtils.drawQuadGradientHorizontal( //Sidebar shadow
                new PosXY(sidebarWidth, 24),
                new PosXY(sidebarWidth + 4, h),
                UIColor.matGrey900(), UIColor.matGrey900(0)
        );

        //Draw sidebar items
        if (mx <= sidebarWidth) {
            if (my >= 28 && my < 52) {
                //Mouse is over the info button
                if (highlightedSidebarButtonYOffset != 28) {
                    highlightedSidebarButtonYOffset = 28;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI));
                }
            } else if (my >= 52 && my < 76) {
                //Mouse is over the sizing button
                if (highlightedSidebarButtonYOffset != 52) {
                    highlightedSidebarButtonYOffset = 52;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Sizing_TILE_ENTITY_GUI));
                }
            } else if (my >= 76 && my < 100) {
                //Mouse is over the security button
                if (highlightedSidebarButtonYOffset != 76) {
                    highlightedSidebarButtonYOffset = 76;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Security_TILE_ENTITY_GUI));
                }
            } else if (my >= 100 && my < 124) {
                //Mouse is over the upgrades button
                if (highlightedSidebarButtonYOffset != 100) {
                    highlightedSidebarButtonYOffset = 100;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked) {
                    //TODO Implement upgrades gui and link to it here
                }
            } else if (my >= 124 && my < 148) {
                //Mouse is over the power statistics button
                if (highlightedSidebarButtonYOffset != 124) {
                    highlightedSidebarButtonYOffset = 124;
                    highlightedSidebarButtonTiming = 0;
                }
            } else {
                highlightedSidebarButtonYOffset = -1;
            }
        } else {
            highlightedSidebarButtonTiming = -1;
        }

        //<editor-fold desc="Highlighted sidebar button background">
        if (highlightedSidebarButtonYOffset != -1) {
            if (lmbDown) {
                GL11.glColor4d(UIColor.matBlueGrey700().r, UIColor.matBlueGrey700().g, UIColor.matBlueGrey700().b, highlightedSidebarButtonTiming); //Dark Blue Grey highlighted button background
            } else {
                GL11.glColor4d(UIColor.matBlueGrey300().r, UIColor.matBlueGrey300().g, UIColor.matBlueGrey300().b, highlightedSidebarButtonTiming); //Light Blue Grey highlighted button background
            }

            GuiUtils.drawQuad( //Highlighted button
                    new PosXY(0, highlightedSidebarButtonYOffset),
                    new PosXY(sidebarWidth, highlightedSidebarButtonYOffset + 24)
            );

            GL11.glLineWidth(8f);

            GuiUtils.drawQuadGradientVertical( //Bottom shadow
                    new PosXY(0, highlightedSidebarButtonYOffset + 24),
                    new PosXY(sidebarWidth, highlightedSidebarButtonYOffset + 26),
                    UIColor.matGrey900(highlightedSidebarButtonTiming), UIColor.matGrey900(0)
            );

            GuiUtils.drawQuadGradientVertical( //Top shadow
                    new PosXY(0, highlightedSidebarButtonYOffset - 2),
                    new PosXY(sidebarWidth, highlightedSidebarButtonYOffset),
                    UIColor.matGrey900(0), UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            if (highlightedSidebarButtonTiming + deltaTime * 8 <= 1) {
                highlightedSidebarButtonTiming += deltaTime * 8;
            } else {
                highlightedSidebarButtonTiming = 1;
            }
        }
        //</editor-fold>
        
        //24 px gap between items
        guiText.add(new Object[]{8, 28, StatCollector.translateToLocal("gui.mff:info"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        guiText.add(new Object[]{8, 52, StatCollector.translateToLocal("gui.mff:sizing"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        guiText.add(new Object[]{8, 76, StatCollector.translateToLocal("gui.mff:security"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        guiText.add(new Object[]{8, 100, StatCollector.translateToLocal("gui.mff:upgrades"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        guiText.add(new Object[]{8, 124, StatCollector.translateToLocal("gui.mff:powerStats"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});

        //Draw power meter at the bottom of the sidebar
        if (mx < sidebarWidth && my > h - 36) {

            GuiUtils.drawQuad( //Mouseover background
                    new PosXY(0, h - 36),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900()
            );

            GuiUtils.drawQuadGradientVertical( //Selected button top shadow
                    new PosXY(0, h - 38),
                    new PosXY(sidebarWidth, h - 36),
                    UIColor.matGrey900(0), UIColor.matGrey900()
            );

            GuiUtils.drawQuad( //Popover BG
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(w - 8, h - 8),
                    UIColor.matGrey900()
            );

            GuiUtils.drawQuadGradient( //Popover BG top shadow
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(w - 8, h - 60),
                    new PosXY(w - 4, h - 64),
                    new PosXY(sidebarWidth + 4, h - 64),
                    UIColor.matGrey900(), UIColor.matGrey900(0)
            );

            GuiUtils.drawQuadGradient( //Popover BG right shadow
                    new PosXY(w - 4, h - 4),
                    new PosXY(w - 4, h - 64),
                    new PosXY(w - 8, h - 60),
                    new PosXY(w - 8, h - 8),
                    UIColor.matGrey900(0), UIColor.matGrey900()
            );

            GuiUtils.drawQuadGradient( //Popover BG bottom shadow
                    new PosXY(sidebarWidth + 12, h - 4),
                    new PosXY(w - 4, h - 4),
                    new PosXY(w - 8, h - 8),
                    new PosXY(sidebarWidth + 8, h - 8),
                    UIColor.matGrey900(0), UIColor.matGrey900()
            );

            GuiUtils.drawQuad( //Popover BG joiner bit
                    new PosXY(sidebarWidth + 8, h - 8),
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(sidebarWidth, h - 36),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900()
            );

            GuiUtils.drawQuadGradient( //Joiner bit top shadow
                    new PosXY(sidebarWidth, h - 36),
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(sidebarWidth + 4, h - 64),
                    new PosXY(sidebarWidth, h - 40),
                    UIColor.matGrey900(), UIColor.matGrey900(0)
            );

            GuiUtils.drawQuadGradient( //Joiner bit bottom shadow
                    new PosXY(sidebarWidth, h + 4),
                    new PosXY(sidebarWidth + 12, h - 4),
                    new PosXY(sidebarWidth + 8, h - 8),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900(0), UIColor.matGrey900()
            );

            GuiUtils.drawQuad( //Popover Power Bar BG
                    new PosXY(sidebarWidth + 12, h - 16),
                    new PosXY(w - 12, h - 12),
                    UIColor.matBlueGrey700()
            );

            GuiUtils.drawQuad( //Popover Power Bar FG
                    new PosXY(sidebarWidth + 12, h - 16),
                    new PosXY(((w - sidebarWidth - 24) * te.power / te.maxPower) + sidebarWidth + 12, h - 12),
                    UIColor.matBlue()
            );

            guiText.add(new Object[]{sidebarWidth + 14, h - 58, String.format("%012.2f / %012.2f %s", te.power, te.maxPower, StatCollector.translateToLocal("gui.mff:fe")), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
            guiText.add(new Object[]{sidebarWidth + 14, h - 38, String.format("%.2f %s / t", te.powerUsage, StatCollector.translateToLocal("gui.mff:fe")), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});

        }

        GuiUtils.drawQuad( //Power Bar BG
                new PosXY(4, h - 12),
                new PosXY(sidebarWidth - 4, h - 8),
                UIColor.matGrey900()
        );

        GuiUtils.drawQuad( //Power Bar FG
                new PosXY(4, h - 12),
                new PosXY((sidebarWidth * te.power / te.maxPower) - 4, h - 8),
                UIColor.matBlueGrey700()
        );

        guiText.add(new Object[]{8, h - 34, String.format("%s: %06.2f%%", StatCollector.translateToLocal("gui.mff:power"), te.power / te.maxPower * 100), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        //</editor-fold>

        //<editor-fold desc="Draw top bar">
        GuiUtils.drawQuad(
                new PosXY(0, 0),
                new PosXY(w, 24),
                UIColor.matBlue()
        );

        GuiUtils.drawQuadGradientVertical(
                new PosXY(0, 24),
                new PosXY(w, 28),
                UIColor.matGrey900(), UIColor.matGrey900(0)
        );

        guiText.add(new Object[]{8, 1, StatCollector.translateToLocal("gui.mff:powerStats"), new Color(whiteR / 255, whiteG / 255, whiteB / 255)});
        //</editor-fold>

        GL11.glLineWidth(2f);

        int graphHeight = ((h - 64) / 4) - 48;
        int graphWidth = (w - 20) - (sidebarWidth + 200);

        //<editor-fold desc="Graph 1">
        //Draw FE value & time at the mouse pos
        if (mx >= sidebarWidth + 200 && mx <= w - 20) {
            if (my >= 48 && my <= graphHeight + 48) {
                //Draw FE value
                GL11.glColor4f(blueR / 255, blueG / 255, blueB / 255, 1); //Draw horizontal line
                GL11.glBegin(GL11.GL_LINE_STRIP);
                {
                    GL11.glVertex2d(sidebarWidth + 200, my);
                    GL11.glVertex2d(w - 20, my);
                }
                GL11.glEnd();

                double mousePercentageHeight = (my - 48f) / graphHeight;
                guiText.add(new Object[]{sidebarWidth + 24, my - 10, String.format("%.2f %s / t",
                        Collections.max(te.powerUsagePerTickForPastMinute) * (1 - mousePercentageHeight), StatCollector.translateToLocal("gui.mff:fe")),
                        new Color(blueR / 255, blueG / 255, blueB / 255)});

                //Draw time
                //Draw vertical line
                GL11.glBegin(GL11.GL_LINE_STRIP);
                {
                    GL11.glVertex2d(mx, 48);
                    GL11.glVertex2d(mx, graphHeight + 48);
                }
                GL11.glEnd();

                double mousePercentageWidth = (mx - sidebarWidth - 220f) / graphWidth;
                guiText.add(new Object[]{mx, graphHeight + 50, String.format("-%.2f%s",
                        60 * (1 - mousePercentageWidth), StatCollector.translateToLocal("gui.mff:secs")),
                        new Color(blueR / 255, blueG / 255, blueB / 255)});
            }
        }

        //Draw graph outline
        GL11.glColor4f(lightGreyR / 255, lightGreyG / 255, lightGreyB / 255, 1);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glVertex2d(sidebarWidth + 200, 48);
            GL11.glVertex2d(sidebarWidth + 200, graphHeight + 48);
            GL11.glVertex2d(w - 20, (h - 64) / 4);
        }
        GL11.glEnd();

        //Draw the graph
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glColor4f(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255, 1);
            for (int i = 0; i < 1200; i++) {
                if (te.powerUsagePerTickForPastMinute.size() - 1 < i) {
                    break;
                }

                double percentageHeightOnGraph = te.powerUsagePerTickForPastMinute.get(i) / Collections.max(te.powerUsagePerTickForPastMinute);

                GL11.glVertex2d((1 - (i / 1200f)) * graphWidth + sidebarWidth + 200, 48 + graphHeight * (1 - percentageHeightOnGraph));

            }
        }
        GL11.glEnd();

        //Draw min and max FE values on the left of the graph
        guiText.add(new Object[]{sidebarWidth + 24, 44, String.format("%.2f %s / t",
                Collections.max(te.powerUsagePerTickForPastMinute), StatCollector.translateToLocal("gui.mff:fe")), new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        guiText.add(new Object[]{sidebarWidth + 24, (h - 64) / 4 - 22, String.format("0 %s / t",
                StatCollector.translateToLocal("gui.mff:fe")), new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});

        //Draw timing on the bottom of the graph
        guiText.add(new Object[]{sidebarWidth + 202, graphHeight + 50, StatCollector.translateToLocal("gui.mff:1MinAgo"),
                new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        guiText.add(new Object[]{w - 22 - GuiUtils.font.getWidth(StatCollector.translateToLocal("gui.mff:now")), graphHeight + 50, StatCollector.translateToLocal("gui.mff:now"),
                new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        //</editor-fold>

        //<editor-fold desc="Graph 2">
        //Draw FE value & time at the mouse pos
        if (mx >= sidebarWidth + 200 && mx <= w - 20) {
            if (my >= 48 + graphHeight + 48 && my <= graphHeight + 48 + graphHeight + 48) {
                //Draw FE value
                GL11.glColor4f(blueR / 255, blueG / 255, blueB / 255, 1); //Draw horizontal line
                GL11.glBegin(GL11.GL_LINE_STRIP);
                {
                    GL11.glVertex2d(sidebarWidth + 200, my);
                    GL11.glVertex2d(w - 20, my);
                }
                GL11.glEnd();

                double mousePercentageHeight = (my - 48f - graphHeight - 48) / graphHeight;
                guiText.add(new Object[]{sidebarWidth + 24, my - 10, String.format("%.2f %s / t",
                        Collections.max(te.powerUsagePerSecondForPastHalfHour) * (1 - mousePercentageHeight), StatCollector.translateToLocal("gui.mff:fe")),
                        new Color(blueR / 255, blueG / 255, blueB / 255)});

                //Draw time
                //Draw vertical line
                GL11.glBegin(GL11.GL_LINE_STRIP);
                {
                    GL11.glVertex2d(mx, 48 + graphHeight + 48);
                    GL11.glVertex2d(mx, graphHeight + 48 + graphHeight + 48);
                }
                GL11.glEnd();

                double mousePercentageWidth = (mx - sidebarWidth - 220f) / graphWidth;
                guiText.add(new Object[]{mx, graphHeight + 50 + graphHeight + 48, String.format("-%.2f%s",
                        1800 * (1 - mousePercentageWidth) / 60, StatCollector.translateToLocal("gui.mff:mins")),
                        new Color(blueR / 255, blueG / 255, blueB / 255)});
            }
        }

        //Draw graph outline
        GL11.glColor4f(lightGreyR / 255, lightGreyG / 255, lightGreyB / 255, 1);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glVertex2d(sidebarWidth + 200, 48 + graphHeight + 48);
            GL11.glVertex2d(sidebarWidth + 200, graphHeight + 48 + graphHeight + 48);
            GL11.glVertex2d(w - 20, (h - 64) / 4 + graphHeight + 48);
        }
        GL11.glEnd();

        //Draw the graph
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glColor4f(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255, 1);
            for (int i = 0; i < 1800; i++) {
                if (te.powerUsagePerSecondForPastHalfHour.size() - 1 < i) {
                    break;
                }

                double percentageHeightOnGraph = te.powerUsagePerSecondForPastHalfHour.get(i) / Collections.max(te.powerUsagePerSecondForPastHalfHour);

                GL11.glVertex2d((1 - (i / 1800f)) * graphWidth + sidebarWidth + 200, 48 + graphHeight * (1 - percentageHeightOnGraph) + graphHeight + 48);

            }
        }
        GL11.glEnd();

        //Draw min and max FE values on the left of the graph
        guiText.add(new Object[]{sidebarWidth + 24, 44 + graphHeight + 48, String.format("%.2f %s / t",
                Collections.max(te.powerUsagePerSecondForPastHalfHour), StatCollector.translateToLocal("gui.mff:fe")), new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        guiText.add(new Object[]{sidebarWidth + 24, (h - 64) / 4 - 22 + graphHeight + 48, String.format("0 %s / t",
                StatCollector.translateToLocal("gui.mff:fe")), new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});

        //Draw timing on the bottom of the graph
        guiText.add(new Object[]{sidebarWidth + 202, graphHeight + 50 + graphHeight + 48, StatCollector.translateToLocal("gui.mff:30MinsAgo"),
                new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        guiText.add(new Object[]{w - 22 - GuiUtils.font.getWidth(StatCollector.translateToLocal("gui.mff:now")), graphHeight + 50 + graphHeight + 48, StatCollector.translateToLocal("gui.mff:now"),
                new Color(darkGreyR / 255, darkGreyG / 255, darkGreyB / 255)});
        //</editor-fold>


        //<editor-fold desc="Draw text on the Gui">
        for (Object[] textData : guiText) {
            GuiUtils.font.drawString((Integer) textData[0], (Integer) textData[1], (String) textData[2], (Color) textData[3]);
        }
        //</editor-fold>

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

}
