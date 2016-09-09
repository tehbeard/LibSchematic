package com.tehbeard.forge.schematic.factory.worker.blocks;

import java.util.HashSet;
import java.util.Set;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.factory.worker.AbstractSchematicWorker;

/**
 * Makes a schematicWorker not paste the specified blocks from the workers
 * constructor This can be used to make hollow structures that intersect the
 * ground more naturally, by having the schematic use a certain block (i.e.
 * sponge) as a marker for leave what the world has there.
 * 
 * @author James
 * 
 */
public class TransparencyWorker extends AbstractSchematicWorker {

    private Set<Integer> blockIds;

    public TransparencyWorker(int... blockIds) {
        this.blockIds = new HashSet<Integer>();
        for (int i : blockIds) {
            this.blockIds.add(i);
        }
    }

    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                for (int z = 0; z < original.getLength(); z++) {
                    if (blockIds.contains(original.getBlockId(x, y, z))) {
                        original.setBlock(x, y, z, -1, ":");
                        original.setBlock(x, y, z, 0, "minecraft:air");
                    }
                }
            }
        }
        return original;
    }
}
