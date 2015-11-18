package io.github.craftedcart.MFF.tileentiry;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.utility.MathUtils;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox {

    @Override
    public void update() {
        BlockPos pos = this.getPos();

        for (int x = -5; x < 6; x++) {
            for (int y = -5; y < 6; y++) {
                BlockPos ffPos = pos.add(x, y, 5);
                BlockPos ffNegativePos = pos.add(x, y, 5 * -1);

                if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                    worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                }

                if (worldObj.getBlockState(ffNegativePos) == Blocks.air.getDefaultState()) {
                    worldObj.setBlockState(ffNegativePos, ModBlocks.forcefield.getDefaultState());
                }
            }
        }

        for (int z = -5; z < 6; z++) {
            for (int y = -5; y < 6; y++) {
                BlockPos ffPos = pos.add(5, y, z);
                BlockPos ffNegativePos = pos.add(5 * -1, y, z);

                if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                    worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                }
                if (worldObj.getBlockState(ffNegativePos) == Blocks.air.getDefaultState()) {
                    worldObj.setBlockState(ffNegativePos, ModBlocks.forcefield.getDefaultState());
                }
            }
        }
    }

}
