package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public class UITextButton extends UIButton {

    public UILabel uiLabel;

    public UITextButton(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                        AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        uiLabel = new UILabel(this,
                "buttonLabel",
                new PosXY(12, 1),
                new AnchorPoint(0, 0),
                GuiUtils.font);
    }

}
