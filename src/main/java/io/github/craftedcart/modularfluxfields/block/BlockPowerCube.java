package io.github.craftedcart.modularfluxfields.block;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerCube;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */
public class BlockPowerCube extends ModBlock implements ITileEntityProvider {

    public BlockPowerCube() {

        super();
        this.setUnlocalizedName(Names.BlockPowerCube);
        this.setHardness(4f);
        this.setHarvestLevel("pickaxe", 2); //Requires iron pickaxe to harvest

    }

    //This will tell minecraft not to render any side of our cube.
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    //And this tell it that you can see through this block, and neighbor blocks should be rendered.
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEPowerCube();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            player.openGui(ModModularFluxFields.instance, GuiHandler.PowerCube_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

}
