package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicWorker;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;

/**
 * Offsets a schematic using WorldEdit's offset vector
 * @author James
 *
 */
public class WEOriginWorker extends AbstractSchematicWorker {

    @Override
    public SchVector modifyOffsetVector(SchVector vector,SchVector schematicVector,
            SchematicWorker worker) {
        WorldEditVectorExtension ve = worker.getSchematic().getExtension(WorldEditVectorExtension.class);
        if(ve==null){return vector;}
        
        vector.add(ve.getOffset());
        return vector;
    }
    
}
