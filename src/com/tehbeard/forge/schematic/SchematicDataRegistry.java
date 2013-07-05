package com.tehbeard.forge.schematic;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.data.RotationHandler;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.extensions.ClassCatalogue;
import com.tehbeard.forge.schematic.extensions.LayersExtension;
import com.tehbeard.forge.schematic.extensions.SchExtension;
import com.tehbeard.forge.schematic.extensions.SchematicExtension;
import com.tehbeard.forge.schematic.extensions.TagsExtension;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


/**
 * Main entry point for LibSchematic.
 * This class acts as the FML mod class, 
 * it handles holding data handlers for mod blocks 
 * and schematic extensions to access extra information 
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

    @PreInit
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        if(DEBUG_MODE){
            logger.setLevel(Level.FINEST);
        }

    }

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
        dataHandlers[Block.chest.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.chestTrapped.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.enderChest.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.furnaceIdle.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.furnaceBurning.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.pistonStickyBase.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.pistonBase.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.pistonExtension.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.pistonMoving.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.ladder.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.signWall.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.dispenser.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.hopperBlock.blockID] = RotationHandler.CONTAINER_PISTON;
        dataHandlers[Block.dropper.blockID] = RotationHandler.CONTAINER_PISTON;

        dataHandlers[Block.torchRedstoneIdle.blockID] = RotationHandler.WALL_MOUNTED;
        dataHandlers[Block.torchRedstoneActive.blockID] = RotationHandler.WALL_MOUNTED;
        dataHandlers[Block.stoneButton.blockID] = RotationHandler.WALL_MOUNTED;
        dataHandlers[Block.woodenButton.blockID] = RotationHandler.WALL_MOUNTED;
        dataHandlers[Block.lever.blockID] = RotationHandler.WALL_MOUNTED;
        dataHandlers[Block.torchWood.blockID] = RotationHandler.WALL_MOUNTED;


        dataHandlers[Block.redstoneComparatorActive.blockID] = RotationHandler.REPEATER;
        dataHandlers[Block.redstoneComparatorIdle.blockID] = RotationHandler.REPEATER;
        dataHandlers[Block.redstoneRepeaterActive.blockID] = RotationHandler.REPEATER;
        dataHandlers[Block.redstoneRepeaterIdle.blockID] = RotationHandler.REPEATER;

        dataHandlers[Block.rail.blockID] = RotationHandler.RAIL;
        dataHandlers[Block.railPowered.blockID] = RotationHandler.RAIL;
        dataHandlers[Block.railDetector.blockID] = RotationHandler.RAIL;
        dataHandlers[Block.railActivator.blockID] = RotationHandler.RAIL;
        
        dataHandlers[Block.stairsBrick.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsCobblestone.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsWoodOak.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsWoodSpruce.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsWoodBirch.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsWoodJungle.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsBrick.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsStoneBrick.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsSandStone.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsNetherQuartz.blockID] = RotationHandler.STAIRS;
        dataHandlers[Block.stairsNetherBrick.blockID] = RotationHandler.STAIRS;
        

        dataHandlers[Block.vine.blockID] = RotationHandler.VINES;

        dataHandlers[Block.trapdoor.blockID] = RotationHandler.TRAPDOOR;

        dataHandlers[Block.tripWireSource.blockID] = RotationHandler.HOOK;

        dataHandlers[Block.fenceGate.blockID] = RotationHandler.FENCEGATE;

        dataHandlers[Block.anvil.blockID] = RotationHandler.ANVIL;

        dataHandlers[Block.bed.blockID] = RotationHandler.BED;

        dataHandlers[Block.signPost.blockID] = RotationHandler.SIGN_POST;

        dataHandlers[Block.doorWood.blockID] = RotationHandler.DOOR;
        dataHandlers[Block.doorIron.blockID] = RotationHandler.DOOR;
        
        dataHandlers[Block.blockNetherQuartz.blockID] = RotationHandler.QUARTZ;
        
        dataHandlers[Block.wood.blockID] = RotationHandler.WOOD;
    }

    private static final ClassCatalogue<SchematicExtension> schematicExtensions = new ClassCatalogue<SchematicExtension>();

    /**
     * Initialise installed extensions
     */
    static{
        schematicExtensions.addProduct(WorldEditVectorExtension.class);
        schematicExtensions.addProduct(LayersExtension.class);
        schematicExtensions.addProduct(TagsExtension.class);
    }

    /**
     * Add a data handler for a block, to be used by mods.
     * Data handlers provide a way for mods to suggest how to handle a block in certain situations
     * such as rotation of a block, changing the owner.
     * @param blockId
     * @param handler
     */
    public static void setHandler(int blockId, SchematicDataHandler handler){
        if(blockId < 0 || blockId >= 4096){
            throw new IllegalArgumentException("INVALID BLOCKID (0-4095) " + blockId + " SUPPLIED");
        }
        dataHandlers[blockId] = handler;
        logger.info("Added schematic data handler for " + Block.blocksList[blockId].getUnlocalizedName() + "[" + blockId + "]");
    }

    /**
     * return data handler for block, it may implement one or several interfaces for 
     * @param blockId
     * @return
     */
    public static SchematicDataHandler getHandler(int blockId){
        if(blockId == -1){return null;}
        if(blockId < 0 || blockId >= 4096){
            throw new IllegalArgumentException("INVALID BLOCKID (0-4095) " + blockId + " SUPPLIED");
        }
        return dataHandlers[blockId];

    }

    /**
     * Returns a list of extension objects for schematic
     * @param tag
     * @param file
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
