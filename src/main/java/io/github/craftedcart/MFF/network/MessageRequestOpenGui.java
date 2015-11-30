package io.github.craftedcart.MFF.network;

import io.github.craftedcart.MFF.ModMFF;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.UUID;

/**
 * Created by CraftedCart on 30/11/2015 (DD/MM/YYYY)
 */

public class MessageRequestOpenGui implements IMessage {

    BlockPos pos;
    EntityPlayer player;
    int guiID;

    public MessageRequestOpenGui() {}

    public MessageRequestOpenGui(BlockPos pos, EntityPlayer player, int guiID) {
        this.pos = pos;
        this.player = player;
        this.guiID = guiID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag;
        tag = ByteBufUtils.readTag(buf);
        int posX = tag.getInteger("posX");
        int posY = tag.getInteger("posY");
        int posZ = tag.getInteger("posZ");
        pos = new BlockPos(posX, posY, posZ);
        player = lookupOwner(UUID.fromString(tag.getString("player")));
        guiID = tag.getInteger("guiID");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("posX", pos.getX());
        tag.setInteger("posY", pos.getY());
        tag.setInteger("posZ", pos.getZ());
        tag.setString("player", String.valueOf(player.getUniqueID()));
        tag.setInteger("guiID", guiID);
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<MessageRequestOpenGui, IMessage> {

        @Override
        public IMessage onMessage(final MessageRequestOpenGui message, final MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    TEFFProjector te = (TEFFProjector) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);

                    if (message.player != null) {
                        message.player.openGui(ModMFF.instance, message.guiID, message.player.worldObj, message.pos.getX(), message.pos.getY(), message.pos.getZ());
                    }

                }
            });
            return null;
        }
    }

    private EntityPlayer lookupOwner(UUID ownerID) {
        if (ownerID == null) {
            return null;
        }
        List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP plr : allPlayers) {
            if (plr.getUniqueID().equals(ownerID)) {
                return plr;
            }
        }
        return null;
    }

}
