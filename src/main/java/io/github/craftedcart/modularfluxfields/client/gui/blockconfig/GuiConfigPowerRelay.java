package io.github.craftedcart.modularfluxfields.client.gui.blockconfig;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.*;
import io.github.craftedcart.modularfluxfields.handler.NetworkHandler;
import io.github.craftedcart.modularfluxfields.network.MessageSetInputSide;
import io.github.craftedcart.modularfluxfields.network.MessageSetOutputSide;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerRelay;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by CraftedCart on 21/03/2016 (DD/MM/YYYY)
 */
public class GuiConfigPowerRelay extends UIDisplay {

    private final TEPowerRelay te;
    private final World world;
    private final StoredData storedData = new StoredData();

    public GuiConfigPowerRelay(World world, TEPowerRelay te) {
        this.world = world;
        this.te = te;
        storedData.inputSide = te.inputSide;
        storedData.outputSide = te.outputSide;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onInit() {
        super.onInit();

        getRootComponent().setPanelBackgroundColor(UIColor.transparent());

        final UIListBox workspace = new UIListBox(getRootComponent(),
                "workspace",
                new PosXY(0, 0),
                new PosXY(512, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(0, 1));

        workspace.setPanelBackgroundColor(UIColor.matWhite(0.75));

        final UIGradientPanel workspaceShadow = new UIGradientPanel(rootComponent,
                "shadow",
                new PosXY(512, 0),
                new PosXY(516, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(0, 1));
        workspaceShadow.setHorizontalGradient(UIColor.matGrey900(0.75), UIColor.matGrey900(0));

        //<editor-fold desc="Title">
        final UIComponent titleComponent = new UIComponent(null,
                "titleComponent",
                new PosXY(0, 0),
                new PosXY(0, 24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        titleComponent.setPanelBackgroundColor(UIColor.transparent());
        final UILabel titleLabel = new UILabel(titleComponent,
                "titleLabel",
                new PosXY(24, 4),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        titleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:powerRelayConfig"));
        workspace.addItem("titleComponent", titleComponent);
        //</editor-fold>

        //<editor-fold desc="Power">
        final UIComponent powerComponent = new UIComponent(null,
                "powerComponent",
                new PosXY(0, 0),
                new PosXY(0, 36),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        powerComponent.setPanelBackgroundColor(UIColor.transparent());
        final UILabel powerLabel = new UILabel(powerComponent,
                "powerLabel",
                new PosXY(24, 4),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerLabel.setText(String.format("%s: %.2f / %.2f %s", StatCollector.translateToLocal("gui.modularfluxfields:power"),
                te.power, te.maxPower, StatCollector.translateToLocal("gui.modularfluxfields:fe")));
        powerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerLabel.setText(String.format("%s: %.2f / %.2f %s", StatCollector.translateToLocal("gui.modularfluxfields:power"),
                        te.power, te.maxPower, StatCollector.translateToLocal("gui.modularfluxfields:fe")));
            }
        });
        final UIProgressBar powerBar = new UIProgressBar(powerComponent,
                "powerBar",
                new PosXY(4, 28),
                new PosXY(-4, -4),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 1));
        powerBar.setPanelBackgroundColor(UIColor.matGrey900());
        powerBar.setPanelForegroundColor(UIColor.matBlue());
        powerBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerBar.setProgress(te.power / te.maxPower);
            }
        });
        workspace.addItem("powerComponent", powerComponent);
        //</editor-fold>

        //<editor-fold desc="Input side label">
        final UIComponent inputSideLabelComponent = new UIComponent(null,
                "inputSideLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        inputSideLabelComponent.setPanelBackgroundColor(UIColor.transparent());
        final UILabel inputSideLabel = new UILabel(inputSideLabelComponent,
                "inputSideLabel",
                new PosXY(24, 4),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        inputSideLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.modularfluxfields:inputSide"),
                StatCollector.translateToLocal(storedData.enumFacingToUnlocString(storedData.inputSide))));
        inputSideLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                inputSideLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.modularfluxfields:inputSide"),
                        StatCollector.translateToLocal(storedData.enumFacingToUnlocString(storedData.inputSide))));
            }
        });
        workspace.addItem("inputSideLabelComponent", inputSideLabelComponent);
        //</editor-fold>

        //<editor-fold desc="Input side selector">
        final UIComponent inputSideSelectorComponent = new UIComponent(null,
                "inputSideSelectorComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        inputSideSelectorComponent.setPanelBackgroundColor(UIColor.transparent());

        //<editor-fold desc="+ XYZ">
        final UITextButton inputSidePosXButton = new UITextButton(inputSideSelectorComponent,
                "inputSidePosXButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(0, 0),
                new AnchorPoint(1 / 6d, 1));
        inputSidePosXButton.setPanelDefaultBackgroundColor(UIColor.matRed());
        inputSidePosXButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSidePosXButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSidePosXButton.uiLabel.setText("+ X");
        inputSidePosXButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSidePosXButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSidePosXButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSidePosXButton.uiLabel.setHorizontalAlign(0);
        inputSidePosXButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.EAST);
            }
        });

        final UITextButton inputSidePosYButton = new UITextButton(inputSideSelectorComponent,
                "inputSidePosYButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(2 / 6d, 0),
                new AnchorPoint(3 / 6d, 1));
        inputSidePosYButton.setPanelDefaultBackgroundColor(UIColor.matGreen());
        inputSidePosYButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSidePosYButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSidePosYButton.uiLabel.setText("+ Y");
        inputSidePosYButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSidePosYButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSidePosYButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSidePosYButton.uiLabel.setHorizontalAlign(0);
        inputSidePosYButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.UP);
            }
        });

        final UITextButton inputSidePosZButton = new UITextButton(inputSideSelectorComponent,
                "inputSidePosZButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(4 / 6d, 0),
                new AnchorPoint(5 / 6d, 1));
        inputSidePosZButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
        inputSidePosZButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSidePosZButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSidePosZButton.uiLabel.setText("+ Z");
        inputSidePosZButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSidePosZButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSidePosZButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSidePosZButton.uiLabel.setHorizontalAlign(0);
        inputSidePosZButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.SOUTH);
            }
        });
        //</editor-fold>

        //<editor-fold desc="- XYZ">
        final UITextButton inputSideNegXButton = new UITextButton(inputSideSelectorComponent,
                "inputSideNegXButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(1 / 6d, 0),
                new AnchorPoint(2 / 6d, 1));
        inputSideNegXButton.setPanelDefaultBackgroundColor(UIColor.matRed());
        inputSideNegXButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSideNegXButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSideNegXButton.uiLabel.setText("- X");
        inputSideNegXButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSideNegXButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSideNegXButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSideNegXButton.uiLabel.setHorizontalAlign(0);
        inputSideNegXButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.WEST);
            }
        });

        final UITextButton inputSideNegYButton = new UITextButton(inputSideSelectorComponent,
                "inputSideNegYButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(3 / 6d, 0),
                new AnchorPoint(4 / 6d, 1));
        inputSideNegYButton.setPanelDefaultBackgroundColor(UIColor.matGreen());
        inputSideNegYButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSideNegYButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSideNegYButton.uiLabel.setText("- Y");
        inputSideNegYButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSideNegYButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSideNegYButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSideNegYButton.uiLabel.setHorizontalAlign(0);
        inputSideNegYButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.DOWN);
            }
        });

        final UITextButton inputSideNegZButton = new UITextButton(inputSideSelectorComponent,
                "inputSideNegZButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(5 / 6d, 0),
                new AnchorPoint(1, 1));
        inputSideNegZButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
        inputSideNegZButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        inputSideNegZButton.setPanelHitBackgroundColor(UIColor.matBlue());
        inputSideNegZButton.uiLabel.setText("- Z");
        inputSideNegZButton.uiLabel.setTextColor(UIColor.matWhite());
        inputSideNegZButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        inputSideNegZButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        inputSideNegZButton.uiLabel.setHorizontalAlign(0);
        inputSideNegZButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setInputSide(EnumFacing.NORTH);
            }
        });
        //</editor-fold>

        workspace.addItem("inputSideSelectorComponent", inputSideSelectorComponent);
        //</editor-fold>

        //<editor-fold desc="Output side label">
        final UIComponent outputSideLabelComponent = new UIComponent(null,
                "outputSideLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        outputSideLabelComponent.setPanelBackgroundColor(UIColor.transparent());
        final UILabel outputSideLabel = new UILabel(outputSideLabelComponent,
                "outputSideLabel",
                new PosXY(24, 4),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        outputSideLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.modularfluxfields:outputSide"),
                StatCollector.translateToLocal(storedData.enumFacingToUnlocString(storedData.outputSide))));
        outputSideLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                outputSideLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.modularfluxfields:outputSide"),
                        StatCollector.translateToLocal(storedData.enumFacingToUnlocString(storedData.outputSide))));
            }
        });
        workspace.addItem("outputSideLabelComponent", outputSideLabelComponent);
        //</editor-fold>

        //<editor-fold desc="Output side selector">
        final UIComponent outputSideSelectorComponent = new UIComponent(null,
                "outputSideSelectorComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        outputSideSelectorComponent.setPanelBackgroundColor(UIColor.transparent());

        //<editor-fold desc="+ XYZ">
        final UITextButton outputSidePosXButton = new UITextButton(outputSideSelectorComponent,
                "outputSidePosXButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(0, 0),
                new AnchorPoint(1 / 6d, 1));
        outputSidePosXButton.setPanelDefaultBackgroundColor(UIColor.matRed());
        outputSidePosXButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSidePosXButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSidePosXButton.uiLabel.setText("+ X");
        outputSidePosXButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSidePosXButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSidePosXButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSidePosXButton.uiLabel.setHorizontalAlign(0);
        outputSidePosXButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.EAST);
            }
        });

        final UITextButton outputSidePosYButton = new UITextButton(outputSideSelectorComponent,
                "outputSidePosYButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(2 / 6d, 0),
                new AnchorPoint(3 / 6d, 1));
        outputSidePosYButton.setPanelDefaultBackgroundColor(UIColor.matGreen());
        outputSidePosYButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSidePosYButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSidePosYButton.uiLabel.setText("+ Y");
        outputSidePosYButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSidePosYButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSidePosYButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSidePosYButton.uiLabel.setHorizontalAlign(0);
        outputSidePosYButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.UP);
            }
        });

        final UITextButton outputSidePosZButton = new UITextButton(outputSideSelectorComponent,
                "outputSidePosZButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(4 / 6d, 0),
                new AnchorPoint(5 / 6d, 1));
        outputSidePosZButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
        outputSidePosZButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSidePosZButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSidePosZButton.uiLabel.setText("+ Z");
        outputSidePosZButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSidePosZButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSidePosZButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSidePosZButton.uiLabel.setHorizontalAlign(0);
        outputSidePosZButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.SOUTH);
            }
        });
        //</editor-fold>

        //<editor-fold desc="- XYZ">
        final UITextButton outputSideNegXButton = new UITextButton(outputSideSelectorComponent,
                "outputSideNegXButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(1 / 6d, 0),
                new AnchorPoint(2 / 6d, 1));
        outputSideNegXButton.setPanelDefaultBackgroundColor(UIColor.matRed());
        outputSideNegXButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSideNegXButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSideNegXButton.uiLabel.setText("- X");
        outputSideNegXButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSideNegXButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSideNegXButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSideNegXButton.uiLabel.setHorizontalAlign(0);
        outputSideNegXButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.WEST);
            }
        });

        final UITextButton outputSideNegYButton = new UITextButton(outputSideSelectorComponent,
                "outputSideNegYButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(3 / 6d, 0),
                new AnchorPoint(4 / 6d, 1));
        outputSideNegYButton.setPanelDefaultBackgroundColor(UIColor.matGreen());
        outputSideNegYButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSideNegYButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSideNegYButton.uiLabel.setText("- Y");
        outputSideNegYButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSideNegYButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSideNegYButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSideNegYButton.uiLabel.setHorizontalAlign(0);
        outputSideNegYButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.DOWN);
            }
        });

        final UITextButton outputSideNegZButton = new UITextButton(outputSideSelectorComponent,
                "outputSideNegZButton",
                new PosXY(4, 4),
                new PosXY(-4, -4),
                new AnchorPoint(5 / 6d, 0),
                new AnchorPoint(1, 1));
        outputSideNegZButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
        outputSideNegZButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        outputSideNegZButton.setPanelHitBackgroundColor(UIColor.matBlue());
        outputSideNegZButton.uiLabel.setText("- Z");
        outputSideNegZButton.uiLabel.setTextColor(UIColor.matWhite());
        outputSideNegZButton.uiLabel.setTopLeftPoint(new PosXY(0, 1));
        outputSideNegZButton.uiLabel.setTopLeftAnchor(new AnchorPoint(0.5, 0));
        outputSideNegZButton.uiLabel.setHorizontalAlign(0);
        outputSideNegZButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                storedData.setOutputSide(EnumFacing.NORTH);
            }
        });
        //</editor-fold>

        workspace.addItem("outputSideSelectorComponent", outputSideSelectorComponent);
        //</editor-fold>

    }

    private class StoredData {

        EnumFacing inputSide;
        EnumFacing outputSide;

        String enumFacingToUnlocString(EnumFacing enumFacing) {
            switch (enumFacing) {
                case UP:
                    return "gui.modularfluxfields:posY";
                case DOWN:
                    return "gui.modularfluxfields:negY";
                case NORTH:
                    return "gui.modularfluxfields:negZ";
                case EAST:
                    return "gui.modularfluxfields:posX";
                case SOUTH:
                    return "gui.modularfluxfields:posZ";
                case WEST:
                    return "gui.modularfluxfields:negX";
            }
            return "ERROR";
        }

        void setInputSide(EnumFacing inputSide) {
            this.inputSide = inputSide;
            ((TEPowerRelay) world.getTileEntity(te.getPos())).setInputSide(inputSide);
            NetworkHandler.network.sendToServer(new MessageSetInputSide(te.getPos(), inputSide));
        }

        void setOutputSide(EnumFacing outputSide) {
            this.outputSide = outputSide;
            ((TEPowerRelay) world.getTileEntity(te.getPos())).setOutputSide(outputSide);
            NetworkHandler.network.sendToServer(new MessageSetOutputSide(te.getPos(), outputSide));
        }
        
    }

}
