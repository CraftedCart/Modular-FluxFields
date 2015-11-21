package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TEForcefield;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class BlockForcefield extends ModBlock implements ITileEntityProvider {

    public BlockForcefield() {

        super();
        this.setUnlocalizedName(Names.BlockForcefield);
        this.setBlockUnbreakable();

    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block == this) {
            return false;
        }

        return super.shouldSideBeRendered(world,pos, side);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEForcefield();
    }

}
