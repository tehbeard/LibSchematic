package com.tehbeard.forge.schematic.extensions;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchematicFile;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

/**
 * Adds support reading FML ModItem data out of a schematic
 * FML ModItem data ties 
 * @author James
 *
 */
@SchExtension(name="FML ModItem data",checkPath="FMLModItemData")
public class ModItemExtension implements SchematicExtension {

    @Override
    public void onLoad(NBTTagCompound tag, SchematicFile file) {
        
        //Set<ItemData> data = GameData.buildWorldItemData(tag.getTagList("FMLModItemData"));
        //GameData.
    }

    @Override
    public void onSave(NBTTagCompound tag, SchematicFile file) {
        // TODO Auto-generated method stub
        
    }

}
