package com.tehbeard.forge.schematic.product;

import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

public class ValidateInWorld extends ActOnWorld {

    public ValidateInWorld(World world) {
        super(world);
    }

    @Override
    protected Object action(int x, int y, int z, int b_id, int b_meta,
            SchVector worldVector, SchematicFile file) {
        int w_id = world.getBlockId(worldVector.getX(), worldVector.getY(), worldVector.getZ());
        byte w_meta = (byte) world.getBlockMetadata(worldVector.getX(), worldVector.getY(), worldVector.getZ());

        if(b_id != w_id || b_meta != w_meta){
            return false;
        }
        return null;
    }

  
}
