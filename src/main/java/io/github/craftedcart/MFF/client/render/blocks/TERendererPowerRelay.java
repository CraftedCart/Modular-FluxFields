package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.init.ModModels;
import io.github.craftedcart.MFF.reference.MFFSettings;
import io.github.craftedcart.MFF.tileentity.TEPowerRelay;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */
public class TERendererPowerRelay extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        TEPowerRelay tePowerRelay = (TEPowerRelay) te;

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(ShaderUtils.specularShaderProgram);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GL11.glColor3d(1, 1, 1);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK); //Doesn't draw back faces

        ResourceLocation resourceLocation;

        if (!MFFSettings.useHighPolyModels) {
            resourceLocation = new ResourceLocation("mff:textures/blocks/powerRelayLowPoly.png");
        } else {
            resourceLocation = new ResourceLocation("mff:textures/blocks/powerRelay.png");
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        if (MFFSettings.useGLSLShaders) {
            GL20.glUniform1i(GL20.glGetUniformLocation(ShaderUtils.specularShaderProgram, "tex1"), 0);
        }

        //<editor-fold desc="Draw the base">
        switch (tePowerRelay.getInputSide()) {
            case DOWN:
                GL11.glRotated(270, 0, 0, 1);
                break;
            case UP:
                GL11.glRotated(90, 0, 0, 1);
                break;
            case NORTH:
                GL11.glRotated(90, 0, 1, 0);
                break;
//            case EAST:
//                //Default model rotation is East, No-Op
//                break;
            case SOUTH:
                GL11.glRotated(270, 0, 1, 0);
                break;
            case WEST:
                GL11.glRotated(180, 0, 1, 0);
                break;
        }

        if (!MFFSettings.useHighPolyModels) {
            ModModels.drawModel(ModModels.powerRelayLowPolyModel);
        } else {
            ModModels.drawModel(ModModels.powerRelayModel);
        }

        GlStateManager.popMatrix();
        //</editor-fold>

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);

        switch (tePowerRelay.getOutputSide()) {
            case DOWN:
                GL11.glRotated(90, 0, 0, 1);
                break;
            case UP:
                GL11.glRotated(270, 0, 0, 1);
                break;
            case NORTH:
                GL11.glRotated(270, 0, 1, 0);
                break;
            case EAST:
                GL11.glRotated(180, 0, 1, 0);
                break;
            case SOUTH:
                GL11.glRotated(90, 0, 1, 0);
                break;
//            case WEST:
//                //Default model rotation is West, No-Op
//                break;
        }

        resourceLocation = new ResourceLocation("mff:textures/blocks/powerRelayOutput.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        ModModels.drawModel(ModModels.powerRelayOutputStaticModel);

        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTick) * 3, 1, 0, 0);

        ModModels.drawModel(ModModels.powerRelayOutputRotateModel);

        GlStateManager.popMatrix();

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(0);
        }

    }

}
