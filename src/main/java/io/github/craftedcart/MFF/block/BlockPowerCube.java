package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.tileentity.TEPowerCube;
import net.minecraft.block.ITileEntityProvider;
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
        this.setUnlocalizedName("powerCube");
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

}
