package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRedstoneSubscriberThing extends BlockHorizontal {

    public BlockRedstoneSubscriberThing() {
        super(Material.IRON);
        this.setUnlocalizedName(IcoT.MODID + ":block.redstone_subscriber_thing");
    }


    @Override
    public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (!(worldIn instanceof World)) return 0;
        World world = (World) worldIn; // We're provided with IBlockAccess instead of World because this is sometimes called
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityRedstoneSubscriberThing)) return 0;
        TileEntityRedstoneSubscriberThing tes = (TileEntityRedstoneSubscriberThing) te;
        if (tes.rsState) {
            IcoT.logger.info("15");
            return 15;
        } else {
            IcoT.logger.info("0");
            return 0;
        }
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (!(worldIn instanceof World)) return 0;
        World world = (World) worldIn; // We're provided with IBlockAccess instead of World because this is sometimes called
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityRedstoneSubscriberThing)) return 0;
        TileEntityRedstoneSubscriberThing tes = (TileEntityRedstoneSubscriberThing) te;
        if (tes.rsState) {
            IcoT.logger.info("15");
            return 15;
        } else {
            IcoT.logger.info("0");
            return 0;
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
        }
    }


    @Override
    public boolean canProvidePower(IBlockState iBlockState)
    {
        return true;
    }


    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRedstoneSubscriberThing();
    }


}
