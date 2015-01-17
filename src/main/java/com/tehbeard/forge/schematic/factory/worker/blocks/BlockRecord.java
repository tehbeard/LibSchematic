package com.tehbeard.forge.schematic.factory.worker.blocks;

/**
 * Class Name - BlockRecord
 * Package - com.tehbeard.forge.schematic.factory.worker.blocks
 * Desc of Class - ...
 * Author(s) - M. D. Ball
 */
public final class BlockRecord {
    public final String namespace;
    public final byte metadata;

    public BlockRecord(String namespace, byte metadata) {
        this.namespace = namespace;
        this.metadata = metadata;
    }
}
