package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class BlockFFProjector extends ModBlock implements ITileEntityProvider {

    public BlockFFProjector() {

        super();
        this.setUnlocalizedName(Names.BlockFFProjector);

    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEFFProjector();
    }

}
