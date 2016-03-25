package io.github.craftedcart.modularfluxfields.block;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        this.setHardness(6f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEFFProjector();
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) {
            player.openGui(ModModularFluxFields.instance, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TEFFProjector te = (TEFFProjector) worldIn.getTileEntity(pos);
        te.owner = String.valueOf(placer.getPersistentID());
        te.ownerName = placer.getName();

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
