package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityPublisherThing extends TileEntity {
    private String name = "one";
    private boolean checkNeeded = false;

    public TileEntityPublisherThing() {
    }

    public String getName() {
        return name;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("thing"))
            this.name = nbt.getString("thing");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("thing", name);
        return nbt;
    }


}
