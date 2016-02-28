package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.client.gui.guiutils.*;
import io.github.craftedcart.MFF.reference.MFFSettings;
import io.github.craftedcart.MFF.utility.MathUtils;
import net.minecraft.util.StatCollector;

/**
 * Created by CraftedCart on 27/02/2016 (DD/MM/YYYY)
 */
public class GuiMFFSettings extends UIDisplay {

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
                new AnchorPoint(0.75, 1));
        settingsListBox.setPanelBackgroundColor(UIColor.matWhite(0.9));

        final UIGradientPanel settingsShadow = new UIGradientPanel(workspace,
                "settingsShadow",
                new PosXY(0, 0),
                new PosXY(4, 0),
                new AnchorPoint(0.75, 0),
                new AnchorPoint(0.75, 1));
        settingsShadow.setHorizontalGradient(UIColor.matGrey900(0.9), UIColor.matGrey900(0));
        //</editor-fold>

        //<editor-fold desc="Title">
        final UIComponent titleLabelComponent = new UIComponent(null,
                "titleLabelComponent",
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        titleLabelComponent.setPanelBackgroundColor(UIColor.transparent());

        final UILabel titleLabel = new UILabel(titleLabelComponent,
                "titleLabel",
                new PosXY(24, 6),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        titleLabel.setText(StatCollector.translateToLocal("gui.mff:mffSettings"));

        settingsListBox.addItem("titleLabelComponent", titleLabelComponent);
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
        highPolyLabel.setText(StatCollector.translateToLocal("gui.mff:useHighPolyModels"));

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
        GLSLLabel.setText(StatCollector.translateToLocal("gui.mff:useGLSLShaders"));

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

    }

}
