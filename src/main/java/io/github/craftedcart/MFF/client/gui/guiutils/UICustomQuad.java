package io.github.craftedcart.MFF.client.gui.guiutils;

/**
 * Created by CraftedCart on 13/01/2016 (DD/MM/YYYY)
 */

public class UICustomQuad extends UIComponent {

    protected PosXY topRightPoint;
    protected PosXY bottomLeftPoint;
    protected PosXY topRightPx;
    protected PosXY bottomLeftPx;

    public UICustomQuad(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY topRightPoint, PosXY bottomRightPoint, PosXY bottomLeftPoint,
                        AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        this.topRightPoint = topRightPoint;
        this.bottomLeftPoint = bottomLeftPoint;
    }

    @Override
    protected void onUpdate() {
        if (visible) {
            topRightPx = new PosXY(
                    parentComponent.width * bottomRightAnchor.xPercent + topRightPoint.x + parentComponent.topLeftPx.x + parentComponent.pointOffset.x,
                    parentComponent.height * topLeftAnchor.yPercent + topRightPoint.y + parentComponent.topLeftPx.y + parentComponent.pointOffset.y
            );

            bottomLeftPx = new PosXY(
                    parentComponent.width * topLeftAnchor.xPercent + bottomLeftPoint.x + parentComponent.topLeftPx.x + parentComponent.pointOffset.x,
                    parentComponent.height * bottomRightAnchor.yPercent + bottomLeftPoint.y + parentComponent.topLeftPx.y + parentComponent.pointOffset.y
            );
        }
        super.onUpdate();
    }

    @Override
    protected void draw() {
        if (lmbDown) {
            GuiUtils.drawQuad(topLeftPx, bottomLeftPx, bottomRightPx, topRightPx, panelHitBackgroundColor);
        } else if (mouseOver || mouseOverChildComponent) {
            GuiUtils.drawQuad(topLeftPx, bottomLeftPx, bottomRightPx, topRightPx, panelActiveBackgroundColor);
        } else {
            GuiUtils.drawQuad(topLeftPx, bottomLeftPx, bottomRightPx, topRightPx, panelDefaultBackgroundColor);
        }
    }

}
