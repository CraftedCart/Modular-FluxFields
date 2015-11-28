package io.github.craftedcart.MFF.item;

import io.github.craftedcart.MFF.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class ItemSpeedUpgrade extends ModItem {

    public ItemSpeedUpgrade() {

        super();
        this.setUnlocalizedName(Names.ItemSpeedUpgrade);

    }

    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean par4) {

        lores.add(StatCollector.translateToLocal("stat.mff:+100%Speed"));
        lores.add(StatCollector.translateToLocal("stat.mff:+125%PowerUsageTick"));

    }

}
