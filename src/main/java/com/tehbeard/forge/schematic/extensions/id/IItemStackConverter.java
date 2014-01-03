package com.tehbeard.forge.schematic.extensions.id;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Manipulates a NBTTagCompound to change the ItemStack id's used by it.
 * Note: This class is called on a TileEntity by the IdTranslateWorker
 * @author James
 *
 */
public interface IItemStackConverter {

	public void convertTag(NBTTagCompound tag,IdTranslateExtension extension);
}
