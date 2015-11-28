package io.github.craftedcart.MFF.handler;

import io.github.craftedcart.MFF.client.gui.GuiCrystalRefinery;
import io.github.craftedcart.MFF.client.gui.GuiFFProjector;
import io.github.craftedcart.MFF.container.ContainerCrystalRefinery;
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

    public static final int FFProjector_TILE_ENTITY_GUI = 0;
    public static final int CrystalRefinery_TILE_ENTITY_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new ContainerCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == FFProjector_TILE_ENTITY_GUI) {

            BlockPos pos = new BlockPos(x, y, z);

            try {

                TEFFProjector te = (TEFFProjector) world.getTileEntity(pos);

                Field fX1 = te.getClass().getField("minX");
                Field fY1 = te.getClass().getField("minY");
                Field fZ1 = te.getClass().getField("minZ");
                Field fX2 = te.getClass().getField("maxX");
                Field fY2 = te.getClass().getField("maxY");
                Field fZ2 = te.getClass().getField("maxZ");

                int x1 = fX1.getInt(te);
                int y1 = fY1.getInt(te);
                int z1 = fZ1.getInt(te);
                int x2 = fX2.getInt(te);
                int y2 = fY2.getInt(te);
                int z2 = fZ2.getInt(te);

                return new GuiFFProjector(te, pos, x1, y1, z1, x2, y2, z2);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (ID == CrystalRefinery_TILE_ENTITY_GUI) {
            return new GuiCrystalRefinery(player.inventory, (TECrystalRefinery) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }

}
