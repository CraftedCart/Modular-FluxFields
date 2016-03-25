package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import io.github.craftedcart.modularfluxfields.utility.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CraftedCart on 11/02/2016 (DD/MM/YYYY)
 */

public class UIListBox extends UIComponent {

    public Map<String, UIComponent> componentMap = new HashMap<String, UIComponent>();
    public List<String> componentNumberIDs = new ArrayList<String>();

    private double targetScrollY = 0;

    public UIListBox(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                     AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (mouseOver || mouseOverChildComponent) {
            targetScrollY = Math.min(Math.max(targetScrollY + GuiUtils.getMouseDWheel(), height - getTotalHeightOfComponents()), 0);
        }

        pointOffset.y = MathUtils.lerp(pointOffset.y, targetScrollY, Math.min(GuiUtils.getDelta() * 20, 1));

    }

    public void addItem(String ID, UIComponent component) {

        double totalHeight = getTotalHeightOfComponents();

        component.topLeftPoint = component.topLeftPoint.add(0, totalHeight);
        component.bottomRightPoint = component.bottomRightPoint.add(0, totalHeight);

        component.parentComponent = this;
        component.componentID = lastComponentID;
        registerComponent(lastComponentID, component);
        lastComponentID++;
        component.calcPos();
        componentMap.put(ID, component);
        componentNumberIDs.add(ID);

    }

    public void removeItems(String... IDs) {
        for (String ID : IDs) {
            childUiComponents.set(componentMap.get(ID).componentID, null); //Destroy the component
            componentMap.remove(ID);
            componentNumberIDs.remove(ID);
        }
        reorganiseItems();
    }

    public void reorganiseItems() {
        double totalHeight = 0;
        for (int i = 0; i < componentMap.size(); i++) {
            PosXY currentTopLeft = componentMap.get(componentNumberIDs.get(i)).topLeftPoint;
            PosXY currentBottomRight = componentMap.get(componentNumberIDs.get(i)).bottomRightPoint;

            componentMap.get(componentNumberIDs.get(i)).bottomRightPoint = new PosXY(currentBottomRight.x, currentBottomRight.y - currentTopLeft.y + totalHeight);
            componentMap.get(componentNumberIDs.get(i)).topLeftPoint = new PosXY(currentTopLeft.x, totalHeight);

            totalHeight = componentMap.get(componentNumberIDs.get(i)).bottomRightPoint.y;
        }
    }

    @Override
    protected void updateChildren() {
        GuiUtils.setupStencilMask();
        GuiUtils.drawQuad(topLeftPx, bottomRightPx, UIColor.pureWhite());
        GuiUtils.setupStencilDraw();
        super.updateChildren();
        GuiUtils.setupStencilEnd();
    }

    public void setTargetScrollY(double targetScrollY) {
        this.targetScrollY = targetScrollY;
    }

    public double getTargetScrollY() {
        return targetScrollY;
    }

    public double getTotalHeightOfComponents() {
        double totalHeight = 0;
        for (UIComponent uiComponent : childUiComponents) {
            if (uiComponent != null) {
                totalHeight += uiComponent.height;
            }
        }
        return totalHeight;
    }

}
