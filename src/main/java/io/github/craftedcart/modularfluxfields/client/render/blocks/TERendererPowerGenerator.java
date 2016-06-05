package io.github.craftedcart.modularfluxfields.client.render.blocks;

import io.github.craftedcart.modularfluxfields.init.ModModels;
import io.github.craftedcart.modularfluxfields.reference.MFFSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Created by CraftedCart on 23/12/2015 (DD/MM/YYYY)
 */
public class TERendererPowerGenerator extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GL11.glColor3d(1, 1, 1);

        ResourceLocation resourceLocation = new ResourceLocation("modularfluxfields:textures/blocks/solarPowerGenerator.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(ShaderUtils.specularShaderProgram);
            GL20.glUniform1i(GL20.glGetUniformLocation(ShaderUtils.specularShaderProgram, "tex1"), 0);
        }

        ModModels.drawModel(ModModels.solarPowerGeneratorModel);

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(0);
        }

        GlStateManager.popMatrix();
    }

}
