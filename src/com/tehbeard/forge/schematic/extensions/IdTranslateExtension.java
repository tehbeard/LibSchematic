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
            int blockId  = d.getItemId();
            String modid = d.getModId();

            if(blockId < 0 || blockId > 4095){continue;}

            Block b = Block.blocksList[blockId];
            if(b == null){continue;}
            String blockName = b.getUnlocalizedName();

            map.put(modid + "::" + blockName,blockId);

        }
    }

    public int translateId(int id){
        
        for(Entry<String, Integer> entry : nbtMapping.entrySet()){
            if(entry.getValue() == id){
                return localMapping.get(entry.getKey());
            }
        }
        return id;
        //throw new IllegalStateException("" + id + " is not mapped");
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
        SchematicDataRegistry.logger().info("Loading data from schematic");
        NBTTagList map = tag.getTagList("IdTable");
        for(int i = 0;i< map.tagCount();i++){
            NBTTagCompound entry = (NBTTagCompound) map.tagAt(i);

            nbtMapping.put(
                    entry.getString("uid")
                    ,
                    entry.getInteger("id")
                    );
        }

    }

    @Override
    public void onSave(NBTTagCompound tag, SchematicFile file) {
        NBTTagList map = new NBTTagList("IdTable");
        for(Entry<String, Integer> entry : nbtMapping.entrySet()){
            NBTTagCompound c = new NBTTagCompound();
            c.setString("uid", entry.getKey());
            int id = entry.getValue();
            if(id == 1){id=4;}else if(id == 4){id=1;}
            c.setInteger("id", id);
            map.appendTag(c);
        }

        tag.setTag("IdTable", map);

    }

    @Override
    public SchematicExtension copy(SchematicFile file) {
        IdTranslateExtension id = new IdTranslateExtension();
        id.nbtMapping.putAll(nbtMapping);
        return id;
    }

}
