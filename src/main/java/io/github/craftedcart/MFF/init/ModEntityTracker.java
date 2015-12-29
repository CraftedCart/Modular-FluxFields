package io.github.craftedcart.MFF.init;

import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 26/12/2015 (DD/MM/YYYY)
 */

public class ModEntityTracker {

    public static List<Class<?>> hostileMobs = new ArrayList<Class<?>>();
    public static List<Class<?>> peacefulMobs = new ArrayList<Class<?>>();

    public static void init() {

        hostileMobs.add(EntityMob.class);
        hostileMobs.add(EntityGhast.class);
        hostileMobs.add(EntitySlime.class);

        peacefulMobs.add(EntityAnimal.class);
        peacefulMobs.add(EntitySquid.class);
        peacefulMobs.add(EntityBat.class);
        peacefulMobs.add(EntitySnowman.class);
        peacefulMobs.add(EntityVillager.class);
        peacefulMobs.add(EntityIronGolem.class);

    }

}
