package io.github.craftedcart.MFF.client.gui.guiutils;

/**
 * Created by CraftedCart on 16/02/2016 (DD/MM/YYYY)
 */

public class UIToggleBox extends UIButton {

    public boolean value = false;
    public UIColor falseColor = UIColor.matRed();
    public UIColor trueColor = UIColor.matGreen();

    public UIToggleBox(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                       AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
    }

    public void setFalseColor(UIColor falseColor) {
        this.falseColor = falseColor;
    }

    public void setTrueColor(UIColor trueColor) {
        this.trueColor = trueColor;
    }

    public void setValue(boolean value) {
        this.value = value;

        if (value) {
            setPanelDefaultBackgroundColor(trueColor);
            setPanelActiveBackgroundColor(new UIColor(trueColor.r * 255, trueColor.g * 255, trueColor.b * 255, trueColor.a * 0.75));
            setPanelHitBackgroundColor(new UIColor(trueColor.r * 255, trueColor.g * 255, trueColor.b * 255, trueColor.a * 0.5));
        } else {
            setPanelDefaultBackgroundColor(falseColor);
            setPanelActiveBackgroundColor(new UIColor(falseColor.r * 255, falseColor.g * 255, falseColor.b * 255, falseColor.a * 0.75));
            setPanelHitBackgroundColor(new UIColor(falseColor.r * 255, falseColor.g * 255, falseColor.b * 255, falseColor.a * 0.5));
        }
    }

    @Override
    protected void onClick() {

        value = !value;

        if (value) {
            setPanelDefaultBackgroundColor(trueColor);
            setPanelActiveBackgroundColor(new UIColor(trueColor.r * 255, trueColor.g * 255, trueColor.b * 255, trueColor.a * 0.75));
            setPanelHitBackgroundColor(new UIColor(trueColor.r * 255, trueColor.g * 255, trueColor.b * 255, trueColor.a * 0.5));
        } else {
            setPanelDefaultBackgroundColor(falseColor);
            setPanelActiveBackgroundColor(new UIColor(falseColor.r * 255, falseColor.g * 255, falseColor.b * 255, falseColor.a * 0.75));
            setPanelHitBackgroundColor(new UIColor(falseColor.r * 255, falseColor.g * 255, falseColor.b * 255, falseColor.a * 0.5));
        }

        super.onClick();
    }
}
