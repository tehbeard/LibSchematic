package com.tehbeard.forge.schematic.extensions;

import java.util.HashSet;
import java.util.Set;

import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

/**
 * Adds support for tagging a schematic with arbitrary flags
 * @author James
 *
 */
@SchExtension(name="Layered schematics",checkPath="Metadata.Tags")
public class TagsExtension implements SchematicExtension{

    private Set<String> tags = new HashSet<String>();
    
    @Override
    public void onLoad(NBTTagCompound tag,SchematicFile file) {
        NBTTagList list = tag.getCompoundTag("Metadata").getTagList("Tags");
        for(int i = 0;i<list.tagCount();i++){
            tags.add(((NBTTagString)list.tagAt(i)).data);
        }
    }

    @Override
    public void onSave(NBTTagCompound tag,SchematicFile file) {
        NBTTagList list = new NBTTagList("Tags");
        for(String stag : tags){
            list.appendTag(new NBTTagString(null, stag));
        }
        if(!tag.hasKey("Metadata")){
            tag.setCompoundTag("Metadata", new NBTTagCompound());
        }
        tag.getCompoundTag("Metadata").setTag("Tags", list);
    }
    
    
    
    public boolean hasTag(String tag){
        return tags.contains(tag);
    }
    
    public void addTag(String tag){
        tags.add(tag);
    }
    
    public void removeTag(String tag){
        tags.remove(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SchematicExtension copy(SchematicFile file) {
        TagsExtension te = new TagsExtension();
        te.tags = (Set<String>) ((HashSet<String>)tags).clone();
        return te;
    }

}
