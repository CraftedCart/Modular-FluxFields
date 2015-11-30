package io.github.craftedcart.MFF.handler;

import io.github.craftedcart.MFF.client.gui.GuiCrystalRefinery;
import io.github.craftedcart.MFF.client.gui.GuiFFProjectorInfo;
import io.github.craftedcart.MFF.container.ContainerCrystalRefinery;
import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.tileentity.TECrystalRefinery;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 22/11/2015 (DD/MM/YYYY)
 */

public class GuiHandler implements IGuiHandler {

    public static final int FFProjector_Info_TILE_ENTITY_GUI = 0;
    public static final int CrystalRefinery_TILE_ENTITY_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FFProjector_Info_TILE_ENTITY_GUI) {
            return new ContainerFFProjectorInfo(player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new ContainerCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FFProjector_Info_TILE_ENTITY_GUI) {
            return new GuiFFProjectorInfo(player, player.inventory, (TEFFProjector) world.getTileEntity(new BlockPos(x, y, z)));
        } else if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new GuiCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

}
