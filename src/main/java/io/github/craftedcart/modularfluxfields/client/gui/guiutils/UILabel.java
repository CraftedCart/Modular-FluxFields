package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import org.newdawn.slick.TrueTypeFont;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */
public class UILabel extends UIComponent {

    protected TrueTypeFont font;
    protected String text = "";
    protected UIColor textColor = UIColor.matGrey900();
    /**
     * -1: Left<br>
     *  0: Center<br>
     *  1: Right<br>
     */
    protected byte horizontalAlign = -1;

    public UILabel(UIComponent parentComponent, String name, PosXY topLeftPoint,
                   AnchorPoint topLeftAnchor, TrueTypeFont font) {
        super(parentComponent, name, topLeftPoint, topLeftPoint,
                topLeftAnchor, topLeftAnchor);
        this.font = font;
        this.setPanelBackgroundColor(UIColor.transparent());
    }

    @Override
    protected void onUpdate() {
        if (visible) {
            super.onUpdate();
            int xPoint = 0;
            if (horizontalAlign == -1) { //Left align
                xPoint = (int) topLeftPx.x;
            } else if (horizontalAlign == 0) { //Center align
                xPoint = (int) (topLeftPx.x - font.getWidth(text) / 2);
            } else if (horizontalAlign == 1) {
                xPoint = (int) (topLeftPx.x - font.getWidth(text));
            }
            GuiUtils.drawString(font, xPoint, (int) topLeftPx.y, text, textColor);
        }
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public void setText(String text) {
        this.text = text;
        bottomRightPoint = topLeftPoint.add(font.getWidth(text), font.getHeight());
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
        bottomRightPoint = topLeftPoint.add(font.getWidth(text), font.getHeight());
    }

    /**
     * -1: Left<br>
     *  0: Center<br>
     *  1: Right<br>
     */
    public void setHorizontalAlign(int horizontalAlign) {
        this.horizontalAlign = (byte) horizontalAlign;
    }

}
