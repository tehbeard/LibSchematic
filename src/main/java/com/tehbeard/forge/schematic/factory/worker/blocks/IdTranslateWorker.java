package com.tehbeard.forge.schematic.factory.worker.blocks;

import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.extensions.id.IItemStackConverter;
import com.tehbeard.forge.schematic.extensions.id.IdTranslateExtension;
import com.tehbeard.forge.schematic.factory.worker.AbstractSchematicWorker;

/**
 * uses the {@link IdTranslateExtension} to map block id's from a schematic to
 * those in this instance of a game.
 * 
 * @author James
 * 
 */
public class IdTranslateWorker extends AbstractSchematicWorker {

    public IdTranslateWorker() {

    }

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        IdTranslateExtension ext = original
                .getExtension(IdTranslateExtension.class);

        if (ext == null) {
            SchematicDataRegistry.logger().error("No extension found");
            return original;
        }

        //NOTE: We're making things
        original.prepareToLoad(ext);
        ext.redoCache();

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                for (int z = 0; z < original.getLength(); z++) {
                    int oldid = original.getBlockId(x, y, z);
                    int newid = ext.mapBlock(oldid);
                    SchematicDataRegistry.logger().info(
                            "" + oldid + " - " + newid);
                    original.setBlockId(x, y, z, newid);
                }
            }
        }
        
        //Translate contents of all Tile Entities, use vanilla ItemStack converter to get most cases.
        //Use specialised 
        for(NBTTagCompound tileEntity : original.getTileEntities()){
        	IItemStackConverter converter = IdTranslateExtension.VANILLA_CONVERTER;
        	if(IdTranslateExtension.converters.containsKey(tileEntity.getString("id"))){
        		converter = IdTranslateExtension.converters.get(tileEntity.getString("id"));
        	}
        	converter.convertTag(tileEntity, ext);
        }
        return original;
    }
}
