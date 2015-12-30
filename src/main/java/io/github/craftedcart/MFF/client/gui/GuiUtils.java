package io.github.craftedcart.MFF.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CraftedCart on 29/12/2015 (DD/MM/YYYY)
 */

public class GuiUtils {

    public static TrueTypeFont font;
    private static long lastFrame = 0;

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

}
