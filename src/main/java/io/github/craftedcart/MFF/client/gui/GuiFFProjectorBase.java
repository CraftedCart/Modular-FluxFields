package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

/**
 * Created by CraftedCart on 08/01/2016 (DD/MM/YYYY)
 */

public class GuiFFProjectorBase extends GuiScreen {

    protected int w; //Window width
    protected int h; //Window height
    protected int mx; //Get mouse X
    protected int my; //Get mouse y
    protected double deltaTime; //Get the time between this frame and the last frame
    protected boolean lmbClicked; //Was the left mouse button was clicked on this frame?
    protected int sidebarWidth = 150; //The width of the sidebar
    protected boolean lmbDown = false; //Left mouse button pressed down?
    protected int highlightedSidebarButtonYOffset = -1; //-1: Nothing selected -2: Power selected
    protected double highlightedSidebarButtonTiming = 0;
    protected int tabID;
    protected DecimalFormat formatter = new DecimalFormat("#,###.00");
    protected boolean debugKeyHit = false;
    protected int debugKeycode = 61; //Debug toggle key (F3)

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        w = Display.getWidth(); //Window width
        h = Display.getHeight(); //Window height
        mx = Mouse.getX(); //Get mouse X
        my = h - Mouse.getY(); //Get mouse y
        deltaTime = GuiUtils.getDelta(); //Get the time between this frame and the last frame
        sidebarWidth = 150; //The width of the sidebar

        lmbClicked = !lmbDown && Mouse.isButtonDown(0);
        lmbDown = Mouse.isButtonDown(0);

