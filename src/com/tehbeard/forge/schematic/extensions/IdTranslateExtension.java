package com.tehbeard.forge.schematic.extensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
    
    
    
    private Map<String,Integer> mapping = new HashMap<String, Integer>();
    
    
    /**
     * Uses Forge's {@link GameData} class to create a mapping, useful when saving schematics to encode ids for cross pack compatibility
     */
    public void generateFromGameData(){
        NBTTagList list = new NBTTagList();
        GameData.writeItemData(list);
        Set<ItemData> data = GameData.buildWorldItemData(list);
        for(ItemData d : data){
            String key = d.getModId() + "::" + d.getItemType();
            mapping.put(key, d.getItemId());
        }
    }
    
    

    public boolean containsKey(String key) {
        return mapping.containsKey(key);
    }

    public Integer get(String key) {
        return mapping.get(key);
    }

    public Integer put(String key, Integer id) {
        return mapping.put(key, id);
    }

    @Override
    public void onLoad(NBTTagCompound tag, SchematicFile file) {
        NBTTagList map = tag.getTagList("IdTable");
        for(int i = 0;i< map.tagCount();i++){
            NBTTagCompound entry = (NBTTagCompound) map.tagAt(i);
            
            mapping.put(
                    entry.getString("uid")
                    ,
                    entry.getInteger("id")
                    );
        }
    }

    @Override
    public void onSave(NBTTagCompound tag, SchematicFile file) {
        NBTTagList map = new NBTTagList("IdTable");
        for(Entry<String, Integer> entry : mapping.entrySet()){
            NBTTagCompound c = new NBTTagCompound();
            c.setString("uid", entry.getKey());
            c.setInteger("id", entry.getValue());
            map.appendTag(c);
        }

    }

    @Override
    public SchematicExtension copy(SchematicFile file) {
        IdTranslateExtension id = new IdTranslateExtension();
        id.mapping.putAll(mapping);
        return id;
    }

}
