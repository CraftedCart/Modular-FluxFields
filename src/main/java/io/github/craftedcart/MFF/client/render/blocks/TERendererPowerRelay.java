package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.init.ModModels;
import io.github.craftedcart.MFF.reference.MFFSettings;
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

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(ShaderUtils.specularShaderProgram);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GL11.glColor3d(1, 1, 1);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK); // Doesn't draw back faces

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

        if (!MFFSettings.useHighPolyModels) {
            ModModels.drawModel(ModModels.powerRelayLowPolyModel);
        } else {
            ModModels.drawModel(ModModels.powerRelayModel);
        }

        GlStateManager.popMatrix();

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(0);
        }

    }

}
