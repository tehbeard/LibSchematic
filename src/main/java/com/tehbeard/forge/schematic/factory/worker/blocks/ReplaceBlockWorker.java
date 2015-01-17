package com.tehbeard.forge.schematic.factory.worker.blocks;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.factory.worker.AbstractSchematicWorker;

import java.util.Map;

/**
 * Class Name - ReplaceBlockWorker
 * Package - com.tehbeard.forge.schematic.factory.worker.blocks
 * Desc of Class - ...
 * Author(s) - M. D. Ball
 */
public class ReplaceBlockWorker extends AbstractSchematicWorker {

    Map<BlockRecord, BlockRecord> blockReplacements;

    public ReplaceBlockWorker(Map<BlockRecord, BlockRecord> blockReplacements) {
        this.blockReplacements = blockReplacements;
    }

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                for (int z = 0; z < original.getLength(); z++) {
                    if (blockReplacements.containsKey(
                            new BlockRecord(
                                    original.getBlockId(x, y, z),
                                    original.getBlockData(x, y, z)
                            ))) {
                        BlockRecord rec = blockReplacements.get(
                                new BlockRecord(
                                        original.getBlockId(x, y, z),
                                        original.getBlockData(x, y, z)
                                )
                        );
                        original.setBlockId(x, y, z, -1);
                        original.setBlockId(x, y, z, rec._id);
                        original.setBlockData(x, y, z, rec.metadata);
                    }
                }
            }
        }
        return original;
    }

}
