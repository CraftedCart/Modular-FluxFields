package io.github.craftedcart.modularfluxfields.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */
public class TEPowerRelay extends TEPoweredBlock {

    public EnumFacing inputSide = EnumFacing.NORTH;
    public EnumFacing outputSide = EnumFacing.SOUTH;

    @Override
    public void init() {
        super.init();

        setup(1, 1);
    }

    public void setInputSide(EnumFacing inputSide) {
        this.inputSide = inputSide;
        worldObj.markBlockForUpdate(pos);
    }

    public EnumFacing getInputSide() {
        return inputSide;
    }

    public void setOutputSide(EnumFacing outputSide) {
        this.outputSide = outputSide;
        worldObj.markBlockForUpdate(pos);
    }

    public EnumFacing getOutputSide() {
        return outputSide;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        writeSyncableDataToNBT(nbt);
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        readSyncableDataFromNBT(nbt);
    }

    void writeSyncableDataToNBT(NBTTagCompound tagCompound) {
        super.writeSyncableDataToNBT(tagCompound);

        tagCompound.setInteger("inputSide", inputSide.getIndex());
        tagCompound.setInteger("outputSide", outputSide.getIndex());
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        super.readSyncableDataFromNBT(tagCompound);

        inputSide = EnumFacing.getFront(tagCompound.getInteger("inputSide"));
        outputSide = EnumFacing.getFront(tagCompound.getInteger("outputSide"));
    }

}
