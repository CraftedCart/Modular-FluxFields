package io.github.craftedcart.modularfluxfields.init;

import io.github.craftedcart.modularfluxfields.client.render.blocks.Face;
import io.github.craftedcart.modularfluxfields.client.render.blocks.Model;
import io.github.craftedcart.modularfluxfields.client.render.blocks.OBJLoader;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;

/**
 * Created by CraftedCart on 26/02/2016 (DD/MM/YYYY)
 */
public class ModModels {

    public static Model powerRelayModel;
    public static Model powerRelayLowPolyModel;
    public static Model solarPowerGeneratorModel;
    public static Model forcefieldProjectorModel;
    public static Model powerCubeStaticModel;
    public static Model powerCubePowerModel;
    public static Model outputArmStaticModel;
    public static Model outputArmRotateModel;

    public static void init() {
        powerRelayModel = loadModel("modularfluxfields:models/block/powerRelay.obj");
        powerRelayLowPolyModel = loadModel("modularfluxfields:models/block/powerRelayLowPoly.obj");
        solarPowerGeneratorModel = loadModel("modularfluxfields:models/block/solarPowerGenerator.obj");
        forcefieldProjectorModel = loadModel("modularfluxfields:models/block/forcefieldProjector.obj");
        powerCubeStaticModel = loadModel("modularfluxfields:models/block/powerCubeStatic.obj");
        powerCubePowerModel = loadModel("modularfluxfields:models/block/powerCubePower.obj");
        outputArmStaticModel = loadModel("modularfluxfields:models/block/outputArmStatic.obj");
        outputArmRotateModel = loadModel("modularfluxfields:models/block/outputArmRotate.obj");
    }

    private static Model loadModel(String resourceLocation) {
        try {
            LogHelper.info("Loading " + resourceLocation);
            return OBJLoader.loadModel(
                    Minecraft.getMinecraft().getResourceManager().getResource(
                            new ResourceLocation(resourceLocation)
                    ).getInputStream());
        } catch (IOException e) {
            LogHelper.error("Failed to load " + resourceLocation);
            e.printStackTrace();
            throw new RuntimeException(e); //Forcibly stop Minecraft
        }
    }

    public static void drawModel(Model m) {

        GL11.glBegin(GL11.GL_TRIANGLES);
        {
            for (Face face : m.faces) {

                //Normal 1
                Vector3f n1 = m.normals.get((int) face.normal.x - 1);
                GL11.glNormal3f(n1.x, n1.y, n1.z);

                //Texture 1
                if (m.textures.size() > 0) {
                    Vector2f t1 = m.textures.get((int) face.texture.x - 1);
                    GL11.glTexCoord2f(t1.x, t1.y);
                }

                //Vertex 1
                Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
                GL11.glVertex3f(v1.x, v1.y, v1.z);

                //Normal 2
                Vector3f n2 = m.normals.get((int) face.normal.y - 1);
                GL11.glNormal3f(n2.x, n2.y, n2.z);

                //Texture 2
                if (m.textures.size() > 0) {
                    Vector2f t2 = m.textures.get((int) face.texture.y - 1);
                    GL11.glTexCoord2f(t2.x, t2.y);
                }

                //Vertex 2
                Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
                GL11.glVertex3f(v2.x, v2.y, v2.z);

                //Normal 3
                Vector3f n3 = m.normals.get((int) face.normal.z - 1);
                GL11.glNormal3f(n3.x, n3.y, n3.z);

                //Texture 3
                if (m.textures.size() > 0) {
                    Vector2f t3 = m.textures.get((int) face.texture.z - 1);
                    GL11.glTexCoord2f(t3.x, t3.y);
                }

                //Vertex 3
                Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
                GL11.glVertex3f(v3.x, v3.y, v3.z);

            }
        }
        GL11.glEnd();

    }

}
