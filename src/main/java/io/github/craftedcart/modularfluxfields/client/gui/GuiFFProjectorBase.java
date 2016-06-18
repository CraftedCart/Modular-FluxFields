package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.mcliquidui.component.*;
import io.github.craftedcart.mcliquidui.util.*;
import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.handler.NetworkHandler;
import io.github.craftedcart.modularfluxfields.network.MessageRequestOpenGui;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 08/01/2016 (DD/MM/YYYY)
 */
public class GuiFFProjectorBase extends UIDisplay {

    int sidebarWidth = 150; //The width of the sidebar
    private final UIComponent workspace = new UIComponent(getRootComponent(),
            "workspace",
            new PosXY(sidebarWidth, 24),
            new PosXY(0, 0),
            new AnchorPoint(0, 0),
            new AnchorPoint(1, 1));

    protected TEFFProjector te;
    protected EntityPlayer player;

    private UILabel topBarTitle;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onInit() {

        super.onInit();

        getRootComponent().setPanelBackgroundColor(UIColor.matWhite());
        
        workspace.setPanelBackgroundColor(UIColor.transparent());

        //<editor-fold desc="Sidebar">
        final UIComponent sidebarPanel = new UIComponent(getRootComponent(),
                "sidebarPanel",
                new PosXY(0, 24),
                new PosXY(sidebarWidth, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(0, 1));
        sidebarPanel.setPanelBackgroundColor(UIColor.matBlueGrey());

        final UIGradientPanel sidebarShadow = new UIGradientPanel(sidebarPanel,
                "shadow",
                new PosXY(0, 0),
                new PosXY(4, 0),
                new AnchorPoint(1, 0),
                new AnchorPoint(1, 1));
        sidebarShadow.setHorizontalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        //<editor-fold desc="Sidebar Info Button">
        final UITextButton sidebarInfoButton = new UITextButton(sidebarPanel,
                "infoButton",
                new PosXY(0, 24),
                new PosXY(0, 48),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarInfoButton.setPanelDefaultBackgroundColor(UIColor.matBlueGrey300(0));
        sidebarInfoButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        sidebarInfoButton.setPanelHitBackgroundColor(UIColor.matBlue());
        sidebarInfoButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:info"));
        sidebarInfoButton.uiLabel.setTextColor(UIColor.matWhite());
        sidebarInfoButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                player.openGui(ModModularFluxFields.instance, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI,te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            }
        });

        final UIGradientPanel sidebarInfoButtonTopShadow = new UIGradientPanel(sidebarInfoButton,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarInfoButtonTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UIGradientPanel sidebarInfoButtonBottomShadow = new UIGradientPanel(sidebarInfoButton,
                "bottomShadow",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarInfoButtonBottomShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        sidebarInfoButton.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarInfoButton.isMouseOver() || sidebarInfoButton.isMouseOverChildComponent()) {
                    sidebarInfoButtonTopShadow.setVisible(true);
                    sidebarInfoButtonBottomShadow.setVisible(true);
                } else {
                    sidebarInfoButtonTopShadow.setVisible(false);
                    sidebarInfoButtonBottomShadow.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Sidebar Sizing Button">
        final UITextButton sidebarSizingButton = new UITextButton(sidebarPanel,
                "sizingButton",
                new PosXY(0, 48),
                new PosXY(0, 72),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarSizingButton.setPanelDefaultBackgroundColor(UIColor.matBlueGrey300(0));
        sidebarSizingButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        sidebarSizingButton.setPanelHitBackgroundColor(UIColor.matBlue());
        sidebarSizingButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:sizing"));
        sidebarSizingButton.uiLabel.setTextColor(UIColor.matWhite());
        sidebarSizingButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                player.openGui(ModModularFluxFields.instance, GuiHandler.FFProjector_Sizing_TILE_ENTITY_GUI, te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            }
        });

        final UIGradientPanel sidebarSizingButtonTopShadow = new UIGradientPanel(sidebarSizingButton,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarSizingButtonTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UIGradientPanel sidebarSizingButtonBottomShadow = new UIGradientPanel(sidebarSizingButton,
                "bottomShadow",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarSizingButtonBottomShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        sidebarSizingButton.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarSizingButton.isMouseOver() || sidebarSizingButton.isMouseOverChildComponent()) {
                    sidebarSizingButtonTopShadow.setVisible(true);
                    sidebarSizingButtonBottomShadow.setVisible(true);
                } else {
                    sidebarSizingButtonTopShadow.setVisible(false);
                    sidebarSizingButtonBottomShadow.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Sidebar Security Button">
        final UITextButton sidebarSecurityButton = new UITextButton(sidebarPanel,
                "securityButton",
                new PosXY(0, 72),
                new PosXY(0, 96),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarSecurityButton.setPanelDefaultBackgroundColor(UIColor.matBlueGrey300(0));
        sidebarSecurityButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        sidebarSecurityButton.setPanelHitBackgroundColor(UIColor.matBlue());
        sidebarSecurityButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:security"));
        sidebarSecurityButton.uiLabel.setTextColor(UIColor.matWhite());
        sidebarSecurityButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                player.openGui(ModModularFluxFields.instance, GuiHandler.FFProjector_Security_TILE_ENTITY_GUI,te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            }
        });

        final UIGradientPanel sidebarSecurityButtonTopShadow = new UIGradientPanel(sidebarSecurityButton,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarSecurityButtonTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UIGradientPanel sidebarSecurityButtonBottomShadow = new UIGradientPanel(sidebarSecurityButton,
                "bottomShadow",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarSecurityButtonBottomShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        sidebarSecurityButton.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarSecurityButton.isMouseOver() || sidebarSecurityButton.isMouseOverChildComponent()) {
                    sidebarSecurityButtonTopShadow.setVisible(true);
                    sidebarSecurityButtonBottomShadow.setVisible(true);
                } else {
                    sidebarSecurityButtonTopShadow.setVisible(false);
                    sidebarSecurityButtonBottomShadow.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Sidebar Upgrades Button">
        final UITextButton sidebarUpgradesButton = new UITextButton(sidebarPanel,
                "upgradesButton",
                new PosXY(0, 96),
                new PosXY(0, 120),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarUpgradesButton.setPanelDefaultBackgroundColor(UIColor.matBlueGrey300(0));
        sidebarUpgradesButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        sidebarUpgradesButton.setPanelHitBackgroundColor(UIColor.matBlue());
        sidebarUpgradesButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:upgrades"));
        sidebarUpgradesButton.uiLabel.setTextColor(UIColor.matWhite());
        sidebarUpgradesButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                NetworkHandler.network.sendToServer(new MessageRequestOpenGui(te.getPos(), player, GuiHandler.FFProjector_Upgrades_TILE_ENTITY_GUI));
            }
        });

        final UIGradientPanel sidebarUpgradesButtonTopShadow = new UIGradientPanel(sidebarUpgradesButton,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarUpgradesButtonTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UIGradientPanel sidebarUpgradesButtonBottomShadow = new UIGradientPanel(sidebarUpgradesButton,
                "bottomShadow",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarUpgradesButtonBottomShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        sidebarUpgradesButton.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarUpgradesButton.isMouseOver() || sidebarUpgradesButton.isMouseOverChildComponent()) {
                    sidebarUpgradesButtonTopShadow.setVisible(true);
                    sidebarUpgradesButtonBottomShadow.setVisible(true);
                } else {
                    sidebarUpgradesButtonTopShadow.setVisible(false);
                    sidebarUpgradesButtonBottomShadow.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Sidebar Power Statistics Button">
        final UITextButton sidebarPowerStatsButton = new UITextButton(sidebarPanel,
                "powerStatsButton",
                new PosXY(0, 120),
                new PosXY(0, 144),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarPowerStatsButton.setPanelDefaultBackgroundColor(UIColor.matBlueGrey300(0));
        sidebarPowerStatsButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
        sidebarPowerStatsButton.setPanelHitBackgroundColor(UIColor.matBlue());
        sidebarPowerStatsButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:powerStats"));
        sidebarPowerStatsButton.uiLabel.setTextColor(UIColor.matWhite());
        sidebarPowerStatsButton.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                player.openGui(ModModularFluxFields.instance, GuiHandler.FFProjector_PowerStats_TILE_ENTITY_GUI,te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            }
        });

        final UIGradientPanel sidebarPowerStatsButtonTopShadow = new UIGradientPanel(sidebarPowerStatsButton,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarPowerStatsButtonTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UIGradientPanel sidebarPowerStatsButtonBottomShadow = new UIGradientPanel(sidebarPowerStatsButton,
                "bottomShadow",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarPowerStatsButtonBottomShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        sidebarPowerStatsButton.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarPowerStatsButton.isMouseOver() || sidebarPowerStatsButton.isMouseOverChildComponent()) {
                    sidebarPowerStatsButtonTopShadow.setVisible(true);
                    sidebarPowerStatsButtonBottomShadow.setVisible(true);
                } else {
                    sidebarPowerStatsButtonTopShadow.setVisible(false);
                    sidebarPowerStatsButtonBottomShadow.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Sidebar Power HoverOver">
        final UIComponent sidebarPowerHoverOver = new UIComponent(sidebarPanel,
                "powerHoverOver",
                new PosXY(0, -32),
                new PosXY(0, 0),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarPowerHoverOver.setPanelDefaultBackgroundColor(UIColor.matGrey900(0));
        sidebarPowerHoverOver.setPanelActiveBackgroundColor(UIColor.matGrey900());
        sidebarPowerHoverOver.setPanelHitBackgroundColor(UIColor.matGrey900());

        final UIGradientPanel sidebarPowerHoverOverTopShadow = new UIGradientPanel(sidebarPowerHoverOver,
                "topShadow",
                new PosXY(0, -2),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarPowerHoverOverTopShadow.setVerticalGradient(UIColor.matGrey900(0), UIColor.matGrey900());

        final UILabel sidebarPowerHoverOverLabel = new UILabel(sidebarPowerHoverOver,
                "powerLabel",
                new PosXY(12, 1),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        sidebarPowerHoverOverLabel.setTextColor(UIColor.matWhite());
        sidebarPowerHoverOverLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverLabel.setText(String.format("%06.2f%% %s", te.power / te.maxPower * 100, StatCollector.translateToLocal("gui.modularfluxfields:fe")));
            }
        });

        final UIProgressBar sidebarPowerHoverOverBar = new UIProgressBar(sidebarPowerHoverOver,
                "powerBar",
                new PosXY(4, 22),
                new PosXY(-4, 26),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        sidebarPowerHoverOverBar.setPanelBackgroundColor(UIColor.matBlueGrey700());
        sidebarPowerHoverOverBar.setPanelForegroundColor(UIColor.matBlueGrey300());
        sidebarPowerHoverOverBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverBar.setProgress(te.power / te.maxPower);
            }
        });

        final UICustomQuad sidebarPowerHoverOverJoiner = new UICustomQuad(sidebarPowerHoverOver,
                "hoverOverJoiner",
                new PosXY(0, 0),
                new PosXY(8, -60),
                new PosXY(8, -8),
                new PosXY(0, 0),
                new AnchorPoint(1, 0),
                new AnchorPoint(1, 1));

        final UIComponent sidebarPowerHoverOverPopUp = new UIComponent(rootComponent,
                "sidebarPowerHoverOverPopUp",
                new PosXY(sidebarWidth + 8, -88),
                new PosXY(-8, -8),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarPowerHoverOverPopUp.setVisible(false);

        sidebarPowerHoverOver.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (sidebarPowerHoverOver.isMouseOver() || sidebarPowerHoverOver.isMouseOverChildComponent()) {
                    sidebarPowerHoverOverTopShadow.setVisible(true);
                    sidebarPowerHoverOverPopUp.setVisible(true);
                    sidebarPowerHoverOverJoiner.setVisible(true);
                } else {
                    sidebarPowerHoverOverTopShadow.setVisible(false);
                    sidebarPowerHoverOverPopUp.setVisible(false);
                    sidebarPowerHoverOverJoiner.setVisible(false);
                }
            }
        });

        final UIProgressBar sidebarPowerHoverOverPopUpBar = new UIProgressBar(sidebarPowerHoverOverPopUp,
                "powerBar",
                new PosXY(4, -8),
                new PosXY(-4, -4),
                new AnchorPoint(0, 1),
                new AnchorPoint(1, 1));
        sidebarPowerHoverOverPopUpBar.setPanelBackgroundColor(UIColor.matBlueGrey700());
        sidebarPowerHoverOverPopUpBar.setPanelForegroundColor(UIColor.matBlue());
        sidebarPowerHoverOverPopUpBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverPopUpBar.setProgress(te.power / te.maxPower);
            }
        });

        final UILabel sidebarPowerHoverOverPopUpPowerTitle = new UILabel(sidebarPowerHoverOverPopUp,
                "powerTitle",
                new PosXY(8, -78),
                new AnchorPoint(0, 1),
                GuiUtils.font);
        sidebarPowerHoverOverPopUpPowerTitle.setTextColor(UIColor.matWhite());
        sidebarPowerHoverOverPopUpPowerTitle.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverPopUpPowerTitle.setText(StatCollector.translateToLocal("gui.modularfluxfields:power"));
            }
        });

        final UILabel sidebarPowerHoverOverPopUpPowerLabel = new UILabel(sidebarPowerHoverOverPopUp,
                "powerLabel",
                new PosXY(8, -54),
                new AnchorPoint(0, 1),
                GuiUtils.font);
        sidebarPowerHoverOverPopUpPowerLabel.setTextColor(UIColor.matWhite());
        sidebarPowerHoverOverPopUpPowerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverPopUpPowerLabel.setText(String.format("%012.2f / %012.2f %s", te.power, te.maxPower, StatCollector.translateToLocal("FE")));
            }
        });

        final UILabel sidebarPowerHoverOverPopUpPowerUsageLabel = new UILabel(sidebarPowerHoverOverPopUp,
                "powerUsageLabel",
                new PosXY(8, -30),
                new AnchorPoint(0, 1),
                GuiUtils.font);
        sidebarPowerHoverOverPopUpPowerUsageLabel.setTextColor(UIColor.matWhite());
        sidebarPowerHoverOverPopUpPowerUsageLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sidebarPowerHoverOverPopUpPowerUsageLabel.setText(String.format("%.2f %s / %s", te.powerUsage, StatCollector.translateToLocal("gui.modularfluxfields:fe"), StatCollector.translateToLocal("gui.modularfluxfields:t")));
            }
        });
        //</editor-fold>

        //</editor-fold>

        //<editor-fold desc="Top Bar">
        final UIComponent topBar = new UIComponent(getRootComponent(),
                "topBar",
                new PosXY(0, 0),
                new PosXY(0, 24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        topBar.setPanelBackgroundColor(UIColor.matBlue());

        final UIGradientPanel topBarShadow = new UIGradientPanel(topBar,
                "shadow",
                new PosXY(0, 24),
                new PosXY(0, 28),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        topBarShadow.setVerticalGradient(UIColor.matGrey900(), UIColor.matGrey900(0));

        topBarTitle = new UILabel(topBar,
                "title",
                new PosXY(12, 2),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        topBarTitle.setTextColor(UIColor.matWhite());
        //</editor-fold>

    }

    UIComponent getWorkspace() {
        return workspace;
    }

    void setTitle(String title) {
        topBarTitle.setText(title);
    }

}
