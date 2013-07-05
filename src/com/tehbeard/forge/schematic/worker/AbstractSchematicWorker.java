package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * A schematic worker can affect the process of a schematic paste
 * @author James
 *
 */
public abstract class AbstractSchematicWorker {
    
    public abstract SchematicFile modifySchematic(SchematicFile original);
}
