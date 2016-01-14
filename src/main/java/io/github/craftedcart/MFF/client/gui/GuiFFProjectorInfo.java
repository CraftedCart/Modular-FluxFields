package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.client.gui.guiutils.*;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 30/11/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorInfo extends GuiFFProjectorBase {

    public GuiFFProjectorInfo(EntityPlayer player, TEFFProjector te) {
        this.te = te;
        this.player = player;
    }

    @Override
    public void onInit() {
        super.onInit();

        setTitle(StatCollector.translateToLocal("gui.mff:info"));

        final UILabel powerTitleLabel = new UILabel(getWorkspace(),
                "powerTitleLabel",
                new PosXY(24, 24),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerTitleLabel.setText(StatCollector.translateToLocal("gui.mff:power"));

        final UILabel powerLabel = new UILabel(getWorkspace(),
                "powerLabel",
                new PosXY(24, 24),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerLabel.setText(String.format("%012.2f / %012.2f %s (%06.2f%%)", te.power, te.maxPower, StatCollector.translateToLocal("gui.mff:fe"), te.power / te.maxPower * 100));
            }
        });

        final UIProgressBar powerBar = new UIProgressBar(getWorkspace(),
                "powerBar",
                new PosXY(24, 48),
                new PosXY(-24, 52),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        powerBar.setPanelBackgroundColor(UIColor.matGrey900());
        powerBar.setPanelForegroundColor(UIColor.matBlue());
        powerBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerBar.setProgress(te.power / te.maxPower);
            }
        });

        final UILabel powerUsageTitleLabel = new UILabel(getWorkspace(),
                "powerUsageTitleLabel",
                new PosXY(24, 52),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerUsageTitleLabel.setText(StatCollector.translateToLocal("gui.mff:powerUsage"));

        final UILabel powerUsageLabel = new UILabel(getWorkspace(),
                "powerUsageLabel",
                new PosXY(24, 52),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerUsageLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerUsageLabel.setText(String.format("%.2f %s / %s", te.powerUsage, StatCollector.translateToLocal("gui.mff:fe"), StatCollector.translateToLocal("gui.mff:t")));
            }
        });

        final UILabel sizingTitleLabel = new UILabel(getWorkspace(),
                "sizingTitleLabel",
                new PosXY(24, 76),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        sizingTitleLabel.setText(StatCollector.translateToLocal("gui.mff:sizing"));

        final UILabel sizingLabel = new UILabel(getWorkspace(),
                "sizingLabel",
                new PosXY(24, 76),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        sizingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sizingLabel.setText(String.format("XYZ: %d, %d, %d", te.maxX - te.minX + 1, te.maxY - te.minY + 1, te.maxZ - te.minZ + 1));
            }
        });

        final UILabel ownerTitleLabel = new UILabel(getWorkspace(),
                "ownerTitleLabel",
                new PosXY(24, 100),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        ownerTitleLabel.setText(StatCollector.translateToLocal("gui.mff:sizing"));

        final UILabel ownerLabel = new UILabel(getWorkspace(),
                "ownerLabel",
                new PosXY(24, 100),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        ownerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                ownerLabel.setText(te.ownerName);
            }
        });

        final UILabel uptimeTitleLabel = new UILabel(getWorkspace(),
                "ownerTitleLabel",
                new PosXY(24, 124),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        uptimeTitleLabel.setText(StatCollector.translateToLocal("gui.mff:uptime"));

        final UILabel uptimeLabel = new UILabel(getWorkspace(),
                "uptimeLabel",
                new PosXY(24, 124),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        uptimeLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                final int seconds = te.uptime / 20;

                final int hr = seconds / 3600;
                final int rem = seconds % 3600;
                final int mn = rem / 60;
                final int sec = rem % 60;
                final String hrStr = (hr < 10 ? "0" : "") + hr;
                final String mnStr = (mn < 10 ? "0" : "") + mn;
                final String secStr = (sec < 10 ? "0" : "") + sec;

                uptimeLabel.setText(String.format("%s : %s : %s", hrStr, mnStr, secStr));
            }
        });

    }

}
