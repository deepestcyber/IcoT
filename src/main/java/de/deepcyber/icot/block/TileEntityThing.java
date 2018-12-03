package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityThing extends TileEntity {
    public boolean rsState = false;

    TileEntityThing() {

    }
/*
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        rsState = false;
        //if (nbt.hasKey("current"))
        //    this.current = new MutableBlockPos(BlockPos.fromLong(nbt.getLong("current")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //nbt.setLong("current", current.toLong());
        return nbt;
    }*/

    public void rsChange(boolean newRsState) {
        if (newRsState!=rsState) {
            // flank
            if (newRsState==true) {
                // upflank
                IcoT.logger.info("RISING FLANK");
                IcoT.sendMqtt("icot", "toggle");
            } else {
                // down flank
            }
            rsState=newRsState;
        }
    }
}
