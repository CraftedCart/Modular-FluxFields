package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.*;
import io.github.craftedcart.modularfluxfields.handler.ConfigurationHandler;
import io.github.craftedcart.modularfluxfields.reference.MFFSettings;
import io.github.craftedcart.modularfluxfields.reference.Reference;
import io.github.craftedcart.modularfluxfields.utility.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by CraftedCart on 27/02/2016 (DD/MM/YYYY)
 */
public class GuiMFFSettings extends UIDisplay {

    private boolean isFullscreen = false;

    public GuiMFFSettings() {}

    public GuiMFFSettings(GuiScreen guiScreen) {
        isFullscreen = true;
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void onInit() {

        super.onInit();

        //<editor-fold desc="Setup">
        getRootComponent().setPanelBackgroundColor(UIColor.transparent());

        final UIComponent workspace = new UIComponent(getRootComponent(),
                "workspace",
                new PosXY(0, 0),
                new PosXY(0, 0),
                new AnchorPoint(-1, 0),
                new AnchorPoint(0, 0));
        workspace.setPanelBackgroundColor(UIColor.transparent());
        workspace.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                workspace.setTopLeftAnchor(new AnchorPoint(MathUtils.lerp(workspace.topLeftAnchor.xPercent, 0, GuiUtils.getDelta() * 10), 0));
                workspace.setBottomRightAnchor(new AnchorPoint(MathUtils.lerp(workspace.bottomRightAnchor.xPercent, 1, GuiUtils.getDelta() * 10), 1));
            }
        });

        final UIListBox settingsListBox = new UIListBox(workspace,
                "settingsListBox",
                new PosXY(0, 0),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(isFullscreen ? 1 : 0.75, 1));
        settingsListBox.setPanelBackgroundColor(UIColor.matWhite(0.9));

        final UIGradientPanel settingsShadow = new UIGradientPanel(workspace,
                "settingsShadow",
                new PosXY(0, 0),
                new PosXY(4, 0),
                new AnchorPoint(isFullscreen ? 1 : 0.75, 0),
                new AnchorPoint(isFullscreen ? 1 : 0.75, 1));
        settingsShadow.setHorizontalGradient(UIColor.matGrey900(0.9), UIColor.matGrey900(0));
        //</editor-fold>

        //<editor-fold desc="Settings Title">
        final UIComponent settingsTitleLabelComponent = new UIComponent(null,
                "settingsTitleLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        settingsTitleLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel settingsTitleLabel = new UILabel(settingsTitleLabelComponent,
                "settingsTitleLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        settingsTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:MFFSettings"));

        settingsListBox.addItem("settingsTitleLabelComponent", settingsTitleLabelComponent);
        //</editor-fold>

        //<editor-fold desc="Use high poly models?">
        final UIComponent highPolyComponent = new UIComponent(null,
                "highPolyComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        highPolyComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel highPolyLabel = new UILabel(highPolyComponent,
                "label",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        highPolyLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:useHighPolyModels"));

        final UIToggleBox highPolyToggleBox = new UIToggleBox(highPolyComponent,
                "toggleBox",
                new PosXY(-28, 4),
                new PosXY(-4, -4),
                new AnchorPoint(1, 0),
                new AnchorPoint(1, 1));
        highPolyToggleBox.setValue(MFFSettings.useHighPolyModels);
        highPolyToggleBox.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                MFFSettings.useHighPolyModels = highPolyToggleBox.value;
            }
        });

        settingsListBox.addItem("highPolyComponent", highPolyComponent);
        //</editor-fold>

        //<editor-fold desc="Use GLSL shaders?">
        final UIComponent GLSLComponent = new UIComponent(null,
                "GLSLComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        GLSLComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel GLSLLabel = new UILabel(GLSLComponent,
                "label",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        GLSLLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:useGLSLShaders"));

        final UIToggleBox GLSLToggleBox = new UIToggleBox(GLSLComponent,
                "toggleBox",
                new PosXY(-28, 4),
                new PosXY(-4, -4),
                new AnchorPoint(1, 0),
                new AnchorPoint(1, 1));
        GLSLToggleBox.setValue(MFFSettings.useGLSLShaders);
        GLSLToggleBox.setOnClickAction(new UIAction() {
            @Override
            public void execute() {
                MFFSettings.useGLSLShaders = GLSLToggleBox.value;
            }
        });

        settingsListBox.addItem("GLSLComponent", GLSLComponent);
        //</editor-fold>

        //<editor-fold desc="Divider 1">
        final UIComponent divider1 = new UIComponent(null,
                "divider1",
                new PosXY(0, 0),
                new PosXY(0, 2),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        divider1.setPanelBackgroundColor(UIColor.matGrey900());
        settingsListBox.addItem("divider1", divider1);
        //</editor-fold>

        //<editor-fold desc="About Title">
        final UIComponent aboutTitleLabelComponent = new UIComponent(null,
                "aboutTitleLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        aboutTitleLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel aboutTitleLabel = new UILabel(aboutTitleLabelComponent,
                "aboutTitleLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        aboutTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:modularfluxfieldsAbout"));

        settingsListBox.addItem("aboutTitleLabelComponent", aboutTitleLabelComponent);
        //</editor-fold>

        //<editor-fold desc="modularfluxfields Version">
        final UIComponent versionLabelComponent = new UIComponent(null,
                "versionLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        versionLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel versionLabel = new UILabel(versionLabelComponent,
                "versionLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        versionLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.modularfluxfields:version"), Reference.VERSION));

        settingsListBox.addItem("versionLabelComponent", versionLabelComponent);
        //</editor-fold>

        //<editor-fold desc="Developed by">
        final UIComponent developedByLabelComponent = new UIComponent(null,
                "developedByLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        developedByLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel developedByLabel = new UILabel(developedByLabelComponent,
                "developedByLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        developedByLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:developedBy"));

        settingsListBox.addItem("developedByLabelComponent", developedByLabelComponent);
        //</editor-fold>

        if (Desktop.isDesktopSupported()) { //Check if it's possible to open web page links in a browser

            //<editor-fold desc="View the Website">
            final UITextButton viewWebsiteButton = new UITextButton(null,
                    "viewWebsiteButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            viewWebsiteButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            viewWebsiteButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            viewWebsiteButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            viewWebsiteButton.uiLabel.setTextColor(UIColor.matWhite());
            viewWebsiteButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:viewWebsite"));
            viewWebsiteButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    try {
                        Desktop.getDesktop().browse(new URI(Reference.websitePage));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            settingsListBox.addItem("viewWebsiteButton", viewWebsiteButton);
            //</editor-fold>

            //<editor-fold desc="Spacer1">
            final UIListSpacer spacer1 = new UIListSpacer(null, "spacer1", 2);
            settingsListBox.addItem("spacer1", spacer1);
            //</editor-fold>

            //<editor-fold desc="View on GitHub">
            final UITextButton viewOnGitHubButton = new UITextButton(null,
                    "viewOnGitHubButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            viewOnGitHubButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            viewOnGitHubButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            viewOnGitHubButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            viewOnGitHubButton.uiLabel.setTextColor(UIColor.matWhite());
            viewOnGitHubButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:viewOnGitHub"));
            viewOnGitHubButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    try {
                        Desktop.getDesktop().browse(new URI(Reference.gitHubPage));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            settingsListBox.addItem("viewOnGitHubButton", viewOnGitHubButton);
            //</editor-fold>

            //<editor-fold desc="Spacer2">
            final UIListSpacer spacer2 = new UIListSpacer(null, "spacer2", 2);
            settingsListBox.addItem("spacer2", spacer2);
            //</editor-fold>

            //<editor-fold desc="View the Issue Tracker">
            final UITextButton viewIssueTrackerButton = new UITextButton(null,
                    "viewIssueTrackerButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            viewIssueTrackerButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            viewIssueTrackerButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            viewIssueTrackerButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            viewIssueTrackerButton.uiLabel.setTextColor(UIColor.matWhite());
            viewIssueTrackerButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:viewIssueTracker"));
            viewIssueTrackerButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    try {
                        Desktop.getDesktop().browse(new URI(Reference.issueTrackerPage));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            settingsListBox.addItem("viewIssueTrackerButton", viewIssueTrackerButton);
            //</editor-fold>

            //<editor-fold desc="Spacer3">
            final UIListSpacer spacer3 = new UIListSpacer(null, "spacer3", 2);
            settingsListBox.addItem("spacer3", spacer3);
            //</editor-fold>

            //<editor-fold desc="View the License">
            final UITextButton viewLicenseButton = new UITextButton(null,
                    "viewLicenseButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            viewLicenseButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            viewLicenseButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            viewLicenseButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            viewLicenseButton.uiLabel.setTextColor(UIColor.matWhite());
            viewLicenseButton.uiLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:viewLicense"));
            viewLicenseButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    try {
                        Desktop.getDesktop().browse(new URI(Reference.licensePage));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            settingsListBox.addItem("viewLicenseButton", viewLicenseButton);
            //</editor-fold>

        }

        //<editor-fold desc="modularfluxfields Licence">
        final UIComponent licenseLabelComponent = new UIComponent(null,
                "licenceLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        licenseLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel licenceLabel = new UILabel(licenseLabelComponent,
                "licenceLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        licenceLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:modularfluxfieldsLicence"));

        settingsListBox.addItem("licenceLabelComponent", licenseLabelComponent);
        //</editor-fold>

        //<editor-fold desc="modularfluxfields Licence Exempt Files Title">
        final UIComponent licenseExemptFilesLabelComponent = new UIComponent(null,
                "licenceExemptFilesLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        licenseExemptFilesLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel licenceExemptFilesLabel = new UILabel(licenseExemptFilesLabelComponent,
                "licenceExemptFilesLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        licenceExemptFilesLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:licenceExemptFiles"));

        settingsListBox.addItem("licenceExemptFilesLabelComponent", licenseExemptFilesLabelComponent);
        //</editor-fold>

        //<editor-fold desc="modularfluxfields Licence Exempt Roboto">
        final UILabel licenceExemptRobotoLabel = new UILabel(null,
                "licenceExemptRobotoLabel",
                new PosXY(24, 0),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        licenceExemptRobotoLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:licenceExemptRoboto"));

        settingsListBox.addItem("licenceExemptRobotoLabel", licenceExemptRobotoLabel);
        //</editor-fold>

        //<editor-fold desc="modularfluxfields Licence Exempt Aviators Open Your Eyes">
        final UILabel licenceExemptAviatorsOpenYourEyesLabel = new UILabel(null,
                "licenceExemptAviatorsOpenYourEyesLabel",
                new PosXY(24, 0),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        licenceExemptAviatorsOpenYourEyesLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:licenceExemptAviatorsOpenYourEyes"));

        settingsListBox.addItem("licenceExemptAviatorsOpenYourEyesLabel", licenceExemptAviatorsOpenYourEyesLabel);
        //</editor-fold>

//        //<editor-fold desc="Divider 2">
//        final UIComponent divider2 = new UIComponent(null,
//                "divider2",
//                new PosXY(0, 0),
//                new PosXY(0, 2),
//                new AnchorPoint(0, 0),
//                new AnchorPoint(1, 0));
//        divider2.setPanelBackgroundColor(UIColor.matGrey900());
//        settingsListBox.addItem("divider2", divider2);
//        //</editor-fold>
//
//        //<editor-fold desc="Debug Title">
//        final UIComponent debugLabelComponent = new UIComponent(null,
//                "debugLabelComponent",
//                new PosXY(0, 0),
//                new PosXY(0, 32),
//                new AnchorPoint(0, 0),
//                new AnchorPoint(1, 0));
//        debugLabelComponent.setPanelBackgroundColor(UIColor.transparent());
//
//        final UILabel debugLabel = new UILabel(debugLabelComponent,
//                "debugLabel",
//                new PosXY(24, 6),
//                new AnchorPoint(0, 0),
//                GuiUtils.font);
//        debugLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:debug"));
//
//        settingsListBox.addItem("debugLabelComponent", debugLabelComponent);
//        //</editor-fold>

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        ConfigurationHandler.saveMFFSettings();
    }
}
