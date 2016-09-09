package com.tehbeard.forge.schematic.factory.worker.blocks;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.factory.worker.AbstractSchematicWorker;
import net.minecraft.block.Block;

import java.util.Map;

/**
 * Class Name - ReplaceBlockWorker
 * Package - com.tehbeard.forge.schematic.factory.worker.blocks
 * Desc of Class - A worker which, when given a map of blocks that
 *                 it needs to convert against blocks that they're
 *                 being converted to... Converts them.
 * Author(s) - M. D. Ball
 */
public class ReplaceBlockWorker extends AbstractSchematicWorker {

    /**
     * This is a map of records against records which is going to be used to
     * say which blocks (with associated metadata) are going to be converted
     * into which blocks (with more associated metadata)
     */
    private Map<BlockRecord, BlockRecord> blockReplacements;

    /**
     * Instantiate the class with the map of the blocks we're replacing
     *
     * @param blockReplacements A collection of records with one-to-one mapping
     *                          of which blocks are going to be converted into
     *                          which blocks
     */
    public ReplaceBlockWorker(Map<BlockRecord, BlockRecord> blockReplacements) {
        this.blockReplacements = blockReplacements;
    }

    /**
     * As per the interface, when this is called, go through the schematic and
     * replace blocks found in the blockReplacements map with their counterparts
     * (also found in the blockReplacements map)
     *
     * @param original This is the schematic that we're going to iterate through
     *                 and replace all the blocks that are present in the
     *                 blockReplacements map
     * @return The altered schematic with switched blocks
     */
    @Override
    public SchematicFile modifySchematic(SchematicFile original) {

        //For each block in the schematic...
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                for (int z = 0; z < original.getLength(); z++) {

                    //If the block is found in the replacements map
                    if (blockReplacements.containsKey(
                            new BlockRecord(
                                    original.getBlockId(x, y, z),
                                    original.getBlockData(x, y, z)
                            ))) {

                        //Then find out the replacement block
                        BlockRecord rec = blockReplacements.get(
                                new BlockRecord(
                                        original.getBlockId(x, y, z),
                                        original.getBlockData(x, y, z)
                                )
                        );

                        //And replace the block with its counterpart
                        original.setBlock(x, y, z, -1, ":");
                        original.setBlock(x, y, z, rec._id, Block.getBlockById(rec._id).getLocalizedName());
                        original.setBlockData(x, y, z, rec.metadata);

                    }

                }
            }
        }

        return original;

    }

}
