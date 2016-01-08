package io.github.craftedcart.MFF.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import java.awt.*;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CraftedCart on 29/12/2015 (DD/MM/YYYY)
 */

public class GuiUtils {

    public static TrueTypeFont font;
    private static long lastFrame = 0;
    public static boolean debugEnabled = false;

    public static void init() throws FontFormatException, IOException {
        InputStream inputStream	= Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("mff:Roboto-Regular.ttf")).getInputStream();

        Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        awtFont2 = awtFont2.deriveFont(16f); // set font size
        font = new TrueTypeFont(awtFont2, true);
    }

    public static double getDelta() {
        long time = Sys.getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta / 1000d;
    }

    public static void drawQuad(PosXY p1, PosXY p2, PosXY p3, PosXY p4, UIColor col) {
        GL11.glColor4d(col.r, col.g, col.b, col.a);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(p1.x, p1.y);
            GL11.glVertex2d(p2.x, p2.y);
            GL11.glVertex2d(p3.x, p3.y);
            GL11.glVertex2d(p4.x, p4.y);
        }
        GL11.glEnd();
    }

    public static void drawQuad(PosXY p1, PosXY p2, PosXY p3, PosXY p4) {
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(p1.x, p1.y);
            GL11.glVertex2d(p2.x, p2.y);
            GL11.glVertex2d(p3.x, p3.y);
            GL11.glVertex2d(p4.x, p4.y);
        }
        GL11.glEnd();
    }

    public static void drawQuadGradient(PosXY p1, PosXY p2, PosXY p3, PosXY p4, UIColor colFrom, UIColor colTo) {
        GL11.glColor4d(colFrom.r, colFrom.g, colFrom.b, colFrom.a);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(p1.x, p1.y);
            GL11.glVertex2d(p2.x, p2.y);
            GL11.glColor4d(colTo.r, colTo.g, colTo.b, colTo.a);
            GL11.glVertex2d(p3.x, p3.y);
            GL11.glVertex2d(p4.x, p4.y);
        }
        GL11.glEnd();
    }

    public static void drawQuad(PosXY p1, PosXY p2, UIColor col) {
        drawQuad(
                new PosXY(p1.x, p1.y),
                new PosXY(p1.x, p2.y),
                new PosXY(p2.x, p2.y),
                new PosXY(p2.x, p1.y),
                col
        );

        if (debugEnabled) {

            if (Mouse.getX() >= p1.x && Mouse.getX() <= p2.x && Mouse.getY() >= Display.getHeight() - p2.y && Mouse.getY() <= Display.getHeight() - p1.y) {
                GL11.glColor4d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b, 0.5);
            } else {
                GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 0.5);
            }


            GL11.glLineWidth(2f);
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2d(p1.x, 0);
                GL11.glVertex2d(p1.x, Display.getHeight());
                GL11.glVertex2d(0, p1.y);
                GL11.glVertex2d(Display.getWidth(), p1.y);
                GL11.glVertex2d(p2.x, 0);
                GL11.glVertex2d(p2.x, Display.getHeight());
                GL11.glVertex2d(0, p2.y);
                GL11.glVertex2d(Display.getWidth(), p2.y);
            }
            GL11.glEnd();
        }
    }

    public static void drawQuad(PosXY p1, PosXY p2) {
        drawQuad(
                new PosXY(p1.x, p1.y),
                new PosXY(p1.x, p2.y),
                new PosXY(p2.x, p2.y),
                new PosXY(p2.x, p1.y)
        );

        if (debugEnabled) {

            if (Mouse.getX() >= p1.x && Mouse.getX() <= p2.x && Mouse.getY() >= Display.getHeight() - p2.y && Mouse.getY() <= Display.getHeight() - p1.y) {
                GL11.glColor4d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b, 0.5);
            } else {
                GL11.glColor4d(UIColor.matGrey900().r, UIColor.matGrey900().g, UIColor.matGrey900().b, 0.5);
            }

            GL11.glLineWidth(2f);
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2d(p1.x, 0);
                GL11.glVertex2d(p1.x, Display.getHeight());
                GL11.glVertex2d(0, p1.y);
                GL11.glVertex2d(Display.getWidth(), p1.y);
                GL11.glVertex2d(p2.x, 0);
                GL11.glVertex2d(p2.x, Display.getHeight());
                GL11.glVertex2d(0, p2.y);
                GL11.glVertex2d(Display.getWidth(), p2.y);
            }
            GL11.glEnd();
        }
    }

    public static void drawQuadGradientHorizontal(PosXY p1, PosXY p2, UIColor colFrom, UIColor colTo) {
        drawQuadGradient(
                new PosXY(p1.x, p1.y),
                new PosXY(p1.x, p2.y),
                new PosXY(p2.x, p2.y),
                new PosXY(p2.x, p1.y),
                colFrom, colTo
        );
    }

    public static void drawQuadGradientVertical(PosXY p1, PosXY p2, UIColor colFrom, UIColor colTo) {
        drawQuadGradient(
                new PosXY(p2.x, p1.y),
                new PosXY(p1.x, p1.y),
                new PosXY(p1.x, p2.y),
                new PosXY(p2.x, p2.y),
                colFrom, colTo
        );
    }

    public static void drawBackground(UIColor col) {
        drawQuad(
                new PosXY(0, 0),
                new PosXY(Display.getWidth(), Display.getHeight()),
                col
        );
    }

    public static void setup(boolean clearBuffer) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 10000, -10000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        if (clearBuffer) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        }

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1, 1, 1, 1);
        TextureImpl.bindNone();
    }

    public static void drawString(org.newdawn.slick.Font font, int x, int y, String string, UIColor col) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        font.drawString(x, y, string, new Color((float) col.r, (float) col.g, (float) col.b, (float) col.a));
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

}
