package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicWorker;

/**
 * Offsets a schematic using a {@link SchVector}
 * @author James
 *
 */
public class VectorOffsetWorker extends AbstractSchematicWorker {
    
    SchVector vector;
    
    public VectorOffsetWorker(SchVector vector){
        this.vector = vector;
    }

    @Override
    public SchVector modifyOffsetVector(SchVector vector,SchVector schematicVector,
            SchematicWorker worker) {
       
        vector.add(vector);
        return vector;
    }
    
}
