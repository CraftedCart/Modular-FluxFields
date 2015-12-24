package io.github.craftedcart.MFF.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class TEPoweredBlock extends TileEntity {

    public double power = 0;
    public double maxPower;
    public boolean isSendingPower = false;
    private double powerDrawRate;

    /**
     * Set up the Powered Block
     *
     * @param maxPower The maximum amount of power the TileEntity can store
     * @param powerDrawRate How much power the TileEntity can draw in one tick
     */
    public void init(double maxPower, double powerDrawRate) {
        this.maxPower = maxPower;
        this.powerDrawRate = powerDrawRate;
    }

    /**
     * Set up the Powered Block (Without powerDrawRate)
     *
     * @param maxPower The maximum amount of power the TileEntity can store
     */
    public void init(double maxPower) {
        init(maxPower, 0);
    }

    /**
     * Draw power from the Powered Block specified
     *
     * @param poweredBlock The Powered Block to draw power from
     */
    protected void drawPower(TEPoweredBlock poweredBlock) {

        if (power < maxPower) { //If we have space for more power

            double pcPower; //Power Cube Power

            pcPower = poweredBlock.power; //Get the power cube's power level

            if (pcPower > 0) { //If the power cube has more than 0 power
                if (pcPower <= powerDrawRate) { //If the power cube has less power than, or is equal to what the FF Projector draws in 1 tick
                    if (power + pcPower <= maxPower) { //If the projector's power + the power cube's power is less than or equal to the projector's max power value
                        power += pcPower; //Draw all power from the power cube
                        poweredBlock.power = 0; //Set the power cube's power level to 0
                    } else {
                        poweredBlock.power -= maxPower - power; //Minus the power cube's power level from the difference between the projector's power and max power
                        power = maxPower; //Set the projector's power level to the max
                    }
                } else {
                    if (power + powerDrawRate <= maxPower) { //If the projector's power + the power cube's power is mess than the projector's max power value
                        power += powerDrawRate; //Draw some power from the power cube
                        poweredBlock.power -= pcPower - powerDrawRate; //Minus the power drawn from the power cube
                    } else {
                        poweredBlock.power -= maxPower - power; //Draw the power difference between the projector's power and max power
                        power = maxPower; //Set the projector's power to the max
                    }
                }
            }

        }

    }

    /**
     * Send power to the Powered Block specified
     *
     * @param poweredBlock The Powered Block to send power to
     */
    protected void sendPower(TEPoweredBlock poweredBlock) { //Send power to a powered block

        double pcPower = poweredBlock.power;

        if (pcPower != poweredBlock.maxPower && power > 0) {
            isSendingPower = true;

            if (pcPower + power <= poweredBlock.maxPower) {
                poweredBlock.power += power;
                power = 0;
            } else {
                power -= poweredBlock.maxPower - pcPower;
                poweredBlock.power = poweredBlock.maxPower;
            }

        } else {
            isSendingPower = false;
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        writeSyncableDataToNBT(tagCompound);

        // ... Continue writing non-syncable data
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        readSyncableDataFromNBT(tagCompound);

        // ... Continue reading non-syncable data
    }

    void writeSyncableDataToNBT(NBTTagCompound tagCompound) {
        tagCompound.setDouble("power", power);
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        power = tagCompound.getDouble("power");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.getPos(), 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readSyncableDataFromNBT(pkt.getNbtCompound());
    }

}
