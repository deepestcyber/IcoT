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
import net.minecraft.world.World;

public class BlockPublisherThing extends BlockIcoTCommon {
    public static final String registryName = "publisher_thing";

    private static final PropertyBool POWERED = PropertyBool.create("powered");

    static private boolean isPowered(IBlockState state) {
        return state.getValue(POWERED);
    }

    static private boolean isPowered(int meta) {
        return (meta & 8) > 0;
    }

    public BlockPublisherThing() {
        super(registryName, Material.CIRCUITS);
    }


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //IcoT.logger.info("NeighborChange");
        //IcoT.logger.info("pos: {}", pos);
        if (worldIn.isRemote)
            return;
        boolean poweredBefore = isPowered(state);
        boolean poweredNow = worldIn.isSidePowered(fromPos, EnumFacing.NORTH);
        //boolean poweredNow = worldIn.isBlockPowered(fromPos);
        if (poweredNow != poweredBefore) {
            IcoT.logger.info("Publisher power changed to {}", poweredNow);
            state = state.cycleProperty(POWERED);
            worldIn.setBlockState(pos, state, 3);
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityPublisherThing) {
                TileEntityPublisherThing tep = (TileEntityPublisherThing) te;
                IcoT.instance.getMqttConnector().publishRedstone(tep.getName(), poweredNow);
            }
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
        return new TileEntityPublisherThing();
    }

}


