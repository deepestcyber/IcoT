package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static net.minecraftforge.fml.relauncher.Side.SERVER;

public class TileEntityRedstoneSubscriberThing extends TileEntity implements ITickable {
    public boolean rsState = false;
    public boolean upnow = false;

    TileEntityRedstoneSubscriberThing() {
    }

    @java.lang.Override
    //@SideOnly(SERVER)
    public void update() {
        if (world.isRemote)
            return;
        if (rsState!=IcoT.subscriberHigh) {
            markDirty();
            rsState = IcoT.subscriberHigh;
            upnow = true;
        }
        else if (upnow) {
            upnow = false;
            IcoT.logger.info("Tile: {}", rsState);
            IcoT.logger.info("Pos: {}", pos);
            IcoT.logger.info("Loaded; {}", world.isBlockLoaded(pos));
            if (world.isBlockLoaded(pos)) {
                IBlockState bs = world.getBlockState(pos);
                Block block = bs.getBlock();
                IcoT.logger.info("Block: {}", block);
                world.setBlockState(pos, bs, 3);
                world.notifyNeighborsOfStateExcept(pos, block, EnumFacing.DOWN);
            }
        }
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

}
