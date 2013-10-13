package com.tehbeard.forge.schematic.handlers.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Handles loading weird TileEntities.
 * 
 * @author James
 * 
 */
public interface ITileEntityLoadHandler {

    public TileEntity generateTileEntity(NBTTagCompound tag);

}
