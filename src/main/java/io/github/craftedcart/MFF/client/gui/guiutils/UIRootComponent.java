package io.github.craftedcart.MFF.client.gui.guiutils;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Created by CraftedCart on 09/01/2016 (DD/MM/YYYY)
 */

public class UIRootComponent extends UIComponent {

    protected UIDisplay uiDisplay;
    protected boolean debugKeyHit = false;
    protected int debugKeycode = 61; //Debug toggle key (F3)
    protected UIComponent debugSelectedComponent = this;
    private boolean resizingDebugSidebar = false;

    /**
     * @param uiDisplay The {@link UIDisplay} which the component will get registered to
     */
    public UIRootComponent(UIDisplay uiDisplay) {

        this.uiDisplay = uiDisplay;

        this.topLeftPx = new PosXY(0, 0);
        this.bottomRightPx = new PosXY(Display.getWidth(), Display.getHeight());

        this.name = "rootComponent";

    }

    /**
     * This is called every frame
     */
    protected void onUpdate() {

        width = bottomRightPx.x - topLeftPx.x;
        height = bottomRightPx.y - topLeftPx.y;

        int mx = Mouse.getX();
        int my = Display.getHeight() - Mouse.getY();

        if (Keyboard.getEventKey() == debugKeycode) {
            if (Keyboard.getEventKeyState()) {
                if (!debugKeyHit) {
                    debugKeyHit = true;
                    GuiUtils.debugEnabled = !GuiUtils.debugEnabled;
                }
            } else {
                debugKeyHit = false;
            }
        }

        if (GuiUtils.debugEnabled) {
            this.bottomRightPx = new PosXY(Display.getWidth() - GuiUtils.debugSidebarWidth, Display.getHeight() - 72);
        } else {
            this.bottomRightPx = new PosXY(Display.getWidth(), Display.getHeight());
        }

        if (visible) {

            if (Mouse.isButtonDown(0)) { //IF LMB is down
                if (!lmbDown) {
                    lmbDown = true;
                    onClick(); //Mouse was clicked on this component
                }
            } else {
                lmbDown = false;
            }

            GuiUtils.drawQuad(
                    topLeftPx,
                    bottomRightPx,
                    panelDefaultBackgroundColor);

            for (UIComponent component : childUiComponents) { //Loop through every component
                component.onUpdate(); //Update all registered components
            }

        } else {
            mouseOver = false;
            selected = false;
        }

        if (GuiUtils.debugEnabled) {
            drawDebug();
        }

    }

