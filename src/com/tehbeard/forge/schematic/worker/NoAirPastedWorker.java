package com.tehbeard.forge.schematic.worker;

import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchematicWorker;

/**
 * Makes a schematicWorker not paste if the schematic block is an air block
 * @author James
 *
 */
public class NoAirPastedWorker extends ISchematicWorker {

    @Override
    public boolean canPaste(World world, int x, int y, int z, int b_id,
            int b_meta, SchematicWorker worker) {
        return b_id != 0;
    }
}
