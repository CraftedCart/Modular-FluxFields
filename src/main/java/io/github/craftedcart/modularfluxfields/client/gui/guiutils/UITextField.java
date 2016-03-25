package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import io.github.craftedcart.modularfluxfields.utility.MathUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by CraftedCart on 08/02/2016 (DD/MM/YYYY)
 */
public class UITextField extends UIComponent {

    public String value = "";
    public String placeholderText;
    public UIColor textColor = UIColor.matWhite();
    public UIColor cursorColor = UIColor.matBlue();
    public double targetCursorAlpha = 0.25;
    public int cursorPos = 0;

    public UIAction onTabAction;
    public UIAction onReturnAction;

    public UITextField(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                       AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (selected) {

            List<Integer> keyBuffer = new ArrayList<Integer>();
            List<Boolean> keyStateBuffer = new ArrayList<Boolean>();
            List<Character> keyCharBuffer = new ArrayList<Character>();

            keyBuffer.addAll(UIDisplay.keyBuffer);
            keyStateBuffer.addAll(UIDisplay.keyStateBuffer);
            keyCharBuffer.addAll(UIDisplay.keyCharBuffer);

            //<editor-fold desc="Get keyboard input and add it to the value variable / check it">
            while (keyBuffer.size() > 0) {

                Integer currentKey = keyBuffer.get(0);
                Boolean currentKeyState = keyStateBuffer.get(0);
                Character currentKeyChar = keyCharBuffer.get(0);

                keyBuffer.remove(0);
                keyStateBuffer.remove(0);
                keyCharBuffer.remove(0);

                if (currentKeyState) {
                    if (currentKey == Keyboard.KEY_BACK) { //Backspace
                        if (cursorPos > 0) {
                            if (Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) && cursorPos != 0) { //If on Mac and Command key down
                                while (cursorPos != 0) { //Delete everything to the left of the cursor
                                    if (cursorPos == value.length()) {
                                        value = value.substring(0, value.length() - 1);
                                    } else {
                                        value = new StringBuilder(value).deleteCharAt(cursorPos - 1).toString();
                                    }
                                    cursorPos--;
                                }
                            } else if (cursorPos == value.length()) {
                                if ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Option key down
                                        (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on Mac and Ctrl key down
                                    do {
                                        value = value.substring(0, value.length() - 1);
                                        cursorPos--;
                                        if (cursorPos == 0) {
                                            break;
                                        }
                                    } while (value.charAt(cursorPos - 1) != ' ');
                                } else {
                                    value = value.substring(0, value.length() - 1);
                                    cursorPos--;
                                }
                            } else {
                                if ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Option key down
                                        (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on Mac and Ctrl key down
                                    do {
                                        value = new StringBuilder(value).deleteCharAt(cursorPos - 1).toString();
                                        cursorPos--;
                                        if (cursorPos == 0) {
                                            break;
                                        }
                                    } while (value.charAt(cursorPos - 1) != ' ');
                                } else {
                                    value = new StringBuilder(value).deleteCharAt(cursorPos - 1).toString();
                                    cursorPos--;
                                }
                            }
                        }
                    } else if (currentKey == Keyboard.KEY_DELETE) { //Forwards delete
                        if (cursorPos < value.length()) {
                            if (cursorPos == value.length() - 1) {
                                value = value.substring(0, value.length() - 1);
                            } else {
                                if ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Option key down
                                        (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on Mac and Ctrl key down
                                    do {
                                        value = new StringBuilder(value).deleteCharAt(cursorPos).toString();
                                        if (cursorPos == value.length()) {
                                            break;
                                        }
                                    } while (value.charAt(cursorPos) != ' ');
                                } else {
                                    value = new StringBuilder(value).deleteCharAt(cursorPos + 1).toString();
                                }
                            }
                        }
                    } else if (currentKey == Keyboard.KEY_RETURN) { //Return
                        if (onReturnAction != null) {
                            onReturnAction.execute();
                        }
                    } else if (currentKey == Keyboard.KEY_TAB) { //Tab
                        if (onTabAction != null) {
                            onTabAction.execute();
                        }
                    } else if (currentKey == Keyboard.KEY_LEFT) { //Left arrow key
                        if ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Option key down
                                (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on Mac and Ctrl key down
                            if (cursorPos != 0) {
                                cursorPos--;
                                do { //Goto prev word
                                    if (cursorPos == 0) {
                                        break;
                                    }
                                    cursorPos--;
                                } while (value.charAt(cursorPos) != ' ');
                                if (value.charAt(cursorPos) == ' ') {
                                    cursorPos++;
                                }
                            }
                        } else if (Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA))) { //If on Mac and Command key down
                            cursorPos = 0; //Goto the beginning of the text field
                        } else {
                            if (cursorPos > 0) {
                                cursorPos--;
                            }
                        }
                    } else if (currentKey == Keyboard.KEY_RIGHT) { //Right arrow key
                        if ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Option key down
                                (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on Mac and Ctrl key down
                            if (cursorPos != value.length()) {
                                do { //Goto next word
                                    cursorPos++;
                                    if (cursorPos == value.length()) {
                                        break;
                                    }
                                } while (value.charAt(cursorPos) != ' ');
                            }
                        } else if (Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA))) { //If on Mac and Command key down
                            cursorPos = value.length(); //Goto the end of the text field
                        } else {
                            if (cursorPos < value.length()) {
                                cursorPos++;
                            }
                        }
                    } else if (currentKey == Keyboard.KEY_HOME) { //Home key (Goto beginning)
                        cursorPos = 0;
                    } else if (currentKey == Keyboard.KEY_END) { //End key (Goto end)
                        cursorPos = value.length();
                    } else if (Pattern.matches("[A-Za-z0-9\\s_\\+\\-\\.,!@#\\$%\\^&\\*\\(\\);\\\\/\\|<>\"'\\[\\]\\?=:]", String.valueOf(Keyboard.getEventCharacter()))) {
                        value = new StringBuilder(value).insert(cursorPos, currentKeyChar).toString();
                        cursorPos++;
                    }
                }
            }
            //</editor-fold>

            //<editor-fold desc="Draw the cursor">
            if (GuiUtils.font.getWidth(value.substring(0, cursorPos)) > width - 8) {
                GuiUtils.drawQuad(bottomRightPx.add(-6, -height), bottomRightPx.add(-4, 0), cursorColor);
            } else {
                GuiUtils.drawQuad(topLeftPx.add(GuiUtils.font.getWidth(value.substring(0, cursorPos)) + 6, 0), topLeftPx.add(GuiUtils.font.getWidth(value.substring(0, cursorPos)) + 8, height), cursorColor);
            }
            //</editor-fold>

            cursorColor.a = MathUtils.lerp(cursorColor.a, targetCursorAlpha, 6 * GuiUtils.getDelta());

            if (cursorColor.a <= 0.3) {
                targetCursorAlpha = 1;
            } else if (cursorColor.a >= 0.95) {
                targetCursorAlpha = 0.25;
            }

        }

        if (value.isEmpty()) {
            GuiUtils.drawWithStencil(new UIAction() {
                @Override
                public void execute() {
                    GuiUtils.drawQuad(topLeftPx, bottomRightPx, UIColor.pureWhite());
                }
            }, new UIAction() {
                @Override
                public void execute() {
                    GuiUtils.drawString(GuiUtils.font, (int) topLeftPx.x + 6, (int) topLeftPx.y, placeholderText, new UIColor(textColor.r * 255, textColor.g * 255, textColor.b * 255, textColor.a / 2));
                }
            });
        }

        //<editor-fold desc="Draw the text in the text field //TODO Match text with the cursor pos">
        GuiUtils.drawWithStencil(new UIAction() {
            @Override
            public void execute() {
                GuiUtils.drawQuad(topLeftPx, bottomRightPx, UIColor.pureWhite());
            }
        }, new UIAction() {
            @Override
            public void execute() {
                int xPoint;

                if (GuiUtils.font.getWidth(value) > width - 10) {
                    xPoint = (int) (topLeftPx.x - GuiUtils.font.getWidth(value) + width - 6);
                } else {
                    xPoint = (int) topLeftPx.x + 6;
                }

                GuiUtils.drawString(GuiUtils.font, xPoint, (int) topLeftPx.y, value, textColor);
            }
        });
        //</editor-fold> //To //TODO

    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }

    public void setOnTabAction(UIAction onTabAction) {
        this.onTabAction = onTabAction;
    }

    public void setOnReturnAction(UIAction onReturnAction) {
        this.onReturnAction = onReturnAction;
    }

    public void clearValue() {
        value = "";
        cursorPos = 0;
    }

}
