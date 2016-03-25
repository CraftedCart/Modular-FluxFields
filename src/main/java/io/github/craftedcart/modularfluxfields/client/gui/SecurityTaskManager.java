package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.UIAction;

/**
 * Created by CraftedCart on 07/02/2016 (DD/MM/YYYY)
 */

public class SecurityTaskManager {

    public static int runningTasks = 0;

    public static void addTask(final UIAction uiAction) {
        Thread thread = new Thread() {
            public void run() {
                uiAction.execute();
                runningTasks -= 1;
            }
        };
        thread.start();
        runningTasks += 1;
    }

}
