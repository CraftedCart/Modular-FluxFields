package io.github.craftedcart.modularfluxfields.client.render.blocks;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */
public class Model {

    public List<Vector3f> vertices = new ArrayList<Vector3f>();
    public List<Vector2f> textures = new ArrayList<Vector2f>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Face> faces = new ArrayList<Face>();

    Model() {}

}
