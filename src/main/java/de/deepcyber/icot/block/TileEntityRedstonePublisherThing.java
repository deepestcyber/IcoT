package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRedstonePublisherThing extends TileEntity {
    public boolean rsState = false;

    TileEntityRedstonePublisherThing() {

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
                IcoT.logger.info("RISING FLANK");
                //IcoT.sendMqtt("icot.out", "high");
            } else {
                IcoT.logger.info("FALLING FLANK");
                //IcoT.sendMqtt("icot.out", "low");
            }
            rsState=newRsState;
        }
    }
}
