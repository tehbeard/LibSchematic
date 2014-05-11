package com.tehbeard.forge.schematic.compat;

import java.util.regex.Pattern;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.handlers.SchematicDataHandler;
import com.tehbeard.forge.schematic.handlers.rotations.VanillaRotations;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
@Mod(modid = "libschematic.compat.vanilla", name = "LibSchematic::Vanilla", version = "1.0", useMetadata = true)

public class VanillaCompat {
    
    private void shRegex(String regex,SchematicDataHandler handler){
        
        for(Object _key : GameData.getBlockRegistry().getKeys()){
            String key = _key.toString();
            if(Pattern.matches(regex, key)){
                SchematicDataRegistry.logger().debug("Regex {0} matched {1} ",regex,key);
                sh(key,handler);
            }
        }
    }
    
    private void sh(String blockNamespace, SchematicDataHandler handler){
        SchematicDataRegistry.setHandler(blockNamespace, handler);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
   
        //Chest related rotations
        sh("chest",VanillaRotations.CONTAINER_PISTON);
        sh("trapped_chest",VanillaRotations.CONTAINER_PISTON);
        sh("ender_chest",VanillaRotations.CONTAINER_PISTON);
        sh("furnace",VanillaRotations.CONTAINER_PISTON);
        sh("lit_furnace",VanillaRotations.CONTAINER_PISTON);
        sh("sticky_piston",VanillaRotations.CONTAINER_PISTON);
        sh("piston",VanillaRotations.CONTAINER_PISTON);
        sh("piston_head",VanillaRotations.CONTAINER_PISTON);
        sh("piston_extension",VanillaRotations.CONTAINER_PISTON);
        sh("ladder",VanillaRotations.CONTAINER_PISTON);
        sh("wall_sign",VanillaRotations.CONTAINER_PISTON);
        sh("dispenser",VanillaRotations.CONTAINER_PISTON);
        sh("hopper",VanillaRotations.CONTAINER_PISTON);
        sh("dropper",VanillaRotations.CONTAINER_PISTON);
        
        //hanging item rotations
        sh("unlit_redstone_torch",VanillaRotations.WALL_MOUNTED);
        sh("redstone_torch",VanillaRotations.WALL_MOUNTED);
        sh("stone_button",VanillaRotations.WALL_MOUNTED);
        sh("wooden_button",VanillaRotations.WALL_MOUNTED);
        sh("lever",VanillaRotations.WALL_MOUNTED);
        sh("torch",VanillaRotations.WALL_MOUNTED);
        
        //Repeaters
        sh("powered_repeater",VanillaRotations.REPEATER);
        sh("unpowered_repeater",VanillaRotations.REPEATER);
        sh("powered_comparator",VanillaRotations.REPEATER);
        sh("unpowered_comparator",VanillaRotations.REPEATER);
        
        //rail
        shRegex("minecraft\\:.*rail", VanillaRotations.RAIL);
        
        //Stairs
        shRegex("minecraft\\:.*_stairs",VanillaRotations.STAIRS);
        

        sh("vine",VanillaRotations.VINES);

        sh("trapdoor",VanillaRotations.TRAPDOOR);

        sh("tripwire_hook",VanillaRotations.HOOK);

        sh("fence_gate", VanillaRotations.FENCEGATE);

        sh("anvil", VanillaRotations.ANVIL);

        sh("bed", VanillaRotations.BED);

        sh("standing_sign", VanillaRotations.SIGN_POST);

        shRegex("minecraft\\:.*_door", VanillaRotations.DOOR);

        sh("quartz_block", VanillaRotations.QUARTZ);

        sh("log", VanillaRotations.WOOD);
        sh("log2", VanillaRotations.WOOD);
    }


}
