package com.tehbeard.forge.schematic.extensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Allows schematics to be used in multiple enviroments without issues due to differences
 * in ids
 * 
 * @author James
 * 
 */
@SchExtension(name = "Id translation service", checkPath = "IdTable")
public class IdTranslateExtension implements SchematicExtension {

	private static Map<String,Integer> localEnv = new HashMap<String, Integer>();
	
	private Map<String,Integer> schematicEnv = new HashMap<String, Integer>();
	
	//Mapping schematic id to runtime id
	private int[] blockCache = new int[4096];
	private int[] itemCache = new int[32000];
	
	private void cleanCache(){
		int[] blockCache = new int[4096];
		int[] itemCache = new int[32000];
	}
	
	private void redoCache(){
		//Take our current runtime as the authorative source.
		for( Entry<String, Integer> entry : localEnv.entrySet()){
			
			int localId = entry.getValue();
			int schematicId = schematicEnv.containsKey(entry.getKey()) ? schematicEnv.get(entry.getKey()) : -1;
			
		}
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) that exists in this current runtime
	 * @param ident
	 * @param uid
	 */
	public void addLocalMapping(String ident,int uid){
		localEnv.put(ident, uid);
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) to this schematic
	 * @param ident
	 * @param uid
	 */
	public void addSchematicMapping(String ident,int uid){
		schematicEnv.put(ident, uid);
	}
	
	
	@Override
	public void onLoad(NBTTagCompound tag, SchematicFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSave(NBTTagCompound tag, SchematicFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SchematicExtension copy(SchematicFile file) {
		// TODO Auto-generated method stub
		return new IdTranslateExtension();
	}

    

}
