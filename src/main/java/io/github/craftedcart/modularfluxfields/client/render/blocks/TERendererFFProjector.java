package io.github.craftedcart.modularfluxfields.client.render.blocks;

import io.github.craftedcart.modularfluxfields.init.ModModels;
import io.github.craftedcart.modularfluxfields.reference.MFFSettings;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Created by CraftedCart on 22/12/2015 (DD/MM/YYYY)
 */
public class TERendererFFProjector extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        GlStateManager.pushMatrix();

//        GlStateManager.translate(x, y, z);
//
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_LIGHTING);
//
//        //<editor-fold desc="Draw lines to the entities being killed">
//        GL11.glColor4d(UIColor.matRed().r, UIColor.matRed().g, UIColor.matRed().b, 1);
//        GL11.glLineWidth(4);
//        GL11.glBegin(GL11.GL_LINES);
//
//        for (Entity entity : ((TEFFProjector) (te)).entityHitList) {
//            GL11.glVertex3d(0.5, 0.5, 0.5);
//            GL11.glVertex3d(entity.posX - te.getPos().getX(), entity.posY - te.getPos().getY() + 0.5, entity.posZ - te.getPos().getZ());
//        }
//
//        GL11.glEnd();
//        //</editor-fold>
//
//        //<editor-fold desc="Draw lines to wall blocks placed">
//        GL11.glColor4d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b, 1);
//        GL11.glBegin(GL11.GL_LINES);
//
//        for (BlockPos pos : ((TEFFProjector) (te)).wallBlocksPlaced) {
//            GL11.glVertex3d(0.5, 0.5, 0.5);
//            GL11.glVertex3d(pos.getX() - te.getPos().getX() + 0.5, pos.getY() - te.getPos().getY() + 0.5, pos.getZ() - te.getPos().getZ() + 0.5);
//        }
//
//        GL11.glEnd();
//        //</editor-fold>
//
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//
////        //<editor-fold desc="Draw line between power cube above and the ff projector">
////        //Draw line between power cube above and the ff projector
////        if (te.getWorld().getTileEntity(te.getPos().add(0, 1, 0)) instanceof TEPoweredBlock) {
////            GL11.glDisable(GL11.GL_TEXTURE_2D);
////            GL11.glDisable(GL11.GL_LIGHTING);
////            float powerPercent = (float) (((TEPoweredBlock) te).power / ((TEPoweredBlock) te).maxPower);
////            float r = MathUtils.lerp(blueR, redR, powerPercent);
////            float g = MathUtils.lerp(blueG, redG, powerPercent);
////            float b = MathUtils.lerp(blueB, redB, powerPercent);
////            GL11.glColor4f(r / 255f, g / 255f, b / 255f, 1);
////
////            GL11.glBegin(GL11.GL_LINE_STRIP);
////            GL11.glVertex3d(0.5, 0.5, 0.5);
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.6, 0.7), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.7, 0.8), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(0.9, 1.0), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.1, 1.2), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.2, 1.3), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.3, 1.4), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(MathUtils.randDouble(0.45, 0.55), MathUtils.randDouble(1.4, 1.5), MathUtils.randDouble(0.45, 0.55));
////            GL11.glVertex3d(0.5, 1.5, 0.5);
////            GL11.glEnd();
////        }
////        GL11.glEnable(GL11.GL_TEXTURE_2D);
////        GL11.glEnable(GL11.GL_LIGHTING);
////        //</editor-fold>

        TEFFProjector ffProjector = (TEFFProjector) te;

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GL11.glColor3d(1, 1, 1);

        ResourceLocation resourceLocation = new ResourceLocation("modularfluxfields:textures/blocks/forcefieldProjector.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(ShaderUtils.specularShaderProgram);
            GL20.glUniform1i(GL20.glGetUniformLocation(ShaderUtils.specularShaderProgram, "tex1"), 0);
        }

        ModModels.drawModel(ModModels.forcefieldProjectorModel);

        double pcPower = ffProjector.power; //Get FF Projector Power
        double pcMaxPower = ffProjector.maxPower;
        float pcPowerPercent = (float) pcPower / (float) pcMaxPower;

        GL11.glScalef(pcPowerPercent * 0.7f, pcPowerPercent * 0.7f, pcPowerPercent * 0.7f);
        GL11.glRotatef((te.getWorld().getTotalWorldTime() + partialTick) * 3, 1, 1, 1);

        resourceLocation = new ResourceLocation("modularfluxfields:textures/blocks/powerCubePower.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        ModModels.drawModel(ModModels.powerCubePowerModel);

        if (MFFSettings.useGLSLShaders) {
            GL20.glUseProgram(0);
        }

        GlStateManager.popMatrix();
    }

}
