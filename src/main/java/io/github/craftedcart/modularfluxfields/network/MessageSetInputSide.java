package io.github.craftedcart.modularfluxfields.network;

import io.github.craftedcart.modularfluxfields.tileentity.TEPowerRelay;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by CraftedCart on 22/03/2016 (DD/MM/YYYY)
 */
public class MessageSetInputSide implements IMessage {

    private BlockPos pos;
    private EnumFacing enumFacing;

    public MessageSetInputSide() {}

    public MessageSetInputSide(BlockPos pos, EnumFacing enumFacing) {
        this.pos = pos;
        this.enumFacing = enumFacing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);

        int posX = tagCompound.getInteger("posX");
        int posY = tagCompound.getInteger("posY");
        int posZ = tagCompound.getInteger("posZ");
        pos = new BlockPos(posX, posY, posZ);

        enumFacing = EnumFacing.getFront(tagCompound.getInteger("enumFacing"));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setInteger("posX", pos.getX());
        tagCompound.setInteger("posY", pos.getY());
        tagCompound.setInteger("posZ", pos.getZ());

        tagCompound.setInteger("enumFacing", enumFacing.getIndex());

        ByteBufUtils.writeTag(buf, tagCompound);
    }

    public static class Handler implements IMessageHandler<MessageSetInputSide, IMessage> {

        @Override
        public IMessage onMessage(final MessageSetInputSide message, final MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    TEPowerRelay te = (TEPowerRelay) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);

                    te.setInputSide(message.enumFacing);

                }
            });
            return null;
        }
    }

}
