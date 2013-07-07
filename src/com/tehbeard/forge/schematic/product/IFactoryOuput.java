package com.tehbeard.forge.schematic.product;

import com.tehbeard.forge.schematic.SchematicFile;

public interface IFactoryOuput {
    
    /**
     * File to process
     * @param file schematic file to parse
     * @return result
     */
    public Object process(SchematicFile file);
}
