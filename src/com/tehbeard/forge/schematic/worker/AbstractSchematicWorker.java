package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * A schematic worker can process a schematic
 * @author James
 *
 */
public abstract class AbstractSchematicWorker {
    
    public abstract SchematicFile modifySchematic(SchematicFile original);
}
