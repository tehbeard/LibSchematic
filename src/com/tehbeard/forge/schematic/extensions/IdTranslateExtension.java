package com.tehbeard.forge.schematic.extensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

/**
 * The Id translation service is an extension for Schematics that stores a map of block/item id to psuedo-unique key<br/>
 * The key is generated via Forge's {@link GameData} class, and takes the form MODID::BLOCKNAME
 * This extension is very useful for mods that appear in multiple mod packs and contain configurable ids. It allows a mod author
 * to distribute one schematic whose id's will be converted at runtime to the correct ones for that particular installtion.  
 * @author James
 *
 */
@SchExtension(name="Id translation service",checkPath="IdTable")
public class IdTranslateExtension implements SchematicExtension {



    private Map<String,Integer> nbtMapping = new HashMap<String, Integer>();

    private static Map<String,Integer> localMapping = new HashMap<String, Integer>();

    private Map<Integer,Integer> numericMapping = new HashMap<Integer, Integer>();

    public static void initLocalMapping(){
        map(localMapping);
    }

    /**
     * Uses Forge's {@link GameData} class to create a mapping, useful when saving schematics to encode ids for cross pack compatibility
     */
    public void generateFromGameData(){
        map(nbtMapping);
    }

    private static void map(Map<String,Integer> map){
        NBTTagList list = new NBTTagList();
        GameData.writeItemData(list);
        Set<ItemData> data = GameData.buildWorldItemData(list);
        for(ItemData d : data){
            String key = d.getModId() + "::" + d.getItemType();
            map.put(key, d.getItemId());
        }
    }

    public int translateId(int id){
        return numericMapping.containsKey(id) ? numericMapping.get(id) : id;
    }



    public boolean containsKey(String key) {
        return nbtMapping.containsKey(key);
    }

    public Integer get(String key) {
        return nbtMapping.get(key);
    }

    public Integer put(String key, Integer id) {
        return nbtMapping.put(key, id);
    }

    @Override
    public void onLoad(NBTTagCompound tag, SchematicFile file) {
        NBTTagList map = tag.getTagList("IdTable");
        for(int i = 0;i< map.tagCount();i++){
            NBTTagCompound entry = (NBTTagCompound) map.tagAt(i);

            nbtMapping.put(
                    entry.getString("uid")
                    ,
                    entry.getInteger("id")
                    );
        }

        
        //Cache to a int int map for fast lookup
        for(Entry<String, Integer> entry : nbtMapping.entrySet()){
            String key = entry.getKey();
            int schemaId = entry.getValue();

            if(!localMapping.containsKey(key)){
                SchematicDataRegistry.logger().severe("Alert! Schematic loaded that contains " + key + " at id: " + schemaId + ", no block of that type found!");
                numericMapping.put(schemaId, -1);
            }
            else{
                int localValue = localMapping.get(key);
                //only cache differing values
                if(localValue != schemaId){
                    numericMapping.put(schemaId, localValue);
                }
            }

        }
    }

    @Override
    public void onSave(NBTTagCompound tag, SchematicFile file) {
        NBTTagList map = new NBTTagList("IdTable");
        for(Entry<String, Integer> entry : nbtMapping.entrySet()){
            NBTTagCompound c = new NBTTagCompound();
            c.setString("uid", entry.getKey());
            c.setInteger("id", entry.getValue());
            map.appendTag(c);
        }

    }

    @Override
    public SchematicExtension copy(SchematicFile file) {
        IdTranslateExtension id = new IdTranslateExtension();
        id.nbtMapping.putAll(nbtMapping);
        return id;
    }

}
