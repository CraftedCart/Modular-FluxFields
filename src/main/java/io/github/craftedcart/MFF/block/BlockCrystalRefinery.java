package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.tileentity.TECrystalRefinery;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class BlockCrystalRefinery extends ModBlock implements ITileEntityProvider {

    public BlockCrystalRefinery() {

        super();
        this.setUnlocalizedName(Names.BlockCrystalRefinery);
        this.setHardness(6f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TECrystalRefinery();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(ModMFF.instance, GuiHandler.CrystalRefinery_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TECrystalRefinery te = (TECrystalRefinery) world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);
        super.breakBlock(world, pos, blockstate);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((TECrystalRefinery) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

}
