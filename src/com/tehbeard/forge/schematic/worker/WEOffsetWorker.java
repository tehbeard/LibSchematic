package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;

/**
 * Offsets a schematic using WorldEdit's offset vector
 * @author James
 *
 */
public class WEOffsetWorker extends AbstractSchematicWorker {

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        WorldEditVectorExtension ve = original.getExtension(WorldEditVectorExtension.class);
        if(ve!=null){
            original.getInitialVector().add(ve.getOffset());
        }
        return original;
    }
    
}
