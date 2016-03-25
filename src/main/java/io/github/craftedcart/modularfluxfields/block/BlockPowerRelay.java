package io.github.craftedcart.modularfluxfields.block;

import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerRelay;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        TileEntity te = worldIn.getTileEntity(pos);

        if (te != null && te instanceof TEPowerRelay) {

            TEPowerRelay tePowerRelay = (TEPowerRelay) te;

            int directionInt = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            switch (directionInt) {
                case 0: { //North
                    tePowerRelay.setInputSide(EnumFacing.NORTH);
                    break;
                }
                case 1: { //East
                    tePowerRelay.setInputSide(EnumFacing.EAST);
                    break;
                }
                case 2: { //South
                    tePowerRelay.setInputSide(EnumFacing.SOUTH);
                    break;
                }
                case 3: { //West
                    tePowerRelay.setInputSide(EnumFacing.WEST);
                    break;
                }
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
