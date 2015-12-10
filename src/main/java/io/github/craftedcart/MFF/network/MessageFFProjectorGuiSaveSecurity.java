package io.github.craftedcart.MFF.network;

import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 10/12/2015 (DD/MM/YYYY)
 */

public class MessageFFProjectorGuiSaveSecurity implements IMessage {

    BlockPos pos;

    List<List<String>> permittedPlayers;
    List<List<Object>> permissionGroups;

    public MessageFFProjectorGuiSaveSecurity() {}

    public MessageFFProjectorGuiSaveSecurity(BlockPos pos, List permittedPlayers, List permissionGroups) {
        this.pos = pos;
        this.permittedPlayers = permittedPlayers;
        this.permissionGroups = permissionGroups;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag;
        tag = ByteBufUtils.readTag(buf);
        int posX = tag.getInteger("posX");
        int posY = tag.getInteger("posY");
        int posZ = tag.getInteger("posZ");
        pos = new BlockPos(posX, posY, posZ);

        //Read players
        NBTTagCompound players = (NBTTagCompound) tag.getTag("permittedPlayers");
        List<List<String>> playersList = new ArrayList<List<String>>();

        int index = 0;
        while (players.hasKey(String.valueOf(index))) {

            List<String> playerData = new ArrayList<String>();

            playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("uuid"));
            playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("name"));
            playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("groupID"));

            playersList.add(playerData);

            index++;
        }
        this.permittedPlayers = playersList;

        //Read groups
        NBTTagCompound groups = (NBTTagCompound) tag.getTag("permissionGroups");
        List<List<Object>> groupsList = new ArrayList<List<Object>>();

        index = 0;
        while (groups.hasKey(String.valueOf(index))) {

            List<Object> groupData = new ArrayList<Object>();

            groupData.add(((NBTTagCompound) (groups.getTag(String.valueOf(index)))).getString("id"));

            groupsList.add(groupData);

            index++;
        }
        this.permissionGroups = groupsList;

    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("posX", pos.getX());
        tag.setInteger("posY", pos.getY());
        tag.setInteger("posZ", pos.getZ());

        //Set players list
        NBTTagCompound players = new NBTTagCompound();
        int index = 0;
        for (Object plr : permittedPlayers) {
            List plrList = (List) plr;

            NBTTagCompound playerData = new NBTTagCompound();
            playerData.setString("uuid", (String) plrList.get(0)); //Set player UUID
            playerData.setString("name", (String) plrList.get(1)); //Set player name
            playerData.setString("groupID", (String) plrList.get(2)); //Set player group ID

            players.setTag(String.valueOf(index), playerData);
            index++;
        }
        tag.setTag("permittedPlayers", players);

        //Set groups list
        NBTTagCompound groups = new NBTTagCompound();
        index = 0;
        for (Object group : permissionGroups) {
            List groupList = (List) group;

            NBTTagCompound groupData = new NBTTagCompound();
            groupData.setString("id", (String) groupList.get(0)); //Set group ID

            groups.setTag(String.valueOf(index), groupData);
            index++;
        }
        tag.setTag("permissionGroups", groups);

        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<MessageFFProjectorGuiSaveSecurity, IMessage> {

        @Override
        public IMessage onMessage(final MessageFFProjectorGuiSaveSecurity message, final MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    TEFFProjector te = (TEFFProjector) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);

                    te.permittedPlayers = message.permittedPlayers;
                    te.permissionGroups = message.permissionGroups;

                }
            });
            return null;
        }
    }

}
