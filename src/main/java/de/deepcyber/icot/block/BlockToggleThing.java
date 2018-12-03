package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class BlockToggleThing extends BlockHorizontal {

    public BlockToggleThing() {
        super(Material.IRON);
//        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setUnlocalizedName(IcoT.MODID + ":block.toggle_thing");
    }


/*
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos posConnectingFrom, EnumFacing side)
    {
        if (side == null) return false;
        if (side == EnumFacing.UP || side == EnumFacing.DOWN) return false;

        // we can connect to three of the four side faces - if the block is facing north, then we can
        //  connect to WEST, SOUTH, or EAST.

        EnumFacing whichFaceOfLamp = side.getOpposite();
        EnumFacing blockFacingDirection = (EnumFacing)state.getValue(PROPERTYFACING);

        if (whichFaceOfLamp == blockFacingDirection) return false;
        return true;
    }
*/

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityThing();
}

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //IcoT.logger.info("NeighborChange");
        //IcoT.logger.info("pos: {}", pos);
        if(worldIn.isRemote)
            return;
        //IcoT.logger.info("pos powered: {}", worldIn.isBlockPowered(pos));
        //IcoT.logger.info("fromPos powered: {}", worldIn.isBlockPowered(fromPos));
        boolean rsState = worldIn.isBlockPowered(fromPos);
        TileEntity te = worldIn.getTileEntity(pos);
        TileEntityThing tet = (TileEntityThing) te;
        tet.rsChange(rsState);
    }


    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        IcoT.logger.info("onNeighborChange");
        IcoT.logger.info("pos: {}", pos);
    }
}
