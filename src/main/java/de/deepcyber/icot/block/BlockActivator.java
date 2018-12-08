package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockActivator extends BlockIcoTCommon {
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockActivator() {
        super("activator", Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
    }

    @Override
    @Deprecated
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    @Deprecated
    public final int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing oppositeSide) {
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            state = state.cycleProperty(POWERED);
            worldIn.setBlockState(pos, state, 3);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            return true;
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (((Boolean)state.getValue(POWERED)).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        //i = i | ((BlockLever.EnumOrientation)state.getValue(FACING)).getMetadata();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWERED});
    }

}
