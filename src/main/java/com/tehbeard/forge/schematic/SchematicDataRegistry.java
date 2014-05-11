package com.tehbeard.forge.schematic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.logging.log4j.Logger;

import com.tehbeard.forge.schematic.extensions.ClassCatalogue;
import com.tehbeard.forge.schematic.extensions.LayersExtension;
import com.tehbeard.forge.schematic.extensions.SchExtension;
import com.tehbeard.forge.schematic.extensions.SchematicExtension;
import com.tehbeard.forge.schematic.extensions.TagsExtension;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;
import com.tehbeard.forge.schematic.extensions.id.IdTranslateExtension;
import com.tehbeard.forge.schematic.handlers.SchematicDataHandler;
import com.tehbeard.forge.schematic.handlers.tileentity.TileEntityTranslator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;

/**
 * Main entry point for LibSchematic.<br/>
 * This class acts as the FML mod class,<br/>
 * it handles holding data handlers for mod blocks<br/>
 * and schematic extensions to access extra information <br/>
 * embedded in a schematic
 * 
 * @author James
 * 
 */
@Mod(modid = "libschematic", name = "LibSchematic", version = "1.00")
public class SchematicDataRegistry {

    // BEGIN FORGE MOD SECTION
    public static final boolean DEBUG_MODE = false;

    private static Logger logger;

    // initialise our shit
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    // Register our shit
    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        logger.info("Polling block array for Schematic data handlers");
        //Check all blocks for SchematicDataHandler
    }

    /*
     * @EventHandler public void IMC(IMCEvent event){
     * 
     * for(IMCMessage msg : event.getMessages()){ if(msg.isNBTMessage()){ //TODO
     * - IMC interface } else { logger.warning("Invalid IMC message from " +
     * msg.getSender()); } } }
     */

    /**
     * Returns the logger for this mod
     * 
     * @return
     */
    public static Logger logger() {
        return logger;
    }

    // END FORGE MOD SECTION

    // data handlers for blocks
    public static final SchematicDataHandler[] dataHandlers = new SchematicDataHandler[4096];

    /**
     * Initialise vanilla data handlers
     */
    

    private static final ClassCatalogue<SchematicExtension> schematicExtensions = new ClassCatalogue<SchematicExtension>(
            SchExtension.class) {

        @Override
        protected String getTag(Class<? extends SchematicExtension> _class) {

            return _class.getAnnotation(SchExtension.class).checkPath();
        }

    };

    public static final Map<String, TileEntityTranslator> tileEntityManager = new HashMap<String, TileEntityTranslator>();
    public static final TileEntityTranslator defaultTileEntityManager = new TileEntityTranslator();

    /**
     * Adds an extension for usage.
     * 
     * @param _class
     */
    public void addSchematicExtension(Class<? extends SchematicExtension> _class) {
        schematicExtensions.addProduct(_class);
    }

    /**
     * Initialise installed extensions
     */
    static {
        schematicExtensions.addProduct(WorldEditVectorExtension.class);
        schematicExtensions.addProduct(LayersExtension.class);
        schematicExtensions.addProduct(TagsExtension.class);
        schematicExtensions.addProduct(IdTranslateExtension.class);
    }

    /**
     * Add a data handler for a block, to be used by other forge mods to add
     * compatibility.<br/>
     * A data handler is a class that implements one to several interfaces, such
     * as those in <code>com.tehbeard.schematic.data</code><br/>
     * These interfaces provide a way for other forge mods to instruct
     * LibSchematic on how to process blocks/tile entities, such as rotation or
     * owner of the block.
     * 
     * @param blockId
     *            id of the block you wish to configure e.g. minecraft:chest
     * @param handler
     *            object that implements one of several
     *            {@link SchematicDataHandler} interfaces for manipulating a
     *            block
     * @throws IllegalArgumentException
     *             on invalid block id
     */
    public static void setHandler(String blockNamespace, SchematicDataHandler handler) {
        Block b = GameData.getBlockRegistry().getObject(blockNamespace);
        if(b == null){
            logger.warn("Passed {0} but could not find a block named this!",blockNamespace);
            return;
        }
        int id  = GameData.getBlockRegistry().getIDForObject(b);
        dataHandlers[id] = handler;
    }

    /**
     * return data handler for block, it may implement one or several interfaces
     * related to manipulation of a block/tile entity
     * 
     * @param blockId
     *            block if to get {@link SchematicDataHandler} for
     * @return {@link SchematicDataHandler} or null if no block found,
     * @throws IllegalArgumentException
     *             on invalid block id
     */
    public static SchematicDataHandler getHandler(int blockId) {
        if (blockId == -1)
            return null;
        if (blockId < 0 || blockId >= 4096)
            throw new IllegalArgumentException("INVALID BLOCKID (0-4095) "
                    + blockId + " SUPPLIED");
        return dataHandlers[blockId];

    }

    /**
     * Returns a list of extension objects for schematic, used internally, do
     * not touch.
     * 
     * @param tag
     *            raw NBT tag of a schematic
     * @param file
     *            {@link SchematicFile} passed to extensions
     * @return
     */
    public static List<SchematicExtension> getExtensions(NBTTagCompound tag,
            SchematicFile file) {
        List<SchematicExtension> l = new ArrayList<SchematicExtension>();

        for (String exTagFull : schematicExtensions.getTags()) {
            logger.debug("Checking for " + exTagFull);
            if (nbtContainsPath(tag, exTagFull)) {
                try {
                    Class<? extends SchematicExtension> c = schematicExtensions
                            .get(exTagFull);
                    logger.debug("Loading extension handler ["
                            + c.getAnnotation(SchExtension.class).name() + "]");
                    SchematicExtension ext = c.newInstance();
                    ext.onLoad(tag, file);
                    l.add(ext);
                } catch (InstantiationException e) {
                    logger.error("Could not load extension, instantiation error ["
                            + exTagFull + "]");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    logger.error("Could not load extension, access error ["
                            + exTagFull + "]");
                    e.printStackTrace();
                }
            }

        }

        return l;
    }

    /**
     * Look for tag based on dot seperated path
     * 
     * @param tag
     * @param exTagFull
     * @return
     */
    private static boolean nbtContainsPath(NBTTagCompound tag, String exTagFull) {
        NBTTagCompound base = tag;
        String[] parts = exTagFull.split(".");

        if (parts.length == 0)
            return base.hasKey(exTagFull);

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (base.hasKey(part)) {
                base = base.getCompoundTag(parts[i]);
            } else
                return false;
        }
        return base.hasKey(parts[parts.length - 1]);
    }
}
