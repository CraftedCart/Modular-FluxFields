package io.github.craftedcart.MFF.block;

import io.github.craftedcart.MFF.reference.Names;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TESolarPowerGenerator;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class BlockSolarPowerGenerator1 extends ModBlock implements ITileEntityProvider {

    public BlockSolarPowerGenerator1() {

        super();
        this.setUnlocalizedName(Names.BlockSolarPowerGenerator1);
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
        TESolarPowerGenerator tepg = new TESolarPowerGenerator(); //tepc, short for TileEntityPowerGenerator
        tepg.init(PowerConf.solarPowerGeneratorMaxPower);
        tepg.initSolar(PowerConf.solarPowerGeneratorBaseGenRate);
        return tepg;
    }

}
