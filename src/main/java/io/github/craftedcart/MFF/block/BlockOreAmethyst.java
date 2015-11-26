package io.github.craftedcart.MFF.block;

/**
 * Created by CraftedCart on 26/11/2015 (DD/MM/YYYY)
 */

public class BlockOreAmethyst extends ModBlock {

    public BlockOreAmethyst() {

        super();
        this.setUnlocalizedName("oreAmethyst");
        this.setHardness(5f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

}
