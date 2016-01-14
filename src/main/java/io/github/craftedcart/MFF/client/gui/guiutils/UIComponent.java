package io.github.craftedcart.MFF.client.gui.guiutils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public class UIComponent {

    protected List<UIComponent> childUiComponents = new ArrayList<UIComponent>();
    protected Integer selectedComponent = null;
    protected int lastComponentID = 0;
    protected String name; //This is only really used in the hierarchy inspector

    protected boolean mouseOver = false;
    protected boolean mouseOverChildComponent = false;
    protected boolean selected = false;
    protected boolean visible = true;

    protected boolean lmbDown = true;

    protected PosXY topLeftPoint;
    protected PosXY bottomRightPoint;
    protected double width;
    protected double height;
    protected PosXY topLeftPx = new PosXY(0, 0);
    protected PosXY bottomRightPx = new PosXY(0, 0);

    protected AnchorPoint topLeftAnchor;
    protected AnchorPoint bottomRightAnchor;

    protected UIComponent parentComponent;
    protected int componentID;

    protected UIAction onClickAction;
    protected UIAction onUpdateAction;

    protected UIColor panelDefaultBackgroundColor = UIColor.matGrey900(); //Default idle background color
    protected UIColor panelActiveBackgroundColor = UIColor.matGrey900(); //Selected / mouse over background color
    protected UIColor panelHitBackgroundColor = UIColor.matGrey900(); //Clicked on background color

    /**
     * Create a new UIComponent by calling this<br>
     * The component will automatically register itself with the {@link io.github.craftedcart.MFF.client.gui.guiutils.UIDisplay} provided
     *
     * @param parentComponent The {@link io.github.craftedcart.MFF.client.gui.guiutils.UIComponent} which the component will get registered to
     * @param topLeftPoint The top left point of the component
     * @param bottomRightPoint The bottom right point of the component
     */
    public UIComponent(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                       AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {

        this.parentComponent = parentComponent;
        this.name = name;
        this.topLeftPoint = topLeftPoint;
        this.bottomRightPoint = bottomRightPoint;
        this.topLeftAnchor = topLeftAnchor;
        this.bottomRightAnchor = bottomRightAnchor;
        this.componentID = this.parentComponent.lastComponentID;
        this.parentComponent.lastComponentID++;
        this.parentComponent.registerComponent(componentID, this);

    }

    public UIComponent() {
    }

    /**
     * This is called every frame
     */
    protected void onUpdate() {

        final int h = Display.getHeight();

        final int mx = Mouse.getX(); //mx: Short for Mouse X
        final int my = h - Mouse.getY(); //my: Short for Mouse Y

        topLeftPx = new PosXY(
                parentComponent.width * topLeftAnchor.xPercent + topLeftPoint.x + parentComponent.topLeftPx.x,
                parentComponent.height * topLeftAnchor.yPercent + topLeftPoint.y + parentComponent.topLeftPx.y
        );

        bottomRightPx = new PosXY(
                parentComponent.width * bottomRightAnchor.xPercent + bottomRightPoint.x + parentComponent.topLeftPx.x,
                parentComponent.height * bottomRightAnchor.yPercent + bottomRightPoint.y + parentComponent.topLeftPx.y
        );

        width = bottomRightPx.x - topLeftPx.x;
        height = bottomRightPx.y - topLeftPx.y;

        if (visible) {

            //<editor-fold desc="Check mouse state">
            if (mx >= topLeftPx.x && mx < bottomRightPx.x && my >= topLeftPx.y && my < bottomRightPx.y) { //If the mouse is over this component
                checkMouseStateFromChildren();
            } else {
                mouseOver = false;
                mouseOverChildComponent = false;
                lmbDown = false;
            }
            //</editor-fold>

            draw();

            if (onUpdateAction != null) {
                onUpdateAction.execute();
            }

            for (UIComponent component : childUiComponents) { //Loop through every component
                component.onUpdate(); //Update all registered components
            }

        } else {
            mouseOver = false;
            lmbDown = false;
            selected = false;
        }

    }

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

        if (!mouseOverChildComponent) {
            mouseOver = true;

            if (Mouse.isButtonDown(0)) { //IF LMB is down
                if (!lmbDown) {
                    lmbDown = true;
                    onClick(); //Mouse was clicked on this component
                }
            } else {
                lmbDown = false;
            }
        } else {
            mouseOver = false;
            lmbDown = false;
        }
    }

    /**
     * This is called when the {@link io.github.craftedcart.MFF.client.gui.guiutils.UIComponent} is clicked on
     */
    protected void onClick() {
        parentComponent.setSelectedComponent(componentID);

        if (onClickAction != null) {
            onClickAction.execute();
        }
    }

    protected void draw() {
        if (lmbDown) {
            GuiUtils.drawQuad(topLeftPx, bottomRightPx, panelHitBackgroundColor);
        } else if (mouseOver || mouseOverChildComponent) {
            GuiUtils.drawQuad(topLeftPx, bottomRightPx, panelActiveBackgroundColor);
        } else {
            GuiUtils.drawQuad(topLeftPx, bottomRightPx, panelDefaultBackgroundColor);
        }
    }

    /**
     * @param clickAction the interface containing an execute() method to be called
     */
    public void setOnClickAction(UIAction clickAction) {
        this.onClickAction = clickAction;
    }

    public void setOnUpdateAction(UIAction onUpdateAction) {
        this.onUpdateAction = onUpdateAction;
    }

    /**
     * Set the default, active and hit background color of the UIComponent
     *
     * @param color The color which the background should be
     */
    public void setPanelBackgroundColor(UIColor color) {
        this.panelDefaultBackgroundColor = color;
        this.panelActiveBackgroundColor = color;
        this.panelHitBackgroundColor = color;
    }

    /**
     * Set the default background color (When it isn't selected) of the UIComponent
     *
     * @param color The color which the background should be
     */
    public void setPanelDefaultBackgroundColor(UIColor color) {
        this.panelDefaultBackgroundColor = color;
    }

    /**
     * Set the active background color (When it is selected / the mouse is over it) of the UIComponent
     *
     * @param color The color which the background should be
     */
    public void setPanelActiveBackgroundColor(UIColor color) {
        this.panelActiveBackgroundColor = color;
    }

    /**
     * Set the hit background color (When it is clicked on) of the UIComponent
     *
     * @param color The color which the background should be
     */
    public void setPanelHitBackgroundColor(UIColor color) {
        this.panelHitBackgroundColor = color;
    }

    /**
     * Set the visibility and interactivity of this component
     *
     * @param visibility Whether this component should be visible and interactable
     */
    public void setVisible(boolean visibility) {
        this.visible = visibility;
    }

    /**
     * The method sets the active selected component
     *
     * @param componentID The ID of the component being selected
     */
    protected void setSelectedComponent(int componentID) {
        selectedComponent = componentID;
    }

    /**
     * You shouldn't need to call this!<br>
     * When a new {@link io.github.craftedcart.MFF.client.gui.guiutils.UIComponent} is created, it will register itself
     *
     * @param componentID The ID of the component being registered
     * @param component The {@link io.github.craftedcart.MFF.client.gui.guiutils.UIComponent} being registered
     */
    protected void registerComponent(int componentID, UIComponent component) {
        childUiComponents.add(componentID, component);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMouseOverChildComponent() {
        return mouseOverChildComponent;
    }

    public void setTopLeftAnchor(AnchorPoint topLeftAnchor) {
        this.topLeftAnchor = topLeftAnchor;
    }

    public void setTopLeftPoint(PosXY topLeftPoint) {
        this.topLeftPoint = topLeftPoint;
    }

    public void setBottomRightAnchor(AnchorPoint bottomRightAnchor) {
        this.bottomRightAnchor = bottomRightAnchor;
    }

    public void setBottomRightPoint(PosXY bottomRightPoint) {
        this.bottomRightPoint = bottomRightPoint;
    }
}
