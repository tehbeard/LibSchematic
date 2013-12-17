package com.tehbeard.forge.schematic.factory.worker;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Offsets a schematic using a {@link SchVector}
 * 
 * @author James
 * 
 */
public class VectorOffsetWorker extends AbstractSchematicWorker {

    SchVector vector;
    boolean relative;

    public VectorOffsetWorker(SchVector vector,boolean relative) {
        this.vector = vector;
        this.relative = relative;
    }

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        if(relative){
          original.getInitialVector().add(vector);
        }
        else{
            original.getInitialVector().setX(vector.getX());
            original.getInitialVector().setY(vector.getY());
            original.getInitialVector().setZ(vector.getZ());
        }
        return original;
    }

}
