package io.github.craftedcart.MFF.client.gui.guiutils;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public class UILabel extends UIComponent {

    protected TrueTypeFont font;
    protected String text = "";
    protected UIColor textColor = UIColor.matGrey900();

    public UILabel(UIComponent parentComponent, String name, PosXY topLeftPoint,
                   AnchorPoint topLeftAnchor, TrueTypeFont font) {
        super(parentComponent, name, topLeftPoint, topLeftPoint,
                topLeftAnchor, topLeftAnchor);
        this.font = font;
        this.setPanelBackgroundColor(UIColor.transparent());
    }

    public UILabel(UIComponent parentComponent, String name, PosXY topLeftPoint,
                   AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor, TrueTypeFont font) {
        super(parentComponent, name, topLeftPoint, topLeftPoint,
                bottomRightAnchor, topLeftAnchor);
        this.font = font;
        this.setPanelBackgroundColor(UIColor.transparent());
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        GuiUtils.drawString(font, (int) topLeftPx.x, (int) topLeftPx.y, text, textColor);
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
}
