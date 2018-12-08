package de.deepcyber.icot.block;

import de.deepcyber.icot.IcoT;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIcoTCommon extends Block {
    private ItemBlock itemBlock = null;

    public BlockIcoTCommon(String registryName, Material material) {
        super(material);
        this.setRegistryName(registryName);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setUnlocalizedName(IcoT.MODID + ":block." + registryName);
    }

    public ItemBlock getItemBlock() {
        if (itemBlock==null) {
            itemBlock = new ItemBlock(this);
            itemBlock.setRegistryName(this.getRegistryName());
        }
        return itemBlock;
    }
}
