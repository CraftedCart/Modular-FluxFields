package io.github.craftedcart.MFF.item;

import io.github.craftedcart.MFF.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class ItemEfficiencyUpgrade extends ModItem {

    public ItemEfficiencyUpgrade() {

        super();
        this.setUnlocalizedName(Names.ItemEfficiencyUpgrade);

    }

    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean par4) {

        lores.add(StatCollector.translateToLocal("stat.mff:-50%PowerUsageTick"));

    }

}
