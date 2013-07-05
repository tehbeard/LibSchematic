package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

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
    public SchematicFile modifySchematic(SchematicFile original) {
        original.getInitialVector().add(vector);
        return original;
    }
    
}
