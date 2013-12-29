package com.tehbeard.forge.schematic.compat.te;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.tileentity.IReconfigurableFacing;

import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.rotations.*;

public class COFHTileRotator extends TileEntityRotationHandler{

	/**
	 * Rotate using the {@link IReconfigurableFacing} interface, this module should work
	 * for all cofh based blocks
	 */
	@Override
	public void rotateTileEntity(SchematicFile schematic, int x,
			int y, int z, int blockId, int metadata,
			TileEntity tileEntity, int rotations) {
		IReconfigurableFacing tile = (IReconfigurableFacing) tileEntity;
		tile.setFacing(
		fdRotate(
				ForgeDirection.getOrientation(tile.getFacing()), 
				rotations
				).ordinal()
		);
		
		
	};

	
}
