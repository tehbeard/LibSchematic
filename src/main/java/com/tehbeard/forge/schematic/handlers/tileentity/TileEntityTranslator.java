package com.tehbeard.forge.schematic.handlers.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Class to handle translating a TileEntity to/from .schematic for certain edge
 * cases. Operations may include converting certain values to relative
 * coordinates, or unpacking weird NBT data (looking at you Forge MultiPart <_<
 * ) Most TileEntity's do not need a specific Translator.
 * 
 * @author James
 * 
 */
public class TileEntityTranslator {

    /**
     * Serializes a TileEntity Standard operation is to call
     * TileEntity.writeToNBT(..)
     * 
     * @param te
     * @return
     */
    public NBTTagCompound pack(World world,TileEntity te) {
        NBTTagCompound tec = new NBTTagCompound();
        te.writeToNBT(tec);
        return tec;
    }

    /**
     * Unpacks a TileEntity ready for placement Standard operation is a call to
     * TileEntity.createAndLoadEntity(..)
     * 
     * @param te
     * @param x in world coords for use in unpacking, do not use to place the TileEntity.
     * @param y
     * @param z
     * @return
     */
    public TileEntity unpack(NBTTagCompound te,World world, int x, int y, int z) {
        return TileEntity.createAndLoadEntity(te);
    }

}
