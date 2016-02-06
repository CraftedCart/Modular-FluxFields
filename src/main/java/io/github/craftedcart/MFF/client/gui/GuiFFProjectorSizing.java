package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.client.gui.guiutils.*;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSaveSizing;
import io.github.craftedcart.MFF.reference.FFProjectorConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 06/02/2016 (DD/MM/YYYY)
 */

public class GuiFFProjectorSizing extends GuiFFProjectorBase {

    public static int x1;
    public static int x2;
    public static int y1;
    public static int y2;
    public static int z1;
    public static int z2;

    public GuiFFProjectorSizing(EntityPlayer player, TEFFProjector te) {
        this.te = te;
        this.player = player;
    }

    @Override
    public void onInit() {

        super.onInit();

        setTitle(StatCollector.translateToLocal("gui.mff:sizing"));

        //<editor-fold desc="X">
        final UILabel xSizingLabel = new UILabel(getWorkspace(),
                "xLabel",
                new PosXY(24, 24),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        xSizingLabel.setText("X");

        final UIDualSlider xDualSlider = new UIDualSlider(getWorkspace(),
                "xDualSlider",
                new PosXY(24, 48),
                new PosXY(-24, 50),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        xDualSlider.minValue = FFProjectorConf.xSizeMin;
        xDualSlider.maxValue = FFProjectorConf.xSizeMax;
        xDualSlider.currentMinValue = te.minX;
        xDualSlider.currentMaxValue = te.maxX;
        x1 = te.minX;
        x2 = te.maxX;
        xDualSlider.setDecimalPlaces(0);
        xDualSlider.setPanelBackgroundColor(UIColor.matGrey300());
        xDualSlider.selectedArea.setPanelBackgroundColor(UIColor.matRed());
        xDualSlider.minHandle.setPanelBackgroundColor(UIColor.matRed());
        xDualSlider.maxHandle.setPanelBackgroundColor(UIColor.matRed());

        xSizingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                xSizingLabel.setText(String.format("X: %.0f - %.0f", xDualSlider.currentMinValue, xDualSlider.currentMaxValue));
            }
        });

        xDualSlider.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                x1 = (int) xDualSlider.currentMinValue;
                x2 = (int) xDualSlider.currentMaxValue;
            }
        });
        //</editor-fold>

        //<editor-fold desc="Y">
        final UILabel ySizingLabel = new UILabel(getWorkspace(),
                "yLabel",
                new PosXY(24, 72),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        ySizingLabel.setText("Y");

        final UIDualSlider yDualSlider = new UIDualSlider(getWorkspace(),
                "yDualSlider",
                new PosXY(24, 96),
                new PosXY(-24, 98),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        yDualSlider.minValue = FFProjectorConf.ySizeMin;
        yDualSlider.maxValue = FFProjectorConf.ySizeMax;
        yDualSlider.currentMinValue = te.minY;
        yDualSlider.currentMaxValue = te.maxY;
        y1 = te.minY;
        y2 = te.maxY;
        yDualSlider.setDecimalPlaces(0);
        yDualSlider.setPanelBackgroundColor(UIColor.matGrey300());
        yDualSlider.selectedArea.setPanelBackgroundColor(UIColor.matGreen());
        yDualSlider.minHandle.setPanelBackgroundColor(UIColor.matGreen());
        yDualSlider.maxHandle.setPanelBackgroundColor(UIColor.matGreen());

        ySizingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                ySizingLabel.setText(String.format("Y: %.0f - %.0f", yDualSlider.currentMinValue, yDualSlider.currentMaxValue));
            }
        });

        yDualSlider.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                y1 = (int) yDualSlider.currentMinValue;
                y2 = (int) yDualSlider.currentMaxValue;
            }
        });
        //</editor-fold>

        //<editor-fold desc="Z">
        final UILabel zSizingLabel = new UILabel(getWorkspace(),
                "zLabel",
                new PosXY(24, 120),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        zSizingLabel.setText("Z");

        final UIDualSlider zDualSlider = new UIDualSlider(getWorkspace(),
                "zDualSlider",
                new PosXY(24, 142),
                new PosXY(-24, 144),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        zDualSlider.minValue = FFProjectorConf.zSizeMin;
        zDualSlider.maxValue = FFProjectorConf.zSizeMax;
        zDualSlider.currentMinValue = te.minZ;
        zDualSlider.currentMaxValue = te.maxZ;
        z1 = te.minZ;
        z2 = te.maxZ;
        zDualSlider.setDecimalPlaces(0);
        zDualSlider.setPanelBackgroundColor(UIColor.matGrey300());
        zDualSlider.selectedArea.setPanelBackgroundColor(UIColor.matBlue());
        zDualSlider.minHandle.setPanelBackgroundColor(UIColor.matBlue());
        zDualSlider.maxHandle.setPanelBackgroundColor(UIColor.matBlue());

        zSizingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                zSizingLabel.setText(String.format("Z: %.0f - %.0f", zDualSlider.currentMinValue, zDualSlider.currentMaxValue));
            }
        });

        zDualSlider.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                z1 = (int) zDualSlider.currentMinValue;
                z2 = (int) zDualSlider.currentMaxValue;
            }
        });

        //</editor-fold>

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        NetworkHandler.network.sendToServer(new MessageFFProjectorGuiSaveSizing(this.te.getPos(), x1, y1, z1, x2, y2, z2));
    }

}
