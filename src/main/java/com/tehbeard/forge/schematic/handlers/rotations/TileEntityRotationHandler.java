package com.tehbeard.forge.schematic.handlers.rotations;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.SchematicRotationHandler;

/**
 * For blocks that are purely TE based in rotation
 * 
 * @author James
 * 
 */
public abstract class TileEntityRotationHandler implements
        SchematicRotationHandler {

    @Override
    public int rotateData(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, int rotations) {
        return metadata;
    }

    @Override
    public abstract void rotateTileEntity(SchematicFile schematic, int x,
            int y, int z, int blockId, int metadata, TileEntity tileEntity,
            int rotations);

    public ForgeDirection fdRotate(ForgeDirection dir, int rotations) {
        ForgeDirection d = dir;
        for (int i = 0; i < rotations; i++) {
            d = d.getRotation(ForgeDirection.UP);
        }
        return d;
    }
}
