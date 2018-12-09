package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TileEntitySubscriberThing extends TileEntity implements ITickable {
    private String name = "one";
    private boolean checkNeeded = false;

    public TileEntitySubscriberThing() {
    }

    public void notifyCheckNeeded() {
        checkNeeded = true;
    }

    public void update() {
        if (world.isRemote)
            return;
        if (checkNeeded) {
            checkNeeded = false; // should be first statement in this if block
            updateRedstoneState();
        }
    }

    public void onLoad() {
        // add to thing register
        IcoT.instance.rsSubscribers.register(name, world, pos);
    }

    public void onChunkUnload() {
        // remove from thing register
        IcoT.instance.rsSubscribers.unregister(name, world, pos);
    }


   // public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    private void updateRedstoneState() {
        if (world.isRemote) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        boolean currentPowered = BlockSubscriberThing.isPowered(state);
        boolean newPowered = IcoT.instance.queryActivation(name);
        if (currentPowered!=newPowered) {
            IcoT.logger.info("changing power to {} at {}", newPowered, pos);
            state = state.cycleProperty(BlockSubscriberThing.POWERED);
            world.setBlockState(pos, state, 3);
            world.notifyNeighborsOfStateChange(pos, BlockSubscriberThing.instance, false);
        }
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
