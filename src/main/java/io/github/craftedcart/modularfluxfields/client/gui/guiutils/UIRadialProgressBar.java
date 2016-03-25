package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

import org.lwjgl.opengl.GL11;

/**
 * Created by CraftedCart on 07/02/2016 (DD/MM/YYYY)
 */

public class UIRadialProgressBar extends UIComponent {

    protected double progress = 0;
    protected float thickness = 4;
    protected UIColor radialBackgroundColor = UIColor.matGrey900();
    protected UIColor radialForegroundColor = UIColor.matBlue();
    /**
     * This is in degrees
     */
    protected double rotationalOffset = 0;

    public UIRadialProgressBar(UIComponent parentComponent, String name, PosXY topLeftPoint, PosXY bottomRightPoint,
                               AnchorPoint topLeftAnchor, AnchorPoint bottomRightAnchor) {
        super(parentComponent, name, topLeftPoint, bottomRightPoint,
                topLeftAnchor, bottomRightAnchor);
        setPanelBackgroundColor(UIColor.transparent());
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        if (visible) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(thickness);

            //<editor-fold desc="Draw foreground circle">
            GL11.glColor4d(radialForegroundColor.r, radialForegroundColor.g, radialForegroundColor.b, radialForegroundColor.a);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            {
                for (double i = 0; i < 361 * progress; i += 0.2) {
                    GL11.glVertex2d(
                            ((topLeftPx.x + bottomRightPx.x) / 2) + ((Math.min(bottomRightPx.x - topLeftPx.x, bottomRightPx.y - topLeftPx.y) - thickness) / 2) * Math.cos(Math.toRadians(i - 90 + rotationalOffset)),
                            ((topLeftPx.y + bottomRightPx.y) / 2) + ((Math.min(bottomRightPx.x - topLeftPx.x, bottomRightPx.y - topLeftPx.y) - thickness) / 2) * Math.sin(Math.toRadians(i - 90 + rotationalOffset))
                    );
                }
            }
            GL11.glEnd();
            //</editor-fold>

            //<editor-fold desc="Draw background circle">
            GL11.glColor4d(radialBackgroundColor.r, radialBackgroundColor.g, radialBackgroundColor.b, radialBackgroundColor.a);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            {
                for (double i = (361 * progress) - 0.2; i < 361; i += 0.2) {
                    GL11.glVertex2d(
                            ((topLeftPx.x + bottomRightPx.x) / 2) + ((Math.min(bottomRightPx.x - topLeftPx.x, bottomRightPx.y - topLeftPx.y) - thickness) / 2) * Math.cos(Math.toRadians(i - 90 + rotationalOffset)),
                            ((topLeftPx.y + bottomRightPx.y) / 2) + ((Math.min(bottomRightPx.x - topLeftPx.x, bottomRightPx.y - topLeftPx.y) - thickness) / 2) * Math.sin(Math.toRadians(i - 90 + rotationalOffset))
                    );
                }
            }
            GL11.glEnd();
            //</editor-fold>

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public float getThickness() {
        return thickness;
    }

    public void setRadialBackgroundColor(UIColor radialBackgroundColor) {
        this.radialBackgroundColor = radialBackgroundColor;
    }

    public UIColor getRadialBackgroundColor() {
        return radialBackgroundColor;
    }

    public void setRadialForegroundColor(UIColor radialForegroundColor) {
        this.radialForegroundColor = radialForegroundColor;
    }

    public UIColor getRadialForegroundColor() {
        return radialForegroundColor;
    }

    public void setRotationalOffset(double rotationalOffset) {
        this.rotationalOffset = rotationalOffset;
    }

    public double getRotationalOffset() {
        return rotationalOffset;
    }

}
