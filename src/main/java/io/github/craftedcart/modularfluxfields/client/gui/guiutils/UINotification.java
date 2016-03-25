package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

/**
 * Created by CraftedCart on 14/02/2016 (DD/MM/YYYY)
 */

public class UINotification extends UIComponent {

    public UILabel uiLabel;
    public double notificationTime = 5; //5 seconds

    public UINotification(UINotificationManager parentComponent, String name, String notificationText, UIColor backgroundColor) {
        super(parentComponent, name, new PosXY(-536, 24), new PosXY(-24, 56), new AnchorPoint(1, 0), new AnchorPoint(1, 0));
        setPanelBackgroundColor(backgroundColor);
        uiLabel = new UILabel(this, "notificationLabel", new PosXY(4, 4), new AnchorPoint(0, 0), GuiUtils.font);
        uiLabel.setText(notificationText);
        uiLabel.setTextColor(UIColor.matWhite());
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        notificationTime -= GuiUtils.getDelta();

        if (notificationTime <= 0) {
            ((UINotificationManager) parentComponent).destroyNotification(componentID); //Destroy this component
        }
    }
}
