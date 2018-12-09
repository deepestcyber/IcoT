package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSubscriberThing extends BlockIcoTCommon {
    public static String registryName = "subscriber_thing";
    public static BlockSubscriberThing instance;
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    static public boolean isPowered(IBlockState state) {
        return state.getValue(POWERED);
    }

    static public boolean isPowered(int meta) {
        return (meta & 8) > 0;
    }

    public BlockSubscriberThing() {
        super(registryName, Material.CIRCUITS);
        instance = this;
    }

    @Override
    public boolean canProvidePower(IBlockState iBlockState) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (isPowered(state)) {
            //IcoT.logger.info("15");
            return 15;
        } else {
            //IcoT.logger.info("0");
            return 0;
        }
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, isPowered(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        //i = i | ((BlockLever.EnumOrientation)state.getValue(FACING)).getMetadata();

        if (isPowered(state)) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySubscriberThing();
    }

}


