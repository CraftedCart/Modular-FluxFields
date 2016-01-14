package io.github.craftedcart.MFF.client.gui;

import com.google.common.collect.Lists;
import io.github.craftedcart.MFF.client.gui.guiutils.AnchorPoint;
import io.github.craftedcart.MFF.client.gui.guiutils.PosXY;
import io.github.craftedcart.MFF.client.gui.guiutils.UIAction;
import io.github.craftedcart.MFF.client.gui.guiutils.UILineGraph;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestPowerStats;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 29/12/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorPowerStats extends GuiFFProjectorBase {

    public GuiFFProjectorPowerStats(EntityPlayer player, TEFFProjector te) {

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
        tabID = 5; //Power Stats Tab ID
        NetworkHandler.network.sendToServer(new MessageRequestPowerStats(te.getPos(), player));

        final UILineGraph powerUsagePerTickForPastMinuteLineGraph = new UILineGraph(getWorkspace(),
                "powerUsagePerTickForPastMinuteLineGraph",
                new PosXY(24, 24),
                new PosXY(-24, -24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0.3333));
        powerUsagePerTickForPastMinuteLineGraph.setYAxisLabelSuffix(String.format("%s / %s", StatCollector.translateToLocal("gui.mff:fe"), StatCollector.translateToLocal("gui.mff:t")));
        powerUsagePerTickForPastMinuteLineGraph.setXAxisLabelSuffix(StatCollector.translateToLocal("gui.mff:secsAgo"));
        powerUsagePerTickForPastMinuteLineGraph.setRightLabelOverride(StatCollector.translateToLocal("gui.mff:now"));
        powerUsagePerTickForPastMinuteLineGraph.setXAxisMinValue(60);
        powerUsagePerTickForPastMinuteLineGraph.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {

                List<Double> graphPoints = new ArrayList<Double>();

                graphPoints.addAll(te.powerUsagePerTickForPastMinute);

                while (graphPoints.size() < 1200) {
                    graphPoints.add(0d);
                }

                powerUsagePerTickForPastMinuteLineGraph.setGraphPoints(Lists.reverse(graphPoints));
            }
        });

        final UILineGraph powerUsagePerSecondForPastHalfHourLineGraph = new UILineGraph(getWorkspace(),
                "powerUsagePerSecondForPastHalfHourLineGraph",
                new PosXY(24, 24),
                new PosXY(-24, -24),
                new AnchorPoint(0, 0.3333),
                new AnchorPoint(1, 0.6666));
        powerUsagePerSecondForPastHalfHourLineGraph.setYAxisLabelSuffix(String.format("%s / %s", StatCollector.translateToLocal("gui.mff:fe"), StatCollector.translateToLocal("gui.mff:t")));
        powerUsagePerSecondForPastHalfHourLineGraph.setXAxisLabelSuffix(StatCollector.translateToLocal("gui.mff:minsAgo"));
        powerUsagePerSecondForPastHalfHourLineGraph.setRightLabelOverride(StatCollector.translateToLocal("gui.mff:now"));
        powerUsagePerSecondForPastHalfHourLineGraph.setXAxisMinValue(30);
        powerUsagePerSecondForPastHalfHourLineGraph.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {

                List<Double> graphPoints = new ArrayList<Double>();

                graphPoints.addAll(te.powerUsagePerSecondForPastHalfHour);

                while (graphPoints.size() < 1800) {
                    graphPoints.add(0d);
                }

                powerUsagePerSecondForPastHalfHourLineGraph.setGraphPoints(Lists.reverse(graphPoints));
            }
        });

    }

