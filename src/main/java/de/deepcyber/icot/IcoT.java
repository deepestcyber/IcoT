package de.deepcyber.icot;


import de.deepcyber.icot.block.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = IcoT.MODID, name = IcoT.NAME, version = IcoT.VERSION)
@Mod.EventBusSubscriber
public class IcoT {
    public static final String MODID = "icot";
    public static final String NAME = "Intercraft of Things";
    public static final String VERSION = "0.0.0";

    private static List<BlockIcoTCommon> blocks = new ArrayList<>();
    private static List<ItemBlock> itemBlocks = new ArrayList<>();
    public static Logger logger;

    private MqttConnector mqttConnector = new MqttConnector();
    public Map<String, Boolean> activationMap = new ConcurrentHashMap<>();
    public ThingRegister rsSubscribers = new ThingRegister();

    public MqttConnector getMqttConnector() {
        return mqttConnector;
    }

    @Mod.Instance(IcoT.MODID)
    public static IcoT instance;

    public static void preInitCommon() {
    }

    public boolean queryActivation(String thing) {
        return activationMap.getOrDefault(thing, false);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        // logger.info("preinit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
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

        //blocks.add(new BlockActivator());
        blocks.add(new BlockThing());
        blocks.add(new BlockPublisherThing());
        blocks.add(new BlockSubscriberThing());
        blocks.forEach((block)->event.getRegistry().register(block));

        GameRegistry.registerTileEntity(TileEntityPublisherThing.class, new ResourceLocation(MODID, BlockPublisherThing.registryName));
        GameRegistry.registerTileEntity(TileEntitySubscriberThing.class, new ResourceLocation(MODID, BlockSubscriberThing.registryName));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        logger.info("registerItems");

        blocks.forEach((block)->event.getRegistry().register(block.getItemBlock()));
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
