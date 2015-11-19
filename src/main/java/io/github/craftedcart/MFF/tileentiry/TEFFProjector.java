package io.github.craftedcart.MFF.tileentiry;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.utility.LogHelper;
import io.github.craftedcart.MFF.utility.MathUtils;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox {

    private int minX = -5;
    private int maxX = 5;
    private int minY = -5;
    private int maxY = 5;
    private int minZ = -5;
    private int maxZ = 5;

    private int updateTime = 1;

    @Override
    public void update() {
        updateTime--;

        if (updateTime <= 0) {
            updateTime = 99; //4.95s (99t)

            BlockPos pos = this.getPos();

            for (int x = minX; x <= maxX; x++) {
                for (int y = minX; y <= maxY; y++) {
                    BlockPos ffPos = pos.add(x, y, minZ);
                    BlockPos ffNegativePos = pos.add(x, y, maxZ);

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

            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos ffPos = pos.add(minX, y, z);
                    BlockPos ffNegativePos = pos.add(maxX, y, z);

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
        } catch (NullPointerException e) {
            LogHelper.warn("Gah! Error when trying to refresh the decayTimer of the Forcefield at " + ffPos.toString() + " - Make sure that the chunk is loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