    protected void drawDebug() {
        final int w = Display.getWidth();
        final int h = Display.getHeight();
        final int mx = Mouse.getX();
        final int my = h - Mouse.getY();

        GuiUtils.drawQuad( //Sidebar Background quad
                new PosXY(w - GuiUtils.debugSidebarWidth, 0),
                new PosXY(w, h),
                UIColor.matGrey900()
        );

        GuiUtils.drawQuad( //Bottom bar Background quad
                new PosXY(0, h - 72),
                new PosXY(w - GuiUtils.debugSidebarWidth, h),
                UIColor.matGrey900()
        );

        GuiUtils.drawQuad( //Ram usage quad
                new PosXY(0, h - 72),
                new PosXY(((w - GuiUtils.debugSidebarWidth) * (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory()), h - 48),
                UIColor.matBlueGrey()
        );

        GuiUtils.drawString(GuiUtils.font, 2, h - 24, String.format("%d FPS (%.3fs Delta Time)", Minecraft.getDebugFPS(), GuiUtils.getDelta()), UIColor.matWhite()); //Draw FPS and Delta Time
        GuiUtils.drawString(GuiUtils.font, 2, h - 48, String.format("Mouse Pos: %d, %d | LMB Down: %b", mx, my, lmbDown), UIColor.matWhite()); //Draw Mouse Pos
        GuiUtils.drawString(GuiUtils.font, 2, h - 72, String.format("Used memory: %.4f GiB / %.4f GiB | Free memory: %.4f GiB",
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1073741824f,
                Runtime.getRuntime().maxMemory() / 1073741824f,
                Runtime.getRuntime().freeMemory() / 1073741824f), UIColor.matWhite()); //Draw Mouse Pos


        //<editor-fold desc="Draw Hierarchy View">
        GuiUtils.drawQuad( //Draw Hierarchy View Selected Component Name BG
                new PosXY(w - GuiUtils.debugSidebarWidth, 0),
                new PosXY(w, 16),
                UIColor.matBlue()
        );

        GuiUtils.drawQuad( //Draw Hierarchy View Title BG
                new PosXY(w - GuiUtils.debugSidebarWidth, 16),
                new PosXY(w, 30),
                UIColor.matBlueGrey()
        );

        GuiUtils.drawString(GuiUtils.debugFont, w - GuiUtils.debugSidebarWidth + 2, 0, //Draw Hierarchy View Selected Component Name
                String.format("%d: %s (%d children)", debugSelectedComponent.componentID, debugSelectedComponent.name, debugSelectedComponent.childUiComponents.size()), UIColor.matWhite());
        GuiUtils.drawString(GuiUtils.debugFont, w - GuiUtils.debugSidebarWidth + 2, 14, "Hierarchy View", UIColor.matWhite()); //Draw Hierarchy View Title

        GuiUtils.drawQuadGradientVertical( //Draw Hierarchy View Selected Component Name BG Shadow
                new PosXY(w - GuiUtils.debugSidebarWidth, 16),
                new PosXY(w, 18),
                UIColor.matGrey900(), UIColor.matGrey900(0)
        );

        GuiUtils.drawQuadGradientVertical( //Draw Hierarchy View Title BG Shadow
                new PosXY(w - GuiUtils.debugSidebarWidth, 30),
                new PosXY(w, 32),
                UIColor.matGrey900(), UIColor.matGrey900(0)
        );

        int hierarchyIndex = 0;
        for (UIComponent childComponent : debugSelectedComponent.childUiComponents) {

            if (childComponent != null) {
                UIColor textCol;
                if (childComponent.mouseOver) {
                    textCol = UIColor.matBlue();
                } else if (childComponent.mouseOverChildComponent) {
                    textCol = UIColor.matBlue700();
                } else {
                    textCol = UIColor.matWhite();
                }

                GuiUtils.drawString(GuiUtils.debugFont, w - GuiUtils.debugSidebarWidth + 2, 14 * hierarchyIndex + 28,
                        String.format("%d: %s (%d children)", hierarchyIndex, childComponent.name, childComponent.childUiComponents.size()), textCol);
            } else {
                GuiUtils.drawString(GuiUtils.debugFont, w - GuiUtils.debugSidebarWidth + 2, 14 * hierarchyIndex + 28, String.format("%d: Null (Removed)", hierarchyIndex), UIColor.matRed());
            }

            hierarchyIndex++;
        }
        //</editor-fold>

        GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 0.25); //Draw crosshair at mouse pos
        GL11.glLineWidth(1f);
        GL11.glBegin(GL11.GL_LINES);
        {
            if (my <= h - 72) {
                GL11.glVertex2d(0, my);
                GL11.glVertex2d(w - GuiUtils.debugSidebarWidth, my);
            }
            if (mx <= w - GuiUtils.debugSidebarWidth) {
                GL11.glVertex2d(mx, 0);
                GL11.glVertex2d(mx, h - 72);
            }
        }
        GL11.glEnd();

        UIComponent mouseOverComponent = null;

        if (mx >= w - GuiUtils.debugSidebarWidth && my <= 14) { //Check if the mouse is over the selected component in the Hierarchy View
            mouseOverComponent = debugSelectedComponent;

        } else if (mx >= w - GuiUtils.debugSidebarWidth && my >= 28) { //Check if the mouse is over something in the Hierarchy View
            int selectedItem = (int) Math.floor((my - 28f) / 14);

            if (selectedItem <= debugSelectedComponent.childUiComponents.size() - 1) {
                if (debugSelectedComponent.childUiComponents.get(selectedItem) != null) {
                    mouseOverComponent = debugSelectedComponent.childUiComponents.get(selectedItem);
                }
            }
        }
        if (mouseOverComponent != null) {
            drawDebugOverlayOnComponentAndChildren(mouseOverComponent, false);
        }

        //<editor-fold desc="UI Inset Shadows">
        GuiUtils.drawQuadGradient(
                new PosXY(w - GuiUtils.debugSidebarWidth - 4, 0),
                new PosXY(w - GuiUtils.debugSidebarWidth - 4, h - 76),
                new PosXY(w - GuiUtils.debugSidebarWidth, h - 72),
                new PosXY(w - GuiUtils.debugSidebarWidth, 0),
                UIColor.matGrey900(0), UIColor.matGrey900()
        );

        GuiUtils.drawQuadGradient(
                new PosXY(w - GuiUtils.debugSidebarWidth - 4, h - 76),
                new PosXY(0, h - 76),
                new PosXY(0, h - 72),
                new PosXY(w - GuiUtils.debugSidebarWidth, h - 72),
                UIColor.matGrey900(0), UIColor.matGrey900()
        );
        //</editor-fold>

        //<editor-fold desc="Resize sidebar if dragged on">
        if (mx >= Display.getWidth() - GuiUtils.debugSidebarWidth - 4 && mx <= Display.getWidth() - GuiUtils.debugSidebarWidth + 4) {
            GuiUtils.drawQuad(new PosXY(Display.getWidth() - GuiUtils.debugSidebarWidth - 4, 0),
                    new PosXY(Display.getWidth() - GuiUtils.debugSidebarWidth + 4, Display.getHeight()),
                    UIColor.matBlue(0.5));

            resizingDebugSidebar = lmbDown;
        }

        if (resizingDebugSidebar) {
            GuiUtils.debugSidebarWidth = Display.getWidth() - mx;
            if (!lmbDown) {
                resizingDebugSidebar = false;
            }
        }
        //</editor-fold>
    }

