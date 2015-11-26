package io.github.craftedcart.MFF.eventhandler;

import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by CraftedCart on 25/11/2015 (DD/MM/YYYY)
 */

public class PreventFFBlockBreak {

    public static ArrayList<ArrayList<Object>> ffProjectors = new ArrayList<ArrayList<Object>>();
    private Set<ArrayList<Object>> hs = new HashSet<ArrayList<Object>>();

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {

        //Remove duplicates
        hs.addAll(ffProjectors);
        ffProjectors.clear();
        ffProjectors.addAll(hs);

        for (Iterator<ArrayList<Object>> iterator = ffProjectors.iterator(); iterator.hasNext();) {

            ArrayList al = iterator.next();

            World world = (World) al.get(0);
            BlockPos pos = (BlockPos) al.get(1);
            TileEntity te = world.getTileEntity(pos);
            TEFFProjector teFFProjector = (TEFFProjector) te;


            if (te != null) {
                try {

                    Field fIsPowered = teFFProjector.getClass().getField("isPowered");

                    if (fIsPowered.getBoolean(te)) {
                        Field fBlockList = teFFProjector.getClass().getField("blockList");
                        ArrayList<BlockPos> blockList = (ArrayList) fBlockList.get(te);

                        for (BlockPos p : blockList) {

                            if (p.getX() == event.pos.getX() &&
                                    p.getY() == event.pos.getY() &&
                                    p.getZ() == event.pos.getZ()) {

                                if (event.getPlayer().capabilities.isCreativeMode) {
                                    if (!event.getPlayer().worldObj.isRemote) {
                                        event.getPlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.mff:bypassedFFProtection")));
                                    }
                                } else {
                                    event.setCanceled(true);
                                    if (!event.getPlayer().worldObj.isRemote) {
                                        event.getPlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.mff:warnFFProtection")));
                                    }
                                }
                                break;

                            }

                        }
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else { iterator.remove(); }

        }
    }

}