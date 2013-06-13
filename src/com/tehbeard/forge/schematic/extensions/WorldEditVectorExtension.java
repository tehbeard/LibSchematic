package com.tehbeard.forge.schematic.extensions;

import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Adds access to WorldEdit's offset and origin vectors.
 * These vectors can be used by a SchematicWorker to place a schematic in a more intuitive place.
 * (e.g. Paste a house so that the front door is infront of the user).
 * @author James
 *
 */
@SchExtension(checkPath = "WEOriginX",name="WorldEdit Vector")
public class WorldEditVectorExtension implements SchematicExtension{

    //Original location in world
    private SchVector origin;

    //Offset vector
    private SchVector offset;
    
    @Override
    public void onLoad(NBTTagCompound tag,SchematicFile file) {
        origin = new SchVector(
                tag.getInteger("WEOriginX"),
                tag.getInteger("WEOriginY"),
                tag.getInteger("WEOriginZ"));

        offset = new SchVector(
                tag.getInteger("WEOffsetX"),
                tag.getInteger("WEOffsetY"),
                tag.getInteger("WEOffsetZ")
                );
    }

    @Override
    public void onSave(NBTTagCompound tag,SchematicFile file) {
        tag.setInteger("WEOriginX",origin.getX());
        tag.setInteger("WEOriginY",origin.getY());
        tag.setInteger("WEOriginZ",origin.getZ());

        tag.setInteger("WEOffsetX",offset.getX());
        tag.setInteger("WEOffsetY",offset.getY());
        tag.setInteger("WEOffsetZ",offset.getZ());
    }
    
    /**
     * Gets the schematics origin (WorldEdit generated schematics)
     * @return
     */
    public SchVector getOrigin() {
        return origin;
    }

    /**
     * Gets the schematics offset (WorldEdit generated schematics)
     * @return
     */
    public SchVector getOffset() {
        return offset;
    }

}
