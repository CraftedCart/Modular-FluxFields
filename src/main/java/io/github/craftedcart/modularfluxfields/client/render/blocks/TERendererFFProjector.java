package io.github.craftedcart.modularfluxfields.client.render.blocks;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.UIColor;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import io.github.craftedcart.modularfluxfields.tileentity.TEPoweredBlock;
import io.github.craftedcart.modularfluxfields.utility.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by CraftedCart on 22/12/2015 (DD/MM/YYYY)
 */

public class TERendererFFProjector extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        //<editor-fold desc="Draw lines to the entities being killed">
        GL11.glColor4d(UIColor.matRed().r, UIColor.matRed().g, UIColor.matRed().b, 1);
        GL11.glLineWidth(4);
        GL11.glBegin(GL11.GL_LINES);

        for (Entity entity : ((TEFFProjector) (te)).entityHitList) {
            GL11.glVertex3d(0.5, 0.5, 0.5);
            GL11.glVertex3d(entity.posX - te.getPos().getX(), entity.posY - te.getPos().getY() + 0.5, entity.posZ - te.getPos().getZ());
        }

        GL11.glEnd();
        //</editor-fold>

        //<editor-fold desc="Draw lines to wall blocks placed">
        GL11.glColor4d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b, 1);
        GL11.glBegin(GL11.GL_LINES);

        for (BlockPos pos : ((TEFFProjector) (te)).wallBlocksPlaced) {
            GL11.glVertex3d(0.5, 0.5, 0.5);
            GL11.glVertex3d(pos.getX() - te.getPos().getX() + 0.5, pos.getY() - te.getPos().getY() + 0.5, pos.getZ() - te.getPos().getZ() + 0.5);
        }

        GL11.glEnd();
        //</editor-fold>

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        float redR = 244f; float redG = 67f; float redB = 54f; //Red RGB
        float blueR = 33f; float blueG = 150f; float blueB = 243f; //Blue RGB

        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

        //<editor-fold desc="Draw line between power cube above and the ff projector">
        //Draw line between power cube above and the ff projector
        if (te.getWorld().getTileEntity(te.getPos().add(0, 1, 0)) instanceof TEPoweredBlock) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            float powerPercent = (float) (((TEPoweredBlock) te).power / ((TEPoweredBlock) te).maxPower);
            float r = MathUtils.lerp(blueR, redR, powerPercent);
            float g = MathUtils.lerp(blueG, redG, powerPercent);
            float b = MathUtils.lerp(blueB, redB, powerPercent);
            GL11.glColor4f(r / 255f, g / 255f, b / 255f, 1);

            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(0.5, 0.5, 0.5);
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.6, 0.7), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.7, 0.8), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.9, 1.0), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.1, 1.2), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.2, 1.3), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.3, 1.4), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.4, 1.5), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(0.5, 1.5, 0.5);
            GL11.glEnd();
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        //</editor-fold>

        //<editor-fold desc="Draw Base">
        // Base
        GL11.glColor4f(1, 1, 1, 0.5f);
        wr.startDrawingQuads();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("modularfluxfields:textures/blocks/base.png"));
        // -Z
        wr.setNormal(0, 0, -1);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 0.0, 0.5);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 0.5);
        wr.addVertexWithUV(0.0, 0.5, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.5, 0.0, 0.0, 0.0);
        // +Z
        wr.setNormal(0, 0, 1);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 0.0, 0.5);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 0.5);
        wr.addVertexWithUV(1.0, 0.5, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.5, 1.0, 0.0, 0.0);
        // -X
        wr.setNormal(-1, 0, 0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.5);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 1.0, 0.5);
        wr.addVertexWithUV(0.0, 0.5, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.5, 0.0, 0.0, 0.0);
        // +X
        wr.setNormal(1, 0, 0);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 0.5);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 1.0, 0.5);
        wr.addVertexWithUV(1.0, 0.5, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.5, 1.0, 0.0, 0.0);
        // -Y
        wr.setNormal(0, -1, 0);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 0.0);
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(1.0, 0.5, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.5, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.5, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.5, 1.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        //</editor-fold>

        //<editor-fold desc="Draw Hologram">
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * 3, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);

        // Hologram
        wr.startDrawingQuads();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("modularfluxfields:textures/blocks/hologramTriangle.png"));
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

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("modularfluxfields:textures/blocks/hologramSquare.png"));
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(1.0, 1.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 1.0, 1.0, 0.0, 0.0);

        GL11.glEnable(GL11.GL_BLEND);
        Tessellator.getInstance().draw();
        GL11.glDisable(GL11.GL_BLEND);
        //</editor-fold>

        //<editor-fold desc="Draw the spinny bit under the hologram">
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.scale(1.2, 1, 1.2);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * -6, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);

        // The spinny bit under the hologram
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("modularfluxfields:textures/blocks/base.png"));

        wr.startDrawingQuads();
        // -Z
        wr.setNormal(0, 0, -1);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.2, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.2, 0.0, 0.0, 0.0);

        wr.addVertexWithUV(0.8, 0.2, 0.2, 0.0, 0.0);
        wr.addVertexWithUV(0.2, 0.2, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.0, 0.2, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.0, 0.2, 0.0, 1.0);
        // +Z
        wr.setNormal(0, 0, 1);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.2, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.2, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.2, 0.8, 0.0, 0.0);
        wr.addVertexWithUV(0.8, 0.2, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.0, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.0, 0.8, 0.0, 1.0);
        // -X
        wr.setNormal(-1, 0, 0);
        wr.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 1.0);
        wr.addVertexWithUV(0.0, 0.0, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.2, 1.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.2, 0.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.0, 0.8, 0.0, 1.0);
        wr.addVertexWithUV(0.2, 0.0, 0.2, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.2, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.2, 0.8, 0.0, 0.0);
        // +X
        wr.setNormal(1, 0, 0);
        wr.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.0, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.2, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(1.0, 0.2, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.8, 0.0, 0.2, 0.0, 1.0);
        wr.addVertexWithUV(0.8, 0.0, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.8, 0.2, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.2, 0.2, 0.0, 0.0);
        // +Y
        wr.setNormal(0, 1, 0);
        wr.addVertexWithUV(0.8, 0.2, 0.2, 0.0, 1.0);
        wr.addVertexWithUV(1.0, 0.2, 0.0, 1.0, 1.0);
        wr.addVertexWithUV(0.0, 0.2, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.2, 0.2, 0.2, 0.0, 0.0);

        wr.addVertexWithUV(1.0, 0.2, 1.0, 0.0, 1.0);
        wr.addVertexWithUV(0.8, 0.2, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.2, 0.8, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.2, 1.0, 0.0, 0.0);

        wr.addVertexWithUV(0.2, 0.2, 0.2, 0.0, 0.0);
        wr.addVertexWithUV(0.0, 0.2, 0.0, 1.0, 0.0);
        wr.addVertexWithUV(0.0, 0.2, 1.0, 1.0, 1.0);
        wr.addVertexWithUV(0.2, 0.2, 0.8, 0.0, 1.0);

        wr.addVertexWithUV(1.0, 0.2, 0.0, 0.0, 0.0);
        wr.addVertexWithUV(0.8, 0.2, 0.2, 1.0, 0.0);
        wr.addVertexWithUV(0.8, 0.2, 0.8, 1.0, 1.0);
        wr.addVertexWithUV(1.0, 0.2, 1.0, 0.0, 1.0);

        Tessellator.getInstance().draw();
        //</editor-fold>

        GlStateManager.popMatrix();
    }

}
