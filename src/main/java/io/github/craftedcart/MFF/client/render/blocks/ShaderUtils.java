package io.github.craftedcart.MFF.client.render.blocks;

import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */

public class ShaderUtils {

    static int specularShaderProgram;

    public static void init() {

        LogHelper.info("Loading GLSL shaders");

        specularShaderProgram = GL20.glCreateProgram();
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        StringBuilder vertexShaderSource = new StringBuilder();
        StringBuilder fragmentShaderSource = new StringBuilder();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("mff:shaders/specular.vert")).getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                vertexShaderSource.append(line).append("\n");
            }

            reader.close();

            reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("mff:shaders/specular.frag")).getInputStream()));

            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append("\n");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        LogHelper.info("Compiling GLSL shaders");

        GL20.glShaderSource(vertexShader, vertexShaderSource);
        GL20.glShaderSource(fragmentShader, fragmentShaderSource);

        GL20.glCompileShader(vertexShader);
        if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            LogHelper.error("Failed to compile the vertex shader");

            IntBuffer logLength = ByteBuffer.allocateDirect(8).asIntBuffer();
            GL20.glGetShader(vertexShader, GL20.GL_INFO_LOG_LENGTH, logLength);
            LogHelper.error(GL20.glGetShaderInfoLog(vertexShader, logLength.get(0))); //Get the co

        } else {
            LogHelper.info("Successfully compiled the vertex shader");
        }

        GL20.glCompileShader(fragmentShader);
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            LogHelper.error("Failed to compile the fragment shader");

            IntBuffer logLength = ByteBuffer.allocateDirect(8).asIntBuffer();
            GL20.glGetShader(vertexShader, GL20.GL_INFO_LOG_LENGTH, logLength);
            LogHelper.error(GL20.glGetShaderInfoLog(vertexShader, logLength.get(0))); //Get the compile error

        } else {
            LogHelper.info("Successfully compiled the fragment shader");
        }

        GL20.glAttachShader(specularShaderProgram, vertexShader);
        GL20.glAttachShader(specularShaderProgram, fragmentShader);
        GL20.glLinkProgram(specularShaderProgram);
        GL20.glValidateProgram(specularShaderProgram);

    }

}
