package com.tehbeard.forge.schematic.data;

import net.minecraft.tileentity.TileEntity;

import com.tehbeard.forge.schematic.SchematicFile;

public abstract class SimpleRotationHandler implements SchematicRotationHandler {

    @Override
    public abstract int rotateData(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, int rotations) ;

    @Override
    public void rotateTileEntity(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, TileEntity tileEntity, int rotations) {}

}
