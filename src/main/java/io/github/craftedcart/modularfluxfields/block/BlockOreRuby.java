package io.github.craftedcart.modularfluxfields.block;

import io.github.craftedcart.modularfluxfields.init.ModItems;
import io.github.craftedcart.modularfluxfields.reference.Names;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Created by CraftedCart on 15/12/2015 (DD/MM/YYYY)
 */

public class BlockOreRuby extends ModBlock {

    private int least_quantity = 1;
    private int most_quantity = 4;

    public BlockOreRuby() {

        super();
        this.setUnlocalizedName(Names.BlockOreRuby);
        this.setHardness(5f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    @Override
    public Item getItemDropped(IBlockState blockstate, Random random, int fortune) {
        return ModItems.rawRuby;
    }

    @Override
    public int quantityDropped(IBlockState blockstate, int fortune, Random random) {
        return this.least_quantity + random.nextInt(this.most_quantity - this.least_quantity + fortune + 1);
    }

}
