package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TEPowerRelay;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */

public class BlockPowerRelay extends ModBlock implements ITileEntityProvider {

    public BlockPowerRelay() {

        super();
        this.setUnlocalizedName(Names.BlockPowerRelay);
        this.setHardness(6f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEPowerRelay();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

}
