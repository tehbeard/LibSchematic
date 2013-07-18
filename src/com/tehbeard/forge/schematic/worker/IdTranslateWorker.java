package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.extensions.IdTranslateExtension;

/**
 * uses the {@link IdTranslateExtension} to map block id's from a schematic to those in this instance of a game.
 * @author James
 *
 */
public class IdTranslateWorker extends AbstractSchematicWorker {

    public IdTranslateWorker(){

    }

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        IdTranslateExtension ext = original.getExtension(IdTranslateExtension.class);
        if(ext == null){SchematicDataRegistry.logger().severe("No extension found");return original;}

        for(int y = 0;y<original.getHeight();y++){
            for(int x = 0;x<original.getWidth();x++){
                for(int z = 0;z<original.getLength();z++){
                    int oldid = original.getBlockId(x, y, z);
                    int newid = ext.translateId(oldid);
                    SchematicDataRegistry.logger().info("" + oldid + " - " + newid);
                    original.setBlockId(x, y, z, newid);
                }
            }
        }
        return original;
    }
}
