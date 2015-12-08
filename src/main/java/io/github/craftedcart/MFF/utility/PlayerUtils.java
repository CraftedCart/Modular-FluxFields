package io.github.craftedcart.MFF.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CraftedCart on 07/12/2015 (DD/MM/YYYY)
 */

public class PlayerUtils {

    public static List<Object> getUUIDFromPlayerName(String name) {

        List<EntityPlayer> onlinePlayers = Minecraft.getMinecraft().theWorld.playerEntities;

        Iterator iterator = onlinePlayers.iterator();

        while (iterator.hasNext()) {
            EntityPlayer plr = (EntityPlayer) iterator.next();

            if (plr.getName().toString().toLowerCase().equals(name.toLowerCase())) {
                List<Object> toReturn = new ArrayList<Object>();
                toReturn.add(plr.getUniqueID());
                toReturn.add(plr.getName());
                return toReturn;
            }

        }

        return null;

    }

}
