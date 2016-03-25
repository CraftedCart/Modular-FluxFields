package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import io.github.craftedcart.modularfluxfields.utility.MathUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * Created by CraftedCart on 06/02/2016 (DD/MM/YYYY)
 */

public class UIDualSlider extends UIComponent {

    public UIComponent minHandle;
    public UIComponent maxHandle;
    public UIComponent selectedArea;
    public double minValue;
    public double maxValue;
    public double currentMinValue;
    public double currentMaxValue;
    public double decimalPlaces = 9;
    private byte selectedHandle = -1;
    /*
     * -1: No handle selected selected
     *  0: Min (Left) handle selected
     *  1: Max (Right) handle selected
     */

    public UIDualSlider(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                        AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        selectedArea = new UIComponent(this,
                "selectedArea",
                new PosXY(0, 0),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 1));
        minHandle = new UIComponent(this,
                "minHandle",
                new PosXY(-2, -8),
                new PosXY(2, 8),
                new AnchorPoint(0, 0),
                new AnchorPoint(0, 1));
        maxHandle = new UIComponent(this,
                "maxHandle",
                new PosXY(-2, -8),
                new PosXY(2, 8),
                new AnchorPoint(1, 0),
                new AnchorPoint(1, 1));
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (Mouse.isButtonDown(0)) { //If LMB down
            double percent = (Mouse.getX() - topLeftPx.x) / width;
            double mouseValue = MathUtils.lerp(minValue, maxValue, percent);

            if (selectedHandle == 0) { //Min handle selected
                if (Mouse.getX() > maxHandle.topLeftPx.x + 2) { //Min handle went over the max handle, so select the max handle
                    setCurrentMinValue(currentMaxValue);
                    selectedHandle = 1;
                } else {
                    setCurrentMinValue(mouseValue);
                }
            } else if (selectedHandle == 1) { //Max handle selected
                if (Mouse.getX() < minHandle.topLeftPx.x + 2) { //Max handle went over the min handle, so select the min handle
                    setCurrentMaxValue(currentMinValue);
                    selectedHandle = 0;
                } else {
                    setCurrentMaxValue(mouseValue);
                }
            }

            if (Mouse.getX() >= minHandle.topLeftPx.x - 4 &&
                    Mouse.getX() <= minHandle.bottomRightPx.x + 4 &&
                    Display.getHeight() - Mouse.getY() >= minHandle.topLeftPx.y - 4 &&
                    Display.getHeight() - Mouse.getY() <= minHandle.bottomRightPx.y + 4) { //Select the min handle
                selectedHandle = 0;
            } else if (Mouse.getX() >= maxHandle.topLeftPx.x - 4 &&
                    Mouse.getX() <= maxHandle.bottomRightPx.x + 4 &&
                    Display.getHeight() - Mouse.getY() >= maxHandle.topLeftPx.y - 4 &&
                    Display.getHeight() - Mouse.getY() <= maxHandle.bottomRightPx.y + 4) { //Select the max handle
                selectedHandle = 1;
            }
        } else { //Deselect handles
            selectedHandle = -1;
        }

    }

    public void setCurrentMinValue(double currentMinValue) {
        double rounded = Math.max(Math.round(currentMinValue * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces), minValue);

        this.currentMinValue = rounded;

        double percent = (rounded - minValue) / (maxValue - minValue);
        minHandle.topLeftAnchor = new AnchorPoint(percent, 0);
        minHandle.bottomRightAnchor = new AnchorPoint(percent, 1);
        selectedArea.topLeftAnchor = new AnchorPoint(percent, 0);
    }

    public void setCurrentMaxValue(double currentMaxValue) {
        double rounded = Math.min(Math.round(currentMaxValue * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces), maxValue);

        this.currentMaxValue = rounded;

        double percent = (rounded - minValue) / (maxValue - minValue);
        maxHandle.topLeftAnchor = new AnchorPoint(percent, 0);
        maxHandle.bottomRightAnchor = new AnchorPoint(percent, 1);
        selectedArea.bottomRightAnchor = new AnchorPoint(percent, 1);
    }

    public void setDecimalPlaces(double decimalPlaces) {
        this.decimalPlaces = decimalPlaces;

        setCurrentMinValue(currentMinValue);
        setCurrentMaxValue(currentMaxValue);
    }
}
