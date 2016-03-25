package io.github.craftedcart.modularfluxfields.network;

import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by CraftedCart on 30/12/2015 (DD/MM/YYYY)
 */

public class MessageFFProjectorSendPowerStatsToClient implements IMessage {

    BlockPos pos;

    NBTTagIntArray powerUsagePerTickForPastMinute;
    NBTTagIntArray powerUsagePerSecondForPastHalfHour;

    public MessageFFProjectorSendPowerStatsToClient() {}

    public MessageFFProjectorSendPowerStatsToClient(BlockPos pos, NBTTagIntArray powerUsagePerTickForPastMinute, NBTTagIntArray powerUsagePerSecondForPastHalfHour) {
        this.pos = pos;
        this.powerUsagePerTickForPastMinute = powerUsagePerTickForPastMinute;
        this.powerUsagePerSecondForPastHalfHour = powerUsagePerSecondForPastHalfHour;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag;
        tag = ByteBufUtils.readTag(buf);
        int posX = tag.getInteger("posX");
        int posY = tag.getInteger("posY");
        int posZ = tag.getInteger("posZ");
        pos = new BlockPos(posX, posY, posZ);
        powerUsagePerTickForPastMinute = (NBTTagIntArray) tag.getTag("powerUsagePerTickForPastMinute");
        powerUsagePerSecondForPastHalfHour = (NBTTagIntArray) tag.getTag("powerUsagePerSecondForPastHalfHour");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("posX", pos.getX());
        tag.setInteger("posY", pos.getY());
        tag.setInteger("posZ", pos.getZ());
        tag.setTag("powerUsagePerTickForPastMinute", powerUsagePerTickForPastMinute);
        tag.setTag("powerUsagePerSecondForPastHalfHour", powerUsagePerSecondForPastHalfHour);
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<MessageFFProjectorSendPowerStatsToClient, IMessage> {

        @Override
        public IMessage onMessage(final MessageFFProjectorSendPowerStatsToClient message, final MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    TEFFProjector te = (TEFFProjector) Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.pos);

                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setTag("powerUsagePerTickForPastMinute", message.powerUsagePerTickForPastMinute);
                    tagCompound.setTag("powerUsagePerSecondForPastHalfHour", message.powerUsagePerSecondForPastHalfHour);

                    te.readPowerStatsFromNBT(tagCompound);

                }
            });
            return null;
        }

    }

}
