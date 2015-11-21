package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.proxy.ClientProxy;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEPowerSphere;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */

public class TERendererPowerSphere extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GlStateManager.rotate(90f, 0f, 0f, 0f);

        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        TEPowerSphere tePowerSphere = (TEPowerSphere) te; //Get own TileEntity
        float powerPercent = 0;
        try {
            Field f = te.getClass().getField("power");
            int power = f.getInt(tePowerSphere);
            powerPercent = (float) power / (float) PowerConf.powerSphereMaxPower;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GL11.glColor4f(0.1294117647f * powerPercent, 0.5882352941f * powerPercent, 0.9529411765f * powerPercent, 0.5f);

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glCallList(ClientProxy.sphereIdOutside);
        GL11.glCallList(ClientProxy.sphereIdInside);

        //Render line between spheres
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(0f, 0f, 0f, 0f);
        GL11.glColor4f(244 / 255, 67 / 255, 54 / 255, 0.5f);
        try {

            Field f = te.getClass().getField("powerSphereLinks");
            List powerSphereLinks = (List) f.get(te);

            for (Object obj : powerSphereLinks) {

                BlockPos pos = (BlockPos) obj;
                BlockPos thisPos = te.getPos();
                pos = pos.add(-thisPos.getX(), -thisPos.getY(), -thisPos.getZ());

                WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
                wr.startDrawingQuads();

                wr.setNormal(1, 0, 0);
                wr.addVertex(-0.1, 0, 0.1);
                wr.addVertex(pos.getX() - 0.1, pos.getZ(), -pos.getY() + 0.1);
                wr.addVertex(pos.getX() + 0.1, pos.getZ(), -pos.getY() - 0.1);
                wr.addVertex(0.1, 0, -0.1);

                wr.setNormal(-1, 0, 0);
                wr.addVertex(-0.1, 0, -0.1);
                wr.addVertex(pos.getX() - 0.1, pos.getZ(), -pos.getY() - 0.1);
                wr.addVertex(pos.getX() + 0.1, pos.getZ(), -pos.getY() - 0.1);
                wr.addVertex(0.1, 0, 0.1);

                Tessellator.getInstance().draw();

            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        GL11.glScalef(1.0f * powerPercent, 1.0f * powerPercent, 1.0f * powerPercent);
        GL11.glColor4f(0.9568627451f, 0.262745098f, 2.16f, 0.5f);

        GL11.glCallList(ClientProxy.sphereIdOutside);
        GL11.glCallList(ClientProxy.sphereIdInside);


        GlStateManager.popMatrix();

    }

}
