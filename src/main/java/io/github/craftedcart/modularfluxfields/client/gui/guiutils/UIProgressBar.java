package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

/**
 * Created by CraftedCart on 12/01/2016 (DD/MM/YYYY)
 */

public class UIProgressBar extends UIComponent {

    public UIComponent uiFGBar;
    protected double progress = 0;

    public UIProgressBar(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                         AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        uiFGBar = new UIComponent(this,
                "fgBar",
                new PosXY(0, 0),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(0, 1));
    }

    public void setProgress(double progress) {
        this.progress = progress;
        uiFGBar.setBottomRightAnchor(new AnchorPoint(progress, 1));
    }

    public double getProgress() {
        return progress;
    }

    public void setPanelForegroundColor(UIColor col) {
        uiFGBar.setPanelBackgroundColor(col);
    }
}
