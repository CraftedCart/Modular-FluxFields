package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CraftedCart on 14/02/2016 (DD/MM/YYYY)
 */

public class UINotificationManager extends UIComponent {

    private List<Integer> componentsToDestroy = new ArrayList<Integer>();

    public UINotificationManager(UIComponent parentComponent, String name) {
        super(parentComponent, name, new PosXY(0, 0), new PosXY(0, 0), new AnchorPoint(1, 0), new AnchorPoint(1, 0));
    }

    public void addNotification(String notificationText, UIColor backgroundColor) {
        new UINotification(this, notificationText + "Notification", notificationText, backgroundColor);
    }

    protected void destroyNotification(int componentID) {
        componentsToDestroy.add(componentID);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        Iterator<Integer> iterToDestroy = componentsToDestroy.iterator();
        while (iterToDestroy.hasNext()) {
            childUiComponents.set(iterToDestroy.next(), null);
            iterToDestroy.remove();
        }
    }

    @Override
    protected void draw() {
        //No-Op
    }
}
