package de.deepcyber.icot;


import de.deepcyber.icot.block.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static net.minecraftforge.fml.relauncher.Side.*;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

@Mod(modid = IcoT.MODID, name = IcoT.NAME, version = IcoT.VERSION)
@Mod.EventBusSubscriber
public class IcoT {
    public static final String MODID = "icot";
    public static final String NAME = "Intercraft of Things";
    public static final String VERSION = "0.0.0";

    public static BlockToggleThing blockToggleThing;
    public static ItemBlock itemBlockToggleThing;
    public static BlockRedstonePublisherThing blockRedstonePublisherThing;
    public static ItemBlock itemBlockRedstonePublisherThing;
    public static BlockRedstoneSubscriberThing blockRedstoneSubscriberThing;
    public static ItemBlock itemBlockRedstoneSubscriberThing;

    public static Logger logger;

    //private static MqttListenerThread mqttListenerThread;
    public static IMqttClient mqttClient;

    public static boolean subscriberHigh = false;

    public static void preInitCommon() {
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("preinit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("xxx DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        if (event.getSide().isServer()) {
            logger.info("INIT on Server");
        }
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        logger.info("SERVER STARTED");

        String publisherId = UUID.randomUUID().toString();
        try {
            mqttClient = new MqttClient("tcp://localhost:1883", publisherId);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) { }

                @Override
                public void messageArrived(String t, MqttMessage m) throws Exception {
                    String s = new String(m.getPayload());
                    if (s.equals("high")) {
                        IcoT.logger.info("GOT HIGH");
                        IcoT.subscriberHigh = true;
                    } else if (s.equals("low")) {
                        IcoT.logger.info("GOT LOW");
                        IcoT.subscriberHigh = false;
                    } else {
                        IcoT.logger.info("GOT UNKNOWN: \"{}\"", s);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken t) { }
            });
            mqttClient.connect();
            mqttClient.subscribe("icot.in");
        } catch (MqttException e) {
            logger.error("Could not connect to mqtt broker, will not work");
        }

        //mqttListenerThread = new MqttListenerThread();
        //mqttListenerThread.start();

    }

    static public void sendMqtt(String topic, String message) {
        try {
            MqttMessage msg = new MqttMessage(message.getBytes());
            IcoT.mqttClient.publish(topic, msg);
        } catch (Exception e) {
            logger.info("Could not publish");
        }
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        logger.info("SERVER STOPPED");
        //mqttListenerThread.notifyStop();
        //mqttListenerThread = null;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        logger.info("registerBlocks");

        blockToggleThing = new BlockToggleThing(); //.setUnlocalizedName("icot_block_toggle_thing");
        blockRedstonePublisherThing = new BlockRedstonePublisherThing();
        blockRedstoneSubscriberThing = new BlockRedstoneSubscriberThing();
        event.getRegistry().registerAll(
                blockToggleThing.setRegistryName(MODID, "toggle_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS),
                blockRedstonePublisherThing.setRegistryName(MODID, "redstone_publisher_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS),
                blockRedstoneSubscriberThing.setRegistryName(MODID, "redstone_subscriber_thing").setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        );

        GameRegistry.registerTileEntity(TileEntityThing.class, MODID + ":toggle_thing");
        GameRegistry.registerTileEntity(TileEntityRedstonePublisherThing.class, MODID + ":redstone_publisher_thing");
        GameRegistry.registerTileEntity(TileEntityRedstoneSubscriberThing.class, MODID + ":redstone_subscriber_thing");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        logger.info("registerItems");
        itemBlockToggleThing = new ItemBlock(blockToggleThing);
        itemBlockToggleThing.setRegistryName(blockToggleThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockToggleThing);
        itemBlockRedstonePublisherThing = new ItemBlock(blockRedstonePublisherThing);
        itemBlockRedstonePublisherThing.setRegistryName(blockRedstonePublisherThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockRedstonePublisherThing);
        itemBlockRedstoneSubscriberThing = new ItemBlock(blockRedstoneSubscriberThing);
        itemBlockRedstoneSubscriberThing.setRegistryName(blockRedstoneSubscriberThing.getRegistryName());
        event.getRegistry().registerAll(itemBlockRedstoneSubscriberThing);
    }
/*
    @SubscribeEvent
    @SideOnly(CLIENT)
    public static void registerModels(ModelRegistryEvent event) throws Exception
    {
        ModelLoader.setCustomModelResourceLocation(itemBlockToggleThing);
        for (Field f : Items.class.getDeclaredFields())
        {
            Item item = (Item)f.get(null);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuarry.class, new QuarryRenderer());

    }*/
}
