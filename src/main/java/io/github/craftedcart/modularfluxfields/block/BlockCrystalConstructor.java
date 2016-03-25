package io.github.craftedcart.modularfluxfields.block;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.TECrystalConstructor;
import io.github.craftedcart.modularfluxfields.tileentity.TECrystalRefinery;
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
 * Created by CraftedCart on 14/12/2015 (DD/MM/YYYY)
 */
public class BlockCrystalConstructor extends ModBlock implements ITileEntityProvider {

    public BlockCrystalConstructor() {

        super();
        this.setUnlocalizedName(Names.BlockCrystalConstructor);
        this.setHardness(6f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TECrystalConstructor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(ModModularFluxFields.instance, GuiHandler.CrystalConstructor_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TECrystalConstructor te = (TECrystalConstructor) world.getTileEntity(pos);
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
