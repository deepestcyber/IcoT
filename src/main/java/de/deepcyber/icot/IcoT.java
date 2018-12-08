package de.deepcyber.icot;


import de.deepcyber.icot.block.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

@Mod(modid = IcoT.MODID, name = IcoT.NAME, version = IcoT.VERSION)
@Mod.EventBusSubscriber
public class IcoT {
    public static final String MODID = "icot";
    public static final String NAME = "Intercraft of Things";
    public static final String VERSION = "0.0.0";

    private static List<BlockIcoTCommon> blocks = new ArrayList<>();
    private static List<ItemBlock> itemBlocks = new ArrayList<>();
/*
    public static BlockToggleThing blockToggleThing;
    public static ItemBlock itemBlockToggleThing;
    public static BlockRedstonePublisherThing blockRedstonePublisherThing;
    public static ItemBlock itemBlockRedstonePublisherThing;
    public static BlockRedstoneSubscriberThing blockRedstoneSubscriberThing;
    public static ItemBlock itemBlockRedstoneSubscriberThing;
*/
    public static Logger logger;

    public static Map<String, Boolean> activationMap = new HashMap<>();

    //private static MqttListenerThread mqttListenerThread;
    static private MqttConnector mqttConnector = new MqttConnector();

    public static boolean subscriberHigh = false;

    public static MqttConnector getMqttConnector() {
        return mqttConnector;
    }

    public static void preInitCommon() {
    }

    public static boolean queryActivation(String thing) {
        return activationMap.getOrDefault(thing, false);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("preinit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        /*
        logger.info("xxx DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        if (event.getSide().isServer()) {
            logger.info("INIT on Server");
        }*/
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        //logger.info("SERVER STARTED");
        mqttConnector.connect();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        //logger.info("SERVER STOPPED");
        mqttConnector.disconnect();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        logger.info("registerBlocks");

        blocks.add(new BlockActivator());
        blocks.add(new BlockPublisherThing());
        blocks.add(new BlockSubscriberThing());
        blocks.forEach((block)->event.getRegistry().register(block));
/*
        blockToggleThing = new BlockToggleThing(); //.setUnlocalizedName("icot_block_toggle_thing");
        blockRedstonePublisherThing = new BlockRedstonePublisherThing();
        blockRedstoneSubscriberThing = new BlockRedstoneSubscriberThing();
        event.getRegistry().registerAll(
                blockToggleThing.setRegistryName(MODID, "toggle_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS),
                blockRedstonePublisherThing.setRegistryName(MODID, "redstone_publisher_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS),
                blockRedstoneSubscriberThing.setRegistryName(MODID, "redstone_subscriber_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        );

        GameRegistry.registerTileEntity(TileEntityThing.class, new ResourceLocation(MODID, "toggle_thing"));
        GameRegistry.registerTileEntity(TileEntityRedstonePublisherThing.class, new ResourceLocation(MODID, "redstone_publisher_thing"));
        GameRegistry.registerTileEntity(TileEntityRedstoneSubscriberThing.class, new ResourceLocation(MODID , "redstone_subscriber_thing"));
        */
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        logger.info("registerItems");

        blocks.forEach((block)->event.getRegistry().register(block.getItemBlock()));
/*
        itemBlockToggleThing = new ItemBlock(blockToggleThing);
        itemBlockToggleThing.setRegistryName(blockToggleThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockToggleThing);
        itemBlockRedstonePublisherThing = new ItemBlock(blockRedstonePublisherThing);
        itemBlockRedstonePublisherThing.setRegistryName(blockRedstonePublisherThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockRedstonePublisherThing);
        itemBlockRedstoneSubscriberThing = new ItemBlock(blockRedstoneSubscriberThing);
        itemBlockRedstoneSubscriberThing.setRegistryName(blockRedstoneSubscriberThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockRedstoneSubscriberThing);*/
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) throws Exception {
        logger.info("registerModels");
        for (BlockIcoTCommon block:blocks) {
            ItemBlock itemBlock = block.getItemBlock();
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
        }
    }

}
