package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) {
            playerIn.openGui(ModMFF.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return true;
    }

}
