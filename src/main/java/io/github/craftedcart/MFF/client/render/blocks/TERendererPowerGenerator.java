package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.tileentity.TEPowerGenerator;
import io.github.craftedcart.MFF.utility.MathUtils;
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

        float redR = 244f; float redG = 67f; float redB = 54f; //Red RGB
        float blueR = 33f; float blueG = 150f; float blueB = 243f; //Blue RGB

        GlStateManager.translate(x, y, z);

        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

        //<editor-fold desc="Draw line between power cube above and the ff projector">
        //Draw line between power cube above and the ff projector
        if (te.getWorld().getBlockState(te.getPos().add(0, -1, 0)) == ModBlocks.powerCube.getDefaultState() && ((TEPowerGenerator) te).isSendingPower) {
            float powerPercent;
            if (((TEPowerGenerator) te).maxPower == -1) { //If it's a creative power generator
                powerPercent = 1;
            } else {
                powerPercent = (float) (((TEPowerGenerator) te).power / ((TEPowerGenerator) te).maxPower);
            }
            float r = MathUtils.lerp(blueR, redR, powerPercent);
            float g = MathUtils.lerp(blueG, redG, powerPercent);
            float b = MathUtils.lerp(blueB, redB, powerPercent);
            GL11.glColor4f(r / 255f, g / 255f, b / 255f, 1);

            GL11.glLineWidth(4);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(0.5, 0.9, 0.5);
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.8, 0.7), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.6, 0.5), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.4, 0.3), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.2, 0.1), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.1, 0.0), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.0, -0.1), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(-0.1, -0.2), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(-0.2, -0.3), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(-0.3, -0.4), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(-0.4, -0.5), MathUtils.randDouble(0.45, 0.55));
            GL11.glVertex3d(0.5, -0.5, 0.5);
            GL11.glEnd();
        }
        //</editor-fold>

        GL11.glColor4f(1, 1, 1, 0.5f);


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
