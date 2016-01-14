package io.github.craftedcart.MFF.client.gui.guiutils;

/**
 * Created by CraftedCart on 10/01/2016 (DD/MM/YYYY)
 */

public class UIGradientPanel extends UIComponent {

    protected byte gradientDirection = 0; //0: Vertical gradient | 1: Horizontal Gradient
    protected UIColor colorFrom = UIColor.matGrey900();
    protected UIColor colorTo = UIColor.matGrey900();

    public UIGradientPanel(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                    AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        this.setPanelBackgroundColor(UIColor.transparent());
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (visible) {
            if (gradientDirection == 0) {
                GuiUtils.drawQuadGradientVertical(topLeftPx, bottomRightPx, colorFrom, colorTo);
            } else {
                GuiUtils.drawQuadGradientHorizontal(topLeftPx, bottomRightPx, colorFrom, colorTo);
            }
        }

    }

    public void setVerticalGradient(UIColor colorFrom, UIColor colorTo) {
        gradientDirection = 0;
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    public void setHorizontalGradient(UIColor colorFrom, UIColor colorTo) {
        gradientDirection = 1;
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

}
