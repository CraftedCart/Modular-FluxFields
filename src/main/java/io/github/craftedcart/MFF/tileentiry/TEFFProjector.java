package io.github.craftedcart.MFF.tileentiry;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.utility.LogHelper;
import io.github.craftedcart.MFF.utility.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox {

    private int updateTime = 99;

    @Override
    public void update() {
        updateTime--;

        if (updateTime == 0) {
            updateTime = 99;

            BlockPos pos = this.getPos();

            for (int x = -5; x < 6; x++) {
                for (int y = -5; y < 6; y++) {
                    BlockPos ffPos = pos.add(x, y, 5);
                    BlockPos ffNegativePos = pos.add(x, y, 5 * -1);

                    if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                        worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                    } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                        refreshFFTimer(ffPos);
                    }

                    if (worldObj.getBlockState(ffNegativePos) == Blocks.air.getDefaultState()) {
                        worldObj.setBlockState(ffNegativePos, ModBlocks.forcefield.getDefaultState());
                    } else if (worldObj.getBlockState(ffNegativePos) == ModBlocks.forcefield.getDefaultState()) {
                        refreshFFTimer(ffNegativePos);
                    }
                }
            }

            for (int z = -5; z < 6; z++) {
                for (int y = -5; y < 6; y++) {
                    BlockPos ffPos = pos.add(5, y, z);
                    BlockPos ffNegativePos = pos.add(5 * -1, y, z);

                    if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                        worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                    } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                        refreshFFTimer(ffPos);
                    }

                    if (worldObj.getBlockState(ffNegativePos) == Blocks.air.getDefaultState()) {
                        worldObj.setBlockState(ffNegativePos, ModBlocks.forcefield.getDefaultState());
                    } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                        refreshFFTimer(ffNegativePos);
                    }
                }
            }
        }
    }

    private void refreshFFTimer(BlockPos ffPos) {

        TileEntity te = worldObj.getTileEntity(ffPos);
        try {
            Field f = te.getClass().getField("decayTimer");
            f.setInt(te, MathUtils.randInt(100, 200)); //5s - 10s
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogHelper.warn("Gah! Error when trying to refresh the decayTimer of the Forcefield at " + ffPos.toString());
        }

    }

}
