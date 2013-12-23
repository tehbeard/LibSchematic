package com.tehbeard.forge.schematic.handlers.rotations;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.SchematicRotationHandler;

public abstract class SimpleRotationHandler implements SchematicRotationHandler {

    @Override
    public abstract int rotateData(SchematicFile schematic, int x, int y,
            int z, int blockId, int metadata, int rotations);

    @Override
    public void rotateTileEntity(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, TileEntity tileEntity, int rotations) {
    }

    public ForgeDirection fdRotate(ForgeDirection dir, int rotations) {
        ForgeDirection d = dir;
        for (int i = 0; i < rotations; i++) {
            d = d.getRotation(ForgeDirection.UP);
        }
        return d;
    }
}
