package io.github.craftedcart.modularfluxfields.client.gui;

import com.google.common.collect.Lists;
import io.github.craftedcart.modularfluxfields.client.gui.guiutils.AnchorPoint;
import io.github.craftedcart.modularfluxfields.client.gui.guiutils.PosXY;
import io.github.craftedcart.modularfluxfields.client.gui.guiutils.UIAction;
import io.github.craftedcart.modularfluxfields.client.gui.guiutils.UILineGraph;
import io.github.craftedcart.modularfluxfields.handler.NetworkHandler;
import io.github.craftedcart.modularfluxfields.network.MessageRequestPowerStats;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
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
    public void onInit() {
        super.onInit();
        NetworkHandler.network.sendToServer(new MessageRequestPowerStats(te.getPos(), player));

        setTitle(StatCollector.translateToLocal("gui.modularfluxfields:powerStats"));

        final UILineGraph powerUsagePerTickForPastMinuteLineGraph = new UILineGraph(getWorkspace(),
                "powerUsagePerTickForPastMinuteLineGraph",
                new PosXY(24, 24),
                new PosXY(-24, -24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0.3333));
        powerUsagePerTickForPastMinuteLineGraph.setYAxisLabelSuffix(String.format("%s / %s", StatCollector.translateToLocal("gui.modularfluxfields:fe"), StatCollector.translateToLocal("gui.modularfluxfields:t")));
        powerUsagePerTickForPastMinuteLineGraph.setXAxisLabelSuffix(StatCollector.translateToLocal("gui.modularfluxfields:secsAgo"));
        powerUsagePerTickForPastMinuteLineGraph.setRightLabelOverride(StatCollector.translateToLocal("gui.modularfluxfields:now"));
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
        powerUsagePerSecondForPastHalfHourLineGraph.setYAxisLabelSuffix(String.format("%s / %s", StatCollector.translateToLocal("gui.modularfluxfields:fe"), StatCollector.translateToLocal("gui.modularfluxfields:t")));
        powerUsagePerSecondForPastHalfHourLineGraph.setXAxisLabelSuffix(StatCollector.translateToLocal("gui.modularfluxfields:minsAgo"));
        powerUsagePerSecondForPastHalfHourLineGraph.setRightLabelOverride(StatCollector.translateToLocal("gui.modularfluxfields:now"));
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

}
