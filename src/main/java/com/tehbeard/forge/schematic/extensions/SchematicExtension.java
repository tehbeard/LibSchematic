package com.tehbeard.forge.schematic.extensions;

import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Represents an extension to the Schematic file format extensions add
 * additional data on top of the standard schematic format.
 * 
 * @author James
 * 
 */
public interface SchematicExtension {

    /**
     * Called after loading the base data
     * 
     * @param tag
     */
    public void onLoad(NBTTagCompound tag, SchematicFile file);

    /**
     * @param tag
     */
    public void onSave(NBTTagCompound tag, SchematicFile file);

    /**
     * Creates a copy of this {@link SchematicExtension}, using the supplied
     * {@link SchematicFile}
     * SchematicExtension <b>SHOULD NOT</b> add itself to the SchematicFile, this
     * is done elsewhere.
     * @param file
     * @return
     */
    public SchematicExtension copy(SchematicFile file);
}
