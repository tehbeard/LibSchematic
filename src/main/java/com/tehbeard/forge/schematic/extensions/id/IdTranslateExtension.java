package com.tehbeard.forge.schematic.extensions.id;

import java.util.*;
import java.util.Map.Entry;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.extensions.SchExtension;
import com.tehbeard.forge.schematic.extensions.SchematicExtension;

/**
 * Allows schematics to be used in multiple enviroments without issues due to differences
 * in ids
 * 
 * @author James
 * 
 */
@SchExtension(name = "Id translation service", checkPath = "IdTable")
public class IdTranslateExtension implements SchematicExtension {
	
	public static final Map<String,IItemStackConverter> converters = new HashMap<String, IItemStackConverter>();
	public static final IItemStackConverter VANILLA_CONVERTER = new IItemStackConverter() {
		
		@Override
		public void convertTag(NBTTagCompound tag,
				IdTranslateExtension extension) {
			
			//Convert this tag
			extension.convertItemStackTag(tag);
			//Recursively check and change all ItemStack tags
			Iterator it = tag.func_150296_c().iterator();
			while(it.hasNext()){
			    String _key = (String) it.next();
			    if(tag.func_150299_b(_key) == 10){
                    convertTag(tag.getCompoundTag(_key), extension);
                }
			    
			}
		}
	};
	

	/**
	 * Local runtime dictionary of stringId -> int ids
	 */
	private static Map<String,Integer> localBlockMap = new HashMap<String, Integer>();
	private static Map<String,Integer> localItemMap = new HashMap<String, Integer>();
	
	/**
	 * Schematic dictionary of stringId -> int ids
	 */
	private Map<String,Integer> schematicBlockMap = new HashMap<String, Integer>();
	private Map<String,Integer> schematicItemMap = new HashMap<String, Integer>();
	
	//Mapping schematic id to runtime id
	private int[] blockCache = new int[4096];
	private int[] itemCache = new int[32256];
	
	/**
	 * Clean array caches;
	 */
	private void cleanCache(){
		blockCache = new int[4096];
		itemCache = new int[32256];
		fillArray(blockCache, -1);
		fillArray(itemCache, -1);
	}
	
	private void fillArray(int[] a,int d){
		for(int i = 0; i<a.length;i++){
			a[i] = d;
		}
	}
	
	/**
	 * Map values from two dictionaries
	 * @param cache array cache to write to
	 * @param local local dictionary of stringId -> int id
	 * @param schematic schematic dictionary of stringId -> int id
	 */
	private void map(int[] cache, Map<String,Integer> local, Map<String,Integer> schematic){
		for( Entry<String, Integer> entry : local.entrySet()){
			int localId = entry.getValue();
			int schematicId = schematic.containsKey(entry.getKey()) ? schematic.get(entry.getKey()) : -1;
			if (schematicId==-1) continue;
			cache[schematicId] = localId;
		}
	}
	
	/**
	 * Rewrites the cache
	 */
	public void redoCache(){
		//Take our current runtime as the authorative source.
		cleanCache();
		map(blockCache, localBlockMap, schematicBlockMap);
		map( itemCache,  localItemMap, schematicItemMap);
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) that exists in this current runtime
	 * @param ident
	 * @param uid
	 */
	public static void addLocalBlock(String ident,int uid){
		localBlockMap.put(ident, uid);
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) to this schematic
	 * @param ident
	 * @param uid
	 */
	public void addSchematicBlock(String ident,int uid){
		schematicBlockMap.put(ident, uid);
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) that exists in this current runtime
	 * @param ident
	 * @param uid
	 */
	public static void addLocalItem(String ident,int uid){
		localItemMap.put(ident, uid);
	}
	
	/**
	 * Map a string to an id (minecraft:stone -> 1) to this schematic
	 * @param ident
	 * @param uid
	 */
	public void addSchematicItem(String ident,int uid){
		schematicItemMap.put(ident, uid);
	}

	private String unwind(Map<?,?> wound) {
		String s = "";

		for (Object key : wound.keySet()) {
			s += String.format("[%s : %s] ", key.toString(), wound.get(key).toString());
		}

		return s;
	}

	/**
	 * Map a block id from the schematic to the id in this world. 
	 * @param fromId
	 * @return
	 */
	public int mapBlock(int fromId){
		SchematicDataRegistry.logger().info(String.format(
				"%s", Arrays.toString(blockCache)
		));
		SchematicDataRegistry.logger().info(String.format(
				"%s", unwind(localBlockMap)
		));
		SchematicDataRegistry.logger().info(String.format(
				"%s", unwind(localItemMap)
		));
		return blockCache[fromId];
	}
	
	/**
	 * Map a block id from the schematic to the id in this world. 
	 * @param fromId
	 * @return
	 */
	public int mapItem(int fromId){
		//Check block cache, failing that check item cache.
		int newId = -1;
		if(fromId >= 0 && fromId < 4096){
			newId = mapBlock(fromId);
		}
		
		if(newId == -1){
			newId = itemCache[fromId];
		}
		return newId;
	}
	
	/**
	 * Copy the local environment map to the schematic (use when writing a schematic)
	 */
	public void useLocalMapping(){
		schematicBlockMap = new HashMap<String, Integer>(localBlockMap);
		schematicItemMap  = new HashMap<String, Integer>(localItemMap);
	}
	
	private NBTTagCompound toTag(Map<String,Integer> map){
		NBTTagCompound tag = new NBTTagCompound();
		for( Entry<String, Integer> e : map.entrySet()){
			tag.setInteger(e.getKey(), e.getValue());
		}
		return tag;
	}
	
	@SuppressWarnings("unchecked")
    private Map<String,Integer> fromTag(NBTTagCompound tag){
		HashMap<String, Integer> map = new HashMap<String,Integer>();

		for(String _id : (Set<String>)tag.func_150296_c()){ // getTags
			map.put(_id,tag.getInteger(_id));
		}
		return map;
	}
	
	@Override
	public void onLoad(NBTTagCompound tag, SchematicFile file) {
		NBTTagCompound table = tag.getCompoundTag("IdTable");
		localBlockMap = fromTag(table.getCompoundTag("blocks"));
		localItemMap = fromTag(table.getCompoundTag("items"));
		
		redoCache();
	}

	@Override
	public void onSave(NBTTagCompound tag, SchematicFile file) {
		//Make NBT table
		NBTTagCompound table = tag.getCompoundTag("IdTable");
		table.setTag("blocks",toTag(localBlockMap));
		table.setTag("items",toTag(localItemMap));
		//add to schematic table
		tag.setTag("IdTable", table);
	}

	@Override
	public SchematicExtension copy(SchematicFile file) {
		IdTranslateExtension translate = new IdTranslateExtension();
		translate.schematicBlockMap = new HashMap<String, Integer>(schematicBlockMap);
		translate.schematicItemMap = new HashMap<String, Integer>(schematicItemMap);
		return translate;
	}

    public void convertItemStackTag(NBTTagCompound tag){
    	//id
    	//Damage
    	//Count
    	
    	//Check this tag
    	if(tag.hasKey("id") &&
    	tag.hasKey("Damage") &&
    	tag.hasKey("Count")){
    		tag.setShort("id",(short)mapItem(tag.getShort("id")));
    	}
    	
    }

}