    protected void drawDebugOverlayOnComponentAndChildren(UIComponent component, boolean isChildComponent) {
        if (isChildComponent) {
            GuiUtils.drawQuad(
                    component.topLeftPx,
                    component.bottomRightPx,
                    UIColor.matRed(0.25)
            );
        } else {
            GuiUtils.drawQuad(
                    component.topLeftPx,
                    component.bottomRightPx,
                    UIColor.matOrange(0.25)
            );

            GL11.glLineWidth(2f);
            GL11.glColor4d(UIColor.matOrange().r, UIColor.matOrange().g, UIColor.matOrange().b, 1);
            if (component.parentComponent != null) {

                //Draw Top Left Anchor
                GuiUtils.drawLine(
                        new PosXY(component.parentComponent.topLeftPx.x + component.topLeftAnchor.xPercent * component.parentComponent.width - 10,
                                component.parentComponent.topLeftPx.y + component.topLeftAnchor.yPercent * component.parentComponent.height),
                        new PosXY(component.parentComponent.topLeftPx.x + component.topLeftAnchor.xPercent * component.parentComponent.width + 10,
                                component.parentComponent.topLeftPx.y + component.topLeftAnchor.yPercent * component.parentComponent.height),
                        UIColor.matOrange());
                GuiUtils.drawLine(
                        new PosXY(component.parentComponent.topLeftPx.x + component.topLeftAnchor.xPercent * component.parentComponent.width,
                                component.parentComponent.topLeftPx.y + component.topLeftAnchor.yPercent * component.parentComponent.height - 10),
                        new PosXY(component.parentComponent.topLeftPx.x + component.topLeftAnchor.xPercent * component.parentComponent.width,
                                component.parentComponent.topLeftPx.y + component.topLeftAnchor.yPercent * component.parentComponent.height + 10),
                        UIColor.matOrange());

                //Draw Bottom Right Anchor
                GuiUtils.drawLine(
                        new PosXY(component.parentComponent.topLeftPx.x + component.bottomRightAnchor.xPercent * component.parentComponent.width - 10,
                                component.parentComponent.topLeftPx.y + component.bottomRightAnchor.yPercent * component.parentComponent.height),
                        new PosXY(component.parentComponent.topLeftPx.x + component.bottomRightAnchor.xPercent * component.parentComponent.width + 10,
                                component.parentComponent.topLeftPx.y + component.bottomRightAnchor.yPercent * component.parentComponent.height),
                        UIColor.matOrange());
                GuiUtils.drawLine(
                        new PosXY(component.parentComponent.topLeftPx.x + component.bottomRightAnchor.xPercent * component.parentComponent.width,
                                component.parentComponent.topLeftPx.y + component.bottomRightAnchor.yPercent * component.parentComponent.height - 10),
                        new PosXY(component.parentComponent.topLeftPx.x + component.bottomRightAnchor.xPercent * component.parentComponent.width,
                                component.parentComponent.topLeftPx.y + component.bottomRightAnchor.yPercent * component.parentComponent.height + 10),
                        UIColor.matOrange());
            }
        }

        for (UIComponent childComponent : component.childUiComponents) {
            drawDebugOverlayOnComponentAndChildren(childComponent, true);
        }
    }

    /**
     * This is called when the {@link UIRootComponent} is clicked on
     */
    protected void onClick() {
        if (onClickAction != null) {
            onClickAction.execute();
        }
        final int w = Display.getWidth();
        final int h = Display.getHeight();
        final int mx = Mouse.getX();
        final int my = h - Mouse.getY();

        if (GuiUtils.debugEnabled) {
            if (mx >= w - GuiUtils.debugSidebarWidth && my <= 14) { //Go up a level in the Hierarchy View
                if (debugSelectedComponent.parentComponent != null) {
                    debugSelectedComponent = debugSelectedComponent.parentComponent;
                }
            } else if (mx >= w - GuiUtils.debugSidebarWidth && my >= 28) { //Check if something in the Hierarchy View was clicked on
                int selectedItem = (int) Math.floor((my - 28f) / 14);
                if (selectedItem <= debugSelectedComponent.childUiComponents.size() - 1) {
                    if (debugSelectedComponent.childUiComponents.get(selectedItem) != null) {
                        debugSelectedComponent = debugSelectedComponent.childUiComponents.get(selectedItem);
                    }
                }
            }
        }

    }

}