//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//
//        super.drawScreen(mouseX, mouseY, partialTicks);
////        <editor-fold desc="OLD">
////        drawSidebarAndTopBar(te, player);
////
////        GL11.glLineWidth(2f);
////
////        int graphHeight = ((h - 64) / 4) - 48;
////        int graphWidth = (w - 20) - (sidebarWidth + 200);
////
////        //<editor-fold desc="Graph 1">
////        //Draw FE value & time at the mouse pos
////        if (mx >= sidebarWidth + 200 && mx <= w - 20) {
////            if (my >= 48 && my <= graphHeight + 48) {
////                //Draw FE value
////                GL11.glColor4d(UIColor.matOrange().r, UIColor.matOrange().g, UIColor.matOrange().b, 1); //Draw horizontal line
////                GL11.glBegin(GL11.GL_LINE_STRIP);
////                {
////                    GL11.glVertex2d(sidebarWidth + 200, my);
////                    GL11.glVertex2d(w - 20, my);
////                }
////                GL11.glEnd();
////
////                double mousePercentageHeight = (my - 48f) / graphHeight;
////                GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, my - 10, String.format("%s %s / t",
////                        formatter.format(Collections.max(te.powerUsagePerTickForPastMinute) * (1 - mousePercentageHeight)), StatCollector.translateToLocal("gui.mff:fe")), UIColor.matOrange());
////
////                //Draw time
////                //Draw vertical line
////                GL11.glBegin(GL11.GL_LINE_STRIP);
////                {
////                    GL11.glVertex2d(mx, 48);
////                    GL11.glVertex2d(mx, graphHeight + 48);
////                }
////                GL11.glEnd();
////
////                double mousePercentageWidth = (mx - sidebarWidth - 220f) / graphWidth;
////                GuiUtils.drawString(GuiUtils.font, mx, graphHeight + 50, String.format("-%.2f%s", 60 * (1 - mousePercentageWidth), StatCollector.translateToLocal("gui.mff:secs")), UIColor.matOrange());
////            }
////        }
////
////        //Draw graph outline
////        GL11.glColor4d(UIColor.matGrey300().r, UIColor.matGrey300().g, UIColor.matGrey300().b, 1);
////        GL11.glBegin(GL11.GL_LINE_STRIP);
////        {
////            GL11.glVertex2d(sidebarWidth + 200, 48);
////            GL11.glVertex2d(sidebarWidth + 200, graphHeight + 48);
////            GL11.glVertex2d(w - 20, (h - 64) / 4);
////        }
////        GL11.glEnd();
////
////        //Draw the graph
////        GL11.glBegin(GL11.GL_LINE_STRIP);
////        {
////            GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 1);
////            for (int i = 0; i < 1200; i++) {
////                if (te.powerUsagePerTickForPastMinute.size() - 1 < i) {
////                    break;
////                }
////
////                double percentageHeightOnGraph = te.powerUsagePerTickForPastMinute.get(i) / Collections.max(te.powerUsagePerTickForPastMinute);
////
////                GL11.glVertex2d((1 - (i / 1200f)) * graphWidth + sidebarWidth + 200, 48 + graphHeight * (1 - percentageHeightOnGraph));
////
////            }
////        }
////        GL11.glEnd();
////
////        //Draw min and max FE values on the left of the graph
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, 44, String.format("%s %s / t",
////                formatter.format(Collections.max(te.powerUsagePerTickForPastMinute)), StatCollector.translateToLocal("gui.mff:fe")), UIColor.matGrey900());
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, (h - 64) / 4 - 22, String.format("0 %s / t", StatCollector.translateToLocal("gui.mff:fe")), UIColor.matGrey900());
////
////        //Draw timing on the bottom of the graph
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 202, graphHeight + 50, StatCollector.translateToLocal("gui.mff:1MinAgo"), UIColor.matGrey900());
////        GuiUtils.drawString(GuiUtils.font, w - 22 - GuiUtils.font.getWidth(StatCollector.translateToLocal("gui.mff:now")), graphHeight + 50,
////                StatCollector.translateToLocal("gui.mff:now"), UIColor.matGrey900());
////        //</editor-fold>
////
////        //<editor-fold desc="Graph 2">
////        //Draw FE value & time at the mouse pos
////        if (mx >= sidebarWidth + 200 && mx <= w - 20) {
////            if (my >= 48 + graphHeight + 48 && my <= graphHeight + 48 + graphHeight + 48) {
////                //Draw FE value
////                GL11.glColor4d(UIColor.matOrange().r, UIColor.matOrange().g, UIColor.matOrange().b, 1); //Draw horizontal line
////                GL11.glBegin(GL11.GL_LINE_STRIP);
////                {
////                    GL11.glVertex2d(sidebarWidth + 200, my);
////                    GL11.glVertex2d(w - 20, my);
////                }
////                GL11.glEnd();
////
////                double mousePercentageHeight = (my - 48f - graphHeight - 48) / graphHeight;
////                GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, my - 10, String.format("%s %s / t",
////                        formatter.format(Collections.max(te.powerUsagePerSecondForPastHalfHour) * (1 - mousePercentageHeight)), StatCollector.translateToLocal("gui.mff:fe")), UIColor.matOrange());
////
////                //Draw time
////                //Draw vertical line
////                GL11.glBegin(GL11.GL_LINE_STRIP);
////                {
////                    GL11.glVertex2d(mx, 48 + graphHeight + 48);
////                    GL11.glVertex2d(mx, graphHeight + 48 + graphHeight + 48);
////                }
////                GL11.glEnd();
////
////                double mousePercentageWidth = (mx - sidebarWidth - 220f) / graphWidth;
////                GuiUtils.drawString(GuiUtils.font, mx, graphHeight + 50 + graphHeight + 48, String.format("-%.2f%s",
////                        1800 * (1 - mousePercentageWidth) / 60, StatCollector.translateToLocal("gui.mff:mins")), UIColor.matOrange());
////            }
////        }
////
////        //Draw graph outline
////        GL11.glColor4d(UIColor.matGrey300().r, UIColor.matGrey300().g, UIColor.matGrey300().b, 1);
////        GL11.glBegin(GL11.GL_LINE_STRIP);
////        {
////            GL11.glVertex2d(sidebarWidth + 200, 48 + graphHeight + 48);
////            GL11.glVertex2d(sidebarWidth + 200, graphHeight + 48 + graphHeight + 48);
////            GL11.glVertex2d(w - 20, (h - 64) / 4 + graphHeight + 48);
////        }
////        GL11.glEnd();
////
////        //Draw the graph
////        GL11.glBegin(GL11.GL_LINE_STRIP);
////        {
////            GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 1);
////            for (int i = 0; i < 1800; i++) {
////                if (te.powerUsagePerSecondForPastHalfHour.size() - 1 < i) {
////                    break;
////                }
////
////                double percentageHeightOnGraph = te.powerUsagePerSecondForPastHalfHour.get(i) / Collections.max(te.powerUsagePerSecondForPastHalfHour);
////
////                GL11.glVertex2d((1 - (i / 1800f)) * graphWidth + sidebarWidth + 200, 48 + graphHeight * (1 - percentageHeightOnGraph) + graphHeight + 48);
////
////            }
////        }
////        GL11.glEnd();
////
////        //Draw min and max FE values on the left of the graph
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, 44 + graphHeight + 48, String.format("%s %s / t",
////                formatter.format(Collections.max(te.powerUsagePerSecondForPastHalfHour)), StatCollector.translateToLocal("gui.mff:fe")), UIColor.matGrey900());
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 24, (h - 64) / 4 - 22 + graphHeight + 48, String.format("0 %s / t", StatCollector.translateToLocal("gui.mff:fe")), UIColor.matGrey900());
////
////        //Draw timing on the bottom of the graph
////        GuiUtils.drawString(GuiUtils.font, sidebarWidth + 202, graphHeight + 50 + graphHeight + 48, StatCollector.translateToLocal("gui.mff:30MinsAgo"), UIColor.matGrey900());
////        GuiUtils.drawString(GuiUtils.font, w - 22 - GuiUtils.font.getWidth(StatCollector.translateToLocal("gui.mff:now")), graphHeight + 50 + graphHeight + 48,
////                StatCollector.translateToLocal("gui.mff:now"), UIColor.matGrey900());
////        //</editor-fold>
////
////        checkAndDrawDebug();
////
////        GlStateManager.enableTexture2D();
////        GlStateManager.disableBlend();
////        </editor-fold>
//
//    }

}
