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

/**
 * Created by CraftedCart on 22/11/2015 (DD/MM/YYYY)
 */

public class MessageFFProjectorGuiSaveSizing implements IMessage {

    BlockPos pos;

    int x1;
    int y1;
    int z1;
    int x2;
    int y2;
    int z2;

    public MessageFFProjectorGuiSaveSizing() {}

    public MessageFFProjectorGuiSaveSizing(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.pos = pos;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag;
        tag = ByteBufUtils.readTag(buf);
        int posX = tag.getInteger("posX");
        int posY = tag.getInteger("posY");
        int posZ = tag.getInteger("posZ");
        pos = new BlockPos(posX, posY, posZ);
        x1 = tag.getInteger("x1");
        y1 = tag.getInteger("y1");
        z1 = tag.getInteger("z1");
        x2 = tag.getInteger("x2");
        y2 = tag.getInteger("y2");
        z2 = tag.getInteger("z2");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("posX", pos.getX());
        tag.setInteger("posY", pos.getY());
        tag.setInteger("posZ", pos.getZ());
        tag.setInteger("x1", x1);
        tag.setInteger("y1", y1);
        tag.setInteger("z1", z1);
        tag.setInteger("x2", x2);
        tag.setInteger("y2", y2);
        tag.setInteger("z2", z2);
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<MessageFFProjectorGuiSaveSizing, IMessage> {

        @Override
        public IMessage onMessage(final MessageFFProjectorGuiSaveSizing message, final MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    TEFFProjector te = (TEFFProjector) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);

                    te.minX = message.x1;
                    te.maxX = message.x2;
                    te.minY = message.y1;
                    te.maxY = message.y2;
                    te.minZ = message.z1;
                    te.maxZ = message.z2;

                    te.getBlocks();

                }
            });
            return null;
        }
    }

}
