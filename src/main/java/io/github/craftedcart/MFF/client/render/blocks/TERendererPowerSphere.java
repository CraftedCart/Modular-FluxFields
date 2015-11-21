package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.proxy.ClientProxy;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEPowerSphere;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

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

        GL11.glScalef(1.0f * powerPercent, 1.0f * powerPercent, 1.0f * powerPercent);
        GL11.glColor4f(0.9568627451f, 0.262745098f, 2.16f, 0.5f);

        GL11.glCallList(ClientProxy.sphereIdOutside);
        GL11.glCallList(ClientProxy.sphereIdInside);

        GlStateManager.popMatrix();

    }

}