        GuiUtils.setup(true);
        GuiUtils.drawBackground(UIColor.matWhite());

    }

    protected void drawSidebarAndTopBar(TEFFProjector te, EntityPlayer player) {
        //<editor-fold desc="Draw sidebar">
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
                if (lmbClicked && tabID != 1) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(te.getPos(), player, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI));
                }
            } else if (my >= 52 && my < 76) {
                //Mouse is over the sizing button
                if (highlightedSidebarButtonYOffset != 52) {
                    highlightedSidebarButtonYOffset = 52;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked && tabID != 2) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(te.getPos(), player, GuiHandler.FFProjector_Sizing_TILE_ENTITY_GUI));
                }
            } else if (my >= 76 && my < 100) {
                //Mouse is over the security button
                if (highlightedSidebarButtonYOffset != 76) {
                    highlightedSidebarButtonYOffset = 76;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked && tabID != 3) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(te.getPos(), player, GuiHandler.FFProjector_Security_TILE_ENTITY_GUI));
                }
            } else if (my >= 100 && my < 124) {
                //Mouse is over the upgrades button
                if (highlightedSidebarButtonYOffset != 100) {
                    highlightedSidebarButtonYOffset = 100;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked && tabID != 4) {
                    //TODO Implement upgrades gui and link to it here
                }
            } else if (my >= 124 && my < 148) {
                //Mouse is over the power statistics button
                if (highlightedSidebarButtonYOffset != 124) {
                    highlightedSidebarButtonYOffset = 124;
                    highlightedSidebarButtonTiming = 0;
                }
                if (lmbClicked && tabID != 5) {
                    NetworkHandler.network.sendToServer(new MessageRequestOpenGui(te.getPos(), player, GuiHandler.FFProjector_PowerStats_TILE_ENTITY_GUI));
                }
            } else if (my > h - 36) {
                //Mouse is over the power bar at the bottom
                if (highlightedSidebarButtonYOffset != -2) {
                    highlightedSidebarButtonYOffset = -2;
                    highlightedSidebarButtonTiming = 0;
                }
            } else {
                highlightedSidebarButtonYOffset = -1;
            }
        } else {
            highlightedSidebarButtonTiming = -1;
        }

        //<editor-fold desc="Highlighted sidebar button background">
        if (highlightedSidebarButtonYOffset > 0) {
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

        } else if (highlightedSidebarButtonYOffset == -2) { //Highlight the power meter at the bottom of the sidebar
            GuiUtils.drawQuad( //Mouseover background
                    new PosXY(0, h - 36),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuadGradientVertical( //Selected button top shadow
                    new PosXY(0, h - 38),
                    new PosXY(sidebarWidth, h - 36),
                    UIColor.matGrey900(0), UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuad( //Popover BG
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(w - 8, h - 8),
                    UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuadGradient( //Popover BG top shadow
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(w - 8, h - 60),
                    new PosXY(w - 4, h - 64),
                    new PosXY(sidebarWidth + 4, h - 64),
                    UIColor.matGrey900(highlightedSidebarButtonTiming), UIColor.matGrey900(0)
            );

            GuiUtils.drawQuadGradient( //Popover BG right shadow
                    new PosXY(w - 4, h - 4),
                    new PosXY(w - 4, h - 64),
                    new PosXY(w - 8, h - 60),
                    new PosXY(w - 8, h - 8),
                    UIColor.matGrey900(0), UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuadGradient( //Popover BG bottom shadow
                    new PosXY(sidebarWidth + 12, h - 4),
                    new PosXY(w - 4, h - 4),
                    new PosXY(w - 8, h - 8),
                    new PosXY(sidebarWidth + 8, h - 8),
                    UIColor.matGrey900(0), UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuad( //Popover BG joiner bit
                    new PosXY(sidebarWidth + 8, h - 8),
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(sidebarWidth, h - 36),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuadGradient( //Joiner bit top shadow
                    new PosXY(sidebarWidth, h - 36),
                    new PosXY(sidebarWidth + 8, h - 60),
                    new PosXY(sidebarWidth + 4, h - 64),
                    new PosXY(sidebarWidth, h - 40),
                    UIColor.matGrey900(highlightedSidebarButtonTiming), UIColor.matGrey900(0)
            );

            GuiUtils.drawQuadGradient( //Joiner bit bottom shadow
                    new PosXY(sidebarWidth, h + 4),
                    new PosXY(sidebarWidth + 12, h - 4),
                    new PosXY(sidebarWidth + 8, h - 8),
                    new PosXY(sidebarWidth, h),
                    UIColor.matGrey900(0), UIColor.matGrey900(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuad( //Popover Power Bar BG
                    new PosXY(sidebarWidth + 12, h - 16),
                    new PosXY(w - 12, h - 12),
                    UIColor.matBlueGrey700(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawQuad( //Popover Power Bar FG
                    new PosXY(sidebarWidth + 12, h - 16),
                    new PosXY(((w - sidebarWidth - 24) * te.power / te.maxPower) + sidebarWidth + 12, h - 12),
                    UIColor.matBlue(highlightedSidebarButtonTiming)
            );

            GuiUtils.drawString(GuiUtils.font, sidebarWidth + 14, h - 58, String.format("%012.2f / %012.2f %s", te.power, te.maxPower, StatCollector.translateToLocal("gui.mff:fe")), UIColor.matWhite(highlightedSidebarButtonTiming));
            GuiUtils.drawString(GuiUtils.font, sidebarWidth + 14, h - 38, String.format("%s %s / t", formatter.format(te.powerUsage), StatCollector.translateToLocal("gui.mff:fe")), UIColor.matWhite(highlightedSidebarButtonTiming));
        }

        if (highlightedSidebarButtonTiming + deltaTime * 8 <= 1) {
            highlightedSidebarButtonTiming += deltaTime * 8;
        } else {
            highlightedSidebarButtonTiming = 1;
        }
        //</editor-fold>

        //24 px gap between items
        GuiUtils.drawString(GuiUtils.font, 8, 28, StatCollector.translateToLocal("gui.mff:info"), UIColor.matWhite());
        GuiUtils.drawString(GuiUtils.font, 8, 52, StatCollector.translateToLocal("gui.mff:sizing"), UIColor.matWhite());
        GuiUtils.drawString(GuiUtils.font, 8, 76, StatCollector.translateToLocal("gui.mff:security"), UIColor.matWhite());
        GuiUtils.drawString(GuiUtils.font, 8, 100, StatCollector.translateToLocal("gui.mff:upgrades"), UIColor.matWhite());
        GuiUtils.drawString(GuiUtils.font, 8, 124, StatCollector.translateToLocal("gui.mff:powerStats"), UIColor.matWhite());

        //Draw power meter at the bottom of the sidebar
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

        GuiUtils.drawString(GuiUtils.font, 8, h - 34, String.format("%s: %06.2f%%", StatCollector.translateToLocal("gui.mff:power"), te.power / te.maxPower * 100), UIColor.matWhite());
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

        GuiUtils.drawString(GuiUtils.font, 8, 1, StatCollector.translateToLocal("gui.mff:powerStats"), UIColor.matWhite());
        //</editor-fold>
    }

    protected void checkAndDrawDebug() {

        if (Keyboard.getEventKey() == debugKeycode) {
            if (Keyboard.getEventKeyState()) {
                if (!debugKeyHit) {
                    debugKeyHit = true;
                    GuiUtils.debugEnabled = !GuiUtils.debugEnabled;
                }
            } else {
                debugKeyHit = false;
            }
        }

        if (GuiUtils.debugEnabled) {

            GuiUtils.drawQuad(
                    new PosXY(24, h - 96),
                    new PosXY(w - 24, h - 24),
                    UIColor.matGrey900(0.5)
            );

            GuiUtils.drawString(GuiUtils.font, 26, h - 48, String.format("%d FPS (%.3fs Delta Time)", Minecraft.getDebugFPS(), deltaTime), UIColor.matWhite()); //Draw FPS and Delta Time
            GuiUtils.drawString(GuiUtils.font, 26, h - 72, String.format("Mouse Pos: %d, %d | LMB Down: %b", mx, my, lmbDown), UIColor.matWhite()); //Draw Mouse Pos
            GuiUtils.drawString(GuiUtils.font, 26, h - 96, String.format("Tab ID: %d", tabID), UIColor.matWhite()); //Draw Tab ID

            GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 0.25); //Draw crosshair at mouse pos
            GL11.glLineWidth(2f);
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2d(0, my);
                GL11.glVertex2d(w, my);
            }
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2d(mx, 0);
                GL11.glVertex2d(mx, h);
            }
            GL11.glEnd();

        }

    }

}
