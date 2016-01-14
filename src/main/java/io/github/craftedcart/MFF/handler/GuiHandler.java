package io.github.craftedcart.MFF.handler;

import io.github.craftedcart.MFF.client.gui.*;
import io.github.craftedcart.MFF.container.*;
import io.github.craftedcart.MFF.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by CraftedCart on 22/11/2015 (DD/MM/YYYY)
 */

public class GuiHandler implements IGuiHandler {

    //Define GUI IDs
    public static final int FFProjector_Info_TILE_ENTITY_GUI = 0;
    public static final int FFProjector_Sizing_TILE_ENTITY_GUI = 1;
    public static final int FFProjector_Security_TILE_ENTITY_GUI = 2;
    public static final int FFProjector_Upgrades_TILE_ENTITY_GUI = 3; //TODO Implement this Gui
    public static final int FFProjector_PowerStats_TILE_ENTITY_GUI = 4;
    public static final int CrystalRefinery_TILE_ENTITY_GUI = 5;
    public static final int CrystalConstructor_TILE_ENTITY_GUI = 6;
    public static final int PowerCube_TILE_ENTITY_GUI = 7;
    public static final int SolarPowerGenerator_TILE_ENTITY_GUI = 8;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FFProjector_Sizing_TILE_ENTITY_GUI) {
            return new ContainerFFProjectorSizing(player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new ContainerCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == FFProjector_Security_TILE_ENTITY_GUI) {
            return new ContainerFFProjectorSecurity(player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalConstructor_TILE_ENTITY_GUI) {
            return new ContainerCrystalConstructor(player.inventory, (TECrystalConstructor) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FFProjector_Info_TILE_ENTITY_GUI) {
            return new GuiFFProjectorInfo(player, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == FFProjector_Sizing_TILE_ENTITY_GUI) {
            return new GuiFFProjectorSizing(player, player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new GuiCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == FFProjector_Security_TILE_ENTITY_GUI) {
            return new GuiFFProjectorSecurity(player, player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == FFProjector_PowerStats_TILE_ENTITY_GUI) {
            return new GuiFFProjectorPowerStats(player, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalConstructor_TILE_ENTITY_GUI) {
            return new GuiCrystalConstructor(player.inventory, (TECrystalConstructor) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == PowerCube_TILE_ENTITY_GUI) {
            return new GuiPowerCube((TEPowerCube) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == SolarPowerGenerator_TILE_ENTITY_GUI) {
            return new GuiSolarPowerGenerator((TEPowerGenerator) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

}
