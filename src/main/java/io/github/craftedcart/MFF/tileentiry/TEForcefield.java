package io.github.craftedcart.MFF.tileentiry;

import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEForcefield extends TileEntity implements IUpdatePlayerListBox {

    public int decayTimer = 100; //5s

    @Override
    public void update() {
        decayTimer--;

        if (decayTimer <= 0) {

            //Destroy forcefield block
            worldObj.setBlockState(this.getPos(), Blocks.air.getDefaultState());

        }
    }

}
