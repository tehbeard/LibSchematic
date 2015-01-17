package com.tehbeard.forge.schematic.factory.worker.blocks;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;

/**
 * Class Name - BlockRecord
 * Package - com.tehbeard.forge.schematic.factory.worker.blocks
 * Desc of Class - ...
 * Author(s) - M. D. Ball
 */
public final class BlockRecord {
    public final String namespace;
    public final int _id;
    public final byte metadata;

    public BlockRecord(String namespace, byte metadata) {
        this.namespace = namespace;
        this._id = Block.getIdFromBlock(Block.getBlockFromName(namespace));
        this.metadata = metadata;
    }

    public BlockRecord(int id, byte metadata) {
        this.namespace = GameData.getBlockRegistry().getNameForObject(Block.getBlockById(id));
        this._id = id;
        this.metadata = (byte) 0;
    }

    public BlockRecord(String namespace) {
        this(namespace, (byte) 0);
    }

    public BlockRecord(int id) {
        this(id, (byte) 0);
    }
}
