package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEPowerCube;
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
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */

public class TERendererPowerCube extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        float cubeSize = 0.3f;
        float redR = 244f; float redG = 67f; float redB = 54f; //Red RGB
        float blueR = 33f; float blueG = 150f; float blueB = 243f; //Blue RGB

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GL11.glColor4f(1, 1, 1, 0.5f);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * 3, (te.getWorld().getTotalWorldTime() + partialTick) * 3, (te.getWorld().getTotalWorldTime() + partialTick) * 3, 0);

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/powerCubeFrame.png"));
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.startDrawingQuads();
        wr.setBrightness(240);
        drawCube(cubeSize + 0.02f);
        Tessellator.getInstance().draw();

        //Display energy as cube in the middle
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("mff:textures/blocks/white.png"));

        double pcPower = ((TEPowerCube) te).power; //Get Power Cube Power
        double pcMaxPower = PowerConf.powerCubeMaxPower;
        float pcPowerPercent = (float) pcPower / (float) pcMaxPower;

        GlStateManager.scale(pcPowerPercent, pcPowerPercent, pcPowerPercent);

        float r = MathUtils.lerp(blueR, redR, pcPowerPercent);
        float g = MathUtils.lerp(blueG, redG, pcPowerPercent);
        float b = MathUtils.lerp(blueB, redB, pcPowerPercent);

        GL11.glColor4f(r / 255f, g / 255f, b / 255f, 0.5f);
        wr.startDrawingQuads();
        drawCube(cubeSize);
        GL11.glEnable(GL11.GL_BLEND);
        Tessellator.getInstance().draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 0.5f);

        GlStateManager.popMatrix();

    }

    private void drawCube(float cubeSize) {
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

        wr.setNormal(1, 0, 0); //+X Face
        wr.addVertexWithUV(cubeSize, cubeSize, -cubeSize, 0.0, 0.0);
        wr.addVertexWithUV(cubeSize, cubeSize, cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(cubeSize, -cubeSize, cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(cubeSize, -cubeSize, -cubeSize, 1.0, 0.0);

        wr.setNormal(0, 1, 0); //+Y Face
        wr.addVertexWithUV(-cubeSize, cubeSize, cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(cubeSize, cubeSize, cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(cubeSize, cubeSize, -cubeSize, 1.0, 0.0);
        wr.addVertexWithUV(-cubeSize, cubeSize, -cubeSize, 0.0, 0.0);

        wr.setNormal(0, 0, 1); //+Z Face
        wr.addVertexWithUV(cubeSize, cubeSize, cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(-cubeSize, cubeSize, cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(-cubeSize, -cubeSize, cubeSize, 1.0, 0.0);
        wr.addVertexWithUV(cubeSize, -cubeSize, cubeSize, 0.0, 0.0);

        wr.setNormal(-1, 0, 0); //-X Face
        wr.addVertexWithUV(-cubeSize, -cubeSize, cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(-cubeSize, cubeSize, cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(-cubeSize, cubeSize, -cubeSize, 1.0, 0.0);
        wr.addVertexWithUV(-cubeSize, -cubeSize, -cubeSize, 0.0, 0.0);

        wr.setNormal(0, -1, 0); //-Y Face
        wr.addVertexWithUV(cubeSize, -cubeSize, -cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(cubeSize, -cubeSize, cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(-cubeSize, -cubeSize, cubeSize, 1.0, 0.0);
        wr.addVertexWithUV(-cubeSize, -cubeSize, -cubeSize, 0.0, 0.0);

        wr.setNormal(0, 0, -1); //-Z Face
        wr.addVertexWithUV(-cubeSize, cubeSize, -cubeSize, 0.0, 1.0);
        wr.addVertexWithUV(cubeSize, cubeSize, -cubeSize, 1.0, 1.0);
        wr.addVertexWithUV(cubeSize, -cubeSize, -cubeSize, 1.0, 0.0);
        wr.addVertexWithUV(-cubeSize, -cubeSize, -cubeSize, 0.0, 0.0);
    }

}
