package com.tehbeard.forge.schematic;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.data.VanillaRotations;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.extensions.ClassCatalogue;
import com.tehbeard.forge.schematic.extensions.IdTranslateExtension;
import com.tehbeard.forge.schematic.extensions.LayersExtension;
import com.tehbeard.forge.schematic.extensions.SchExtension;
import com.tehbeard.forge.schematic.extensions.SchematicExtension;
import com.tehbeard.forge.schematic.extensions.TagsExtension;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


/**
 * Main entry point for LibSchematic.<br/>
 * This class acts as the FML mod class,<br/> 
 * it handles holding data handlers for mod blocks<br/> 
 * and schematic extensions to access extra information <br/>
 * embedded in a schematic
 * @author James
 *
 */
@Mod(modid = "tehbeard.schematic",name="LibSchematic",version="1.00")
@NetworkMod(clientSideRequired=false)
public class SchematicDataRegistry {

    //BEGIN FORGE MOD SECTION
    public static final boolean DEBUG_MODE = false;

    private static Logger logger;
    

    //initialise our shit
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        if(DEBUG_MODE){
            logger.setLevel(Level.FINEST);
        }

    }
    
    //Register our shit
    @EventHandler
    public void init(FMLInitializationEvent event){
        
    }
    
    //ok, check any blocks for SchematicDataHandler
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        logger.info("Polling block array for Data handlers");
        
        for(Block b : Block.blocksList){
            if(b instanceof SchematicDataHandler){
                if(getHandler(b.blockID) == null){
                    setHandler(b.blockID, (SchematicDataHandler) b);
                }
                else
                {
                    logger.warning("Alert! SchematicDataHandler already registered for block " + b.blockID);
                }
            }
        }
        
        IdTranslateExtension.initLocalMapping();
    }
    
    
    @EventHandler
    public void IMC(IMCEvent event){
        
        for(IMCMessage msg : event.getMessages()){
            if(msg.isNBTMessage()){
                //TODO - IMC interface
            }
            else
            {
                logger.warning("Invalid IMC message from " + msg.getSender());
            }
        }
    }

    /**
     * Returns the logger for this mod
     * @return
     */
    public static Logger logger(){
        return logger;
    }

    //END FORGE MOD SECTION


    //data handlers for blocks
    public static final SchematicDataHandler[] dataHandlers = new SchematicDataHandler[4096];

    /**
     * Initialise vanilla data handlers
     */
    static{
        dataHandlers[Block.chest.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.chestTrapped.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.enderChest.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.furnaceIdle.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.furnaceBurning.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.pistonStickyBase.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.pistonBase.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.pistonExtension.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.pistonMoving.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.ladder.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.signWall.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.dispenser.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.hopperBlock.blockID] = VanillaRotations.CONTAINER_PISTON;
        dataHandlers[Block.dropper.blockID] = VanillaRotations.CONTAINER_PISTON;

        dataHandlers[Block.torchRedstoneIdle.blockID] = VanillaRotations.WALL_MOUNTED;
        dataHandlers[Block.torchRedstoneActive.blockID] = VanillaRotations.WALL_MOUNTED;
        dataHandlers[Block.stoneButton.blockID] = VanillaRotations.WALL_MOUNTED;
        dataHandlers[Block.woodenButton.blockID] = VanillaRotations.WALL_MOUNTED;
        dataHandlers[Block.lever.blockID] = VanillaRotations.WALL_MOUNTED;
        dataHandlers[Block.torchWood.blockID] = VanillaRotations.WALL_MOUNTED;


        dataHandlers[Block.redstoneComparatorActive.blockID] = VanillaRotations.REPEATER;
        dataHandlers[Block.redstoneComparatorIdle.blockID] = VanillaRotations.REPEATER;
        dataHandlers[Block.redstoneRepeaterActive.blockID] = VanillaRotations.REPEATER;
        dataHandlers[Block.redstoneRepeaterIdle.blockID] = VanillaRotations.REPEATER;

        dataHandlers[Block.rail.blockID] = VanillaRotations.RAIL;
        dataHandlers[Block.railPowered.blockID] = VanillaRotations.RAIL;
        dataHandlers[Block.railDetector.blockID] = VanillaRotations.RAIL;
        dataHandlers[Block.railActivator.blockID] = VanillaRotations.RAIL;
        
        dataHandlers[Block.stairsBrick.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsCobblestone.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsWoodOak.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsWoodSpruce.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsWoodBirch.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsWoodJungle.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsBrick.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsStoneBrick.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsSandStone.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsNetherQuartz.blockID] = VanillaRotations.STAIRS;
        dataHandlers[Block.stairsNetherBrick.blockID] = VanillaRotations.STAIRS;
        

        dataHandlers[Block.vine.blockID] = VanillaRotations.VINES;

        dataHandlers[Block.trapdoor.blockID] = VanillaRotations.TRAPDOOR;

        dataHandlers[Block.tripWireSource.blockID] = VanillaRotations.HOOK;

        dataHandlers[Block.fenceGate.blockID] = VanillaRotations.FENCEGATE;

        dataHandlers[Block.anvil.blockID] = VanillaRotations.ANVIL;

        dataHandlers[Block.bed.blockID] = VanillaRotations.BED;

        dataHandlers[Block.signPost.blockID] = VanillaRotations.SIGN_POST;

        dataHandlers[Block.doorWood.blockID] = VanillaRotations.DOOR;
        dataHandlers[Block.doorIron.blockID] = VanillaRotations.DOOR;
        
        dataHandlers[Block.blockNetherQuartz.blockID] = VanillaRotations.QUARTZ;
        
        dataHandlers[Block.wood.blockID] = VanillaRotations.WOOD;
    }

    private static final ClassCatalogue<SchematicExtension> schematicExtensions = new ClassCatalogue<SchematicExtension>();
    
    /**
     * Adds an extension for usage.
     * @param _class
     */
    public void addSchematicExtension(Class<? extends SchematicExtension> _class){
        schematicExtensions.addProduct(_class);
    }

    /**
     * Initialise installed extensions
     */
    static{
        schematicExtensions.addProduct(WorldEditVectorExtension.class);
        schematicExtensions.addProduct(LayersExtension.class);
        schematicExtensions.addProduct(TagsExtension.class);
        schematicExtensions.addProduct(IdTranslateExtension.class);
    }

    /**
     * Add a data handler for a block, to be used by other forge mods to add compatibility.<br/>
     * A data handler is a class that implements one to several interfaces, such as those in <code>com.tehbeard.schematic.data</code><br/>
     * These interfaces provide a way for other forge mods to instruct LibSchematic on how to process blocks/tile entities, such as rotation
     * or owner of the block.
     * @param blockId id of the block you wish to configure
     * @param handler object that implements one of several {@link SchematicDataHandler} interfaces for manipulating a block
     * @throws IllegalArgumentException on invalid block id
     */
    public static void setHandler(int blockId, SchematicDataHandler handler){
        if(blockId < 0 || blockId >= 4096){
            throw new IllegalArgumentException("INVALID BLOCKID (0-4095) " + blockId + " SUPPLIED");
        }
        dataHandlers[blockId] = handler;
        logger.info("Added schematic data handler for " + Block.blocksList[blockId].getUnlocalizedName() + "[" + blockId + "]");
    }

    /**
     * return data handler for block, it may implement one or several interfaces related to manipulation of a block/tile entity
     * @param blockId block if to get {@link SchematicDataHandler} for
     * @return {@link SchematicDataHandler} or null if no block found, 
     * @throws IllegalArgumentException on invalid block id
     */
    public static SchematicDataHandler getHandler(int blockId){
        if(blockId == -1){return null;}
        if(blockId < 0 || blockId >= 4096){
            throw new IllegalArgumentException("INVALID BLOCKID (0-4095) " + blockId + " SUPPLIED");
        }
        return dataHandlers[blockId];

    }

    /**
     * Returns a list of extension objects for schematic, used internally, do not touch.
     * @param tag raw NBT tag of a schematic
     * @param file {@link SchematicFile} passed to extensions
     * @return
     */
    public static List<SchematicExtension> getExtensions(NBTTagCompound tag,SchematicFile file){
        List<SchematicExtension> l = new ArrayList<SchematicExtension>();

        for(String exTagFull : schematicExtensions.getTags()){
            logger.fine("Checking for " + exTagFull);
            if(nbtContainsPath(tag,exTagFull)){
                try {
                    Class<? extends SchematicExtension> c = schematicExtensions.get(exTagFull);
                    logger.fine("Loading extension handler [" + c.getAnnotation(SchExtension.class).name() + "]");
                    SchematicExtension ext = c.newInstance();
                    ext.onLoad(tag, file);
                    l.add(ext);
                } catch (InstantiationException e) {
                    logger.severe("Could not load extension, instantiation error [" + exTagFull + "]");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    logger.severe("Could not load extension, access error [" + exTagFull + "]");
                    e.printStackTrace();
                }
            }

        }

        return l;
    }

    /**
     * Look for tag based on dot seperated path
     * @param tag
     * @param exTagFull
     * @return
     */
    private static boolean nbtContainsPath(NBTTagCompound tag,String exTagFull){
        NBTTagCompound base = tag;
        String[] parts = exTagFull.split(".");

        if(parts.length == 0){
            return base.hasKey(exTagFull);
        }

        for(int i = 0; i<parts.length-1;i++){
            String part = parts[i];
            if(base.hasKey(part)){
                base = base.getCompoundTag(parts[i]);
            }
            else
            {
                return false; 
            }
        }
        return base.hasKey(parts[parts.length-1]);
    }
}
