package io.github.craftedcart.MFF.proxy;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public class ClientProxy extends CommonProxy {

    public static int sphereIdOutside;
    public static int sphereIdInside;

    @Override
    public void registerRenders() {

        ModItems.registerRenders();
        ModBlocks.registerRenders();

        Sphere sphere = new Sphere();
        //Set up paramters that are common to both outside and inside.

        //GLU_FILL as a solid.
        sphere.setDrawStyle(GLU.GLU_FILL);
        //GLU_SMOOTH will try to smoothly apply lighting
        sphere.setNormals(GLU.GLU_SMOOTH);

        //First make the call list for the outside of the sphere


        sphere.setOrientation(GLU.GLU_OUTSIDE);

        sphereIdOutside = GL11.glGenLists(1);
        //Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);
        //binds the texture
        ResourceLocation rL = new ResourceLocation(Reference.MOD_ID+":textures/blocks/powerSphere.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
        //The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
        //control the detail, but have a massive impact on performance.
        sphere.draw(0.5F, 8, 8);
        GL11.glEndList();

        //Now make the call list for the inside of the sphere
        sphere.setOrientation(GLU.GLU_INSIDE);
        sphereIdInside = GL11.glGenLists(1);
        //Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdInside, GL11.GL_COMPILE);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
        sphere.draw(0.5F, 8, 8);
        GL11.glEndList();

    }

}
