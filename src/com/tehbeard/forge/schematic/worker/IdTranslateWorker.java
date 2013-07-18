package com.tehbeard.forge.schematic.worker;

import java.util.HashSet;
import java.util.Set;

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
        if(ext == null){return original;}

        for(int y = 0;y<original.getHeight();y++){
            for(int x = 0;x<original.getWidth();x++){
                for(int z = 0;z<original.getLength();z++){
                    original.setBlockId(x, y, z, ext.translateId(original.getBlockId(x, y, z)));
                }
            }
        }
        return original;
    }
}
