package io.github.craftedcart.MFF.tileentiry;

import io.github.craftedcart.MFF.utility.MathUtils;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEForcefield extends TileEntity implements IUpdatePlayerListBox {

    public int decayTimer = MathUtils.randInt(100, 200); //Random decay time between 5s and 100s (100t and 200t)

    @Override
    public void update() {
        decayTimer--;

        if (decayTimer <= 0) {

            //Destroy forcefield block
            worldObj.setBlockState(this.getPos(), Blocks.air.getDefaultState());

        }
    }

}
