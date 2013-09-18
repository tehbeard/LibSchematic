package com.tehbeard.forge.schematic.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Handles loading weird TileEntities.
 * @author James
 *
 */
public interface ITileEntityLoadHandler {
	
	public TileEntity generateTileEntity(NBTTagCompound tag);
	
}
