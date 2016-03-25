package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */
public abstract class UIDisplay extends GuiScreen {

    protected UIRootComponent rootComponent = new UIRootComponent(this);
    protected boolean isFirstFrame = true;

    public static List<Integer> keyBuffer = new ArrayList<Integer>();
    public static List<Boolean> keyStateBuffer = new ArrayList<Boolean>();
    public static List<Character> keyCharBuffer = new ArrayList<Character>();

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiUtils.setup(false);

        keyBuffer.clear();
        keyStateBuffer.clear();
        keyCharBuffer.clear();

        Keyboard.poll();
        Mouse.poll();

        while (Keyboard.next()) {
            keyBuffer.add(Keyboard.getEventKey());
            keyStateBuffer.add(Keyboard.getEventKeyState());
            keyCharBuffer.add(Keyboard.getEventCharacter());

        }

        GuiUtils.calcDelta(isFirstFrame);
        GuiUtils.calcMouseDWheel();

        if (isFirstFrame) {
            isFirstFrame = false;
            onInit();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { //Close the GUI if Esc is pressed
            while (Keyboard.next()) {} //Exhaust the keyboard buffer
            while (Mouse.next()) {} //Exhaust the mouse buffer
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }

        rootComponent.onUpdate();
    }

    @Override
    public void handleKeyboardInput() {
        //No-Op
    }

    @Override
    public void handleMouseInput() {
        //No-Op
    }

    @Override
    public void handleInput() {
        //No-Op
    }

    public UIRootComponent getRootComponent() {
        return rootComponent;
    }

    public void onInit() {
        Keyboard.enableRepeatEvents(true);
    }

}
