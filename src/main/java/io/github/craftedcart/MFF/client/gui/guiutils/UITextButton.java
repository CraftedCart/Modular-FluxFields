package io.github.craftedcart.MFF.client.gui.guiutils;

import org.lwjgl.input.Mouse;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public class UITextButton extends UIComponent {

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

    @Override
    protected void checkMouseStateFromChildren() {
        boolean checkMouseOverChildComponent = false;

        for (UIComponent component : childUiComponents) { //Loop through every component
            if (component.mouseOver || component.mouseOverChildComponent) {
                mouseOverChildComponent = true;
                checkMouseOverChildComponent = true;
                break;
            }
        }

        if (!checkMouseOverChildComponent) {
            mouseOverChildComponent = false;
        }

        mouseOver = true;

        if (Mouse.isButtonDown(0)) { //IF LMB is down
            if (!lmbDown) {
                lmbDown = true;
                onClick(); //Mouse was clicked on this component
            }
        } else {
            lmbDown = false;
        }
    }

}
