package io.github.craftedcart.MFF.client.render.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by CraftedCart on 23/12/2015 (DD/MM/YYYY)
 */

public class TERendererPowerGenerator extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        GL11.glColor4f(1, 1, 1, 0.5f);
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.startDrawingQuads();

        // Base
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/base.png"));
        // -Z
        wr.setNormal(0, 0, -1);
        wr.addVertexWithUV(1.0, 0.9, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.9, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 0.9);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 0.0, 0.9);
        // +Z
        wr.setNormal(0, 0, 1);
        wr.addVertexWithUV(0.0, 0.9, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.9, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 1.0, 0.9);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 0.0, 0.9);
        // -X
        wr.setNormal(-1, 0, 0);
        wr.addVertexWithUV(0.0, 0.9, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.9, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 1.0, 0.9);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 0.0, 0.9);
        // +X
        wr.setNormal(1, 0, 0);
        wr.addVertexWithUV(1.0, 0.9, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.9, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 0.9);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 0.0, 0.9);
        // -Y
        wr.setNormal(0, -1, 0);
        wr.addVertexWithUV(0.0, 0.9, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.9, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.9, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.9, 1.0, 0.0, 0.0);
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 0.0, 0.0);
        Tessellator.getInstance().draw();

        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.scale(0.5, 0.9, 0.5);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * 3, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);

        // Hologram
        wr.startDrawingQuads();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/hologramTriangle.png"));
        // -Z
        wr.setNormal(0, 0, -1);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 0.0, 0.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        // +Z
        wr.setNormal(0, 0, 1);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 0.0, 0.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        // -X
        wr.setNormal(-1, 0, 0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 0.0, 0.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        // +X
        wr.setNormal(1, 0, 0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 0.0, 0.0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.5, 0.0, 0.5, 0.5, 1.0);

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/hologramSquare.png"));
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 0.0, 0.0);

        GL11.glEnable(GL11.GL_BLEND);
        Tessellator.getInstance().draw();
        GL11.glDisable(GL11.GL_BLEND);

        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.scale(1.7, 1, 1.7);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * -15, 0, 1, 0);
        GlStateManager.translate(-0.5, 0.8, -0.5);

        // The spinny bit under the hologram
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/base.png"));

        for (int i = 0; i < 6; i++) {
            drawSpinnyThing(wr);
            GlStateManager.translate(0.05, -0.1, 0.05);
            GlStateManager.scale(0.9, 1, 0.9);
        }

        GlStateManager.popMatrix();
    }

    private void drawSpinnyThing(WorldRenderer wr) { //I couldn't think of a good name...

        wr.startDrawingQuads();
        // -Z
        wr.setNormal(0, 0, -1);
        wr.addVertexWithUV(1.0, 0.00, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.00, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.02, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.02, 0.0, 0.0, 0.0);

        wr.addVertexWithUV(0.8, 0.02, 0.2, 0.0, 0.0);
        wr.addVertexWithUV(0.2, 0.02, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.00, 0.2, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.00, 0.2, 0.0, 1.0);
        // +Z
        wr.setNormal(0, 0, 1);
        wr.addVertexWithUV(0.0, 0.00, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.00, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.02, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.02, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.02, 0.8, 0.0, 0.0);
        wr.addVertexWithUV(0.8, 0.02, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.00, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.00, 0.8, 0.0, 1.0);
        // -X
        wr.setNormal(-1, 0, 0);
        wr.addVertexWithUV(0.0, 0.00, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.00, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.02, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.02, 0.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.00, 0.8, 0.0, 1.0);
        wr.addVertexWithUV(0.2, 0.00, 0.2, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.02, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.02, 0.8, 0.0, 0.0);
        // +X
        wr.setNormal(1, 0, 0);
        wr.addVertexWithUV(1.0, 0.00, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.00, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.02, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.02, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.8, 0.00, 0.2, 0.0, 1.0);
        wr.addVertexWithUV(0.8, 0.00, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.02, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.02, 0.2, 0.0, 0.0);
        // -Y
        wr.setNormal(0, -1, 0);
        wr.addVertexWithUV(0.2, 0.0, 0.2, 0.0, 0.0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.0, 0.2, 0.0, 1.0);

        wr.addVertexWithUV(0.0, 0.0, 1.0, 0.0, 0.0);
        wr.addVertexWithUV(0.2, 0.0, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.0, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 1.0);

        wr.addVertexWithUV(0.2, 0.0, 0.8, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.0, 0.2, 0.0, 0.0);

        wr.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(0.8, 0.0, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.0, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 0.0, 0.0);
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(0.8, 0.02, 0.2, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.02, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.02, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.02, 0.2, 0.0, 0.0);

        wr.addVertexWithUV(1.0, 0.02, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(0.8, 0.02, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.02, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.02, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.02, 0.2, 0.0, 0.0);
        wr.addVertexWithUV(0.0, 0.02, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.02, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.02, 0.8, 0.0, 1.0);

        wr.addVertexWithUV(1.0, 0.02, 0.0, 0.0, 0.0);
        wr.addVertexWithUV(0.8, 0.02, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.02, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.02, 1.0, 0.0, 1.0);

        Tessellator.getInstance().draw();

    }

}
