package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.mcliquidui.util.UIAction;

/**
 * Created by CraftedCart on 07/02/2016 (DD/MM/YYYY)
 */
class SecurityTaskManager {

    static int runningTasks = 0;

    static void addTask(final UIAction uiAction) {
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
