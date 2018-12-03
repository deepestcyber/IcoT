package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRedstonePublisherThing extends BlockHorizontal {

    public BlockRedstonePublisherThing() {
        super(Material.IRON);
//        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setUnlocalizedName(IcoT.MODID + ":block.redstone_publisher_thing");
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
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //IcoT.logger.info("NeighborChange");
        //IcoT.logger.info("pos: {}", pos);
        if(worldIn.isRemote)
            return;
        boolean rsState = worldIn.isBlockPowered(fromPos);
        TileEntity te = worldIn.getTileEntity(pos);
        TileEntityRedstonePublisherThing tet = (TileEntityRedstonePublisherThing) te;
        tet.rsChange(rsState);
    }


    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRedstonePublisherThing();
    }


}
