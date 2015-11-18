package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.creativetab.ModCreativeTab;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class ModBlock extends Block {

    public ModBlock(Material material) {
        super(material);
        this.setCreativeTab(ModCreativeTab.MFF_TAB);
    }

    public ModBlock() {
        super(Material.rock);
        this.setCreativeTab(ModCreativeTab.MFF_TAB);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

}
