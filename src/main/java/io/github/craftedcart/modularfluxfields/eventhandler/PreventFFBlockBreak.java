package io.github.craftedcart.modularfluxfields.eventhandler;

import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by CraftedCart on 25/11/2015 (DD/MM/YYYY)
 */
public class PreventFFBlockBreak {

    public static ArrayList<ArrayList<Object>> ffProjectors = new ArrayList<ArrayList<Object>>();
    private Set<ArrayList<Object>> hs = new HashSet<>();

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {

        //Remove duplicates
        hs.addAll(ffProjectors);
        ffProjectors.clear();
        ffProjectors.addAll(hs);

        for (Iterator<ArrayList<Object>> iterator = ffProjectors.iterator(); iterator.hasNext();) { //Loop through each registered forcefield projector

            ArrayList al = iterator.next();

            World world = (World) al.get(0);
            BlockPos pos = (BlockPos) al.get(1);
            TileEntity te = world.getTileEntity(pos);


            if (te instanceof TEFFProjector) { //If the tile entity is a forcefield projector

                if (((TEFFProjector) te).isPowered) { //If the ff projector is powered
                    ArrayList<BlockPos> blockList = ((TEFFProjector) te).wallBlockList; //Get the wall blocks

                    int index = 0;
                    for (BlockPos p : blockList) { //Loop through each wall block

                        if (((TEFFProjector) te).blockPlaceProgress >= index) { //If the forcefield has calculated that block
                            if (p.getX() == event.pos.getX() &&
                                    p.getY() == event.pos.getY() &&
                                    p.getZ() == event.pos.getZ()) { //If the block broken matches a protected block

                                if (event.getPlayer().capabilities.isCreativeMode) { //If the player is in creative mode
                                    if (!event.getPlayer().worldObj.isRemote) {
                                        event.getPlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.modularfluxfields:bypassedFFProtection")));
                                    }
                                } else {
                                    event.setCanceled(true);
                                    if (!event.getPlayer().worldObj.isRemote) {
                                        event.getPlayer().addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.modularfluxfields:warnFFProtection")));
                                    }
                                }
                                break;

                            }
                        } else {
                            break;
                        }

                        index++;

                    }
                }

            } else {
                iterator.remove();
            }

        }
    }

}
