package com.tehbeard.forge.schematic.extensions;

import java.util.Arrays;

import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Adds support for the layers extension
 * layers is an array in the same format as blocks, 
 * this array designates the layer of a particular xyz coordinate.
 * This can be used by a SchematicWorker to only paste blocks from a certain layer/layers.
 * An example use would be as a way to randomize a damaged building by changing which parts to paste into the world.
 * Another example would be a tiered building, as the player unlocks improvements new layers are pasted into the world.
 * 
 * This technique can be achieved with multiple schematics, layers simply makes it so only one schematic is needed.
 * @author James
 *
 */
@SchExtension(name="Layered schematics",checkPath="Layers")
public class LayersExtension implements SchematicExtension{

    //Layer data for "partial" imports 
    private byte[] layers;
    
    private SchematicFile file;

    @Override
    public void onLoad(NBTTagCompound tag,SchematicFile file) {
        layers = tag.getByteArray("Layers");
        this.file = file;
    }

    @Override
    public void onSave(NBTTagCompound tag,SchematicFile file) {
        tag.setByteArray("Layers", layers);

    }
    
    /**
     * Get the block layer at a coordinate (This is a feature I've been working on, think of it like layers in your image editing program of choice) 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public byte getLayer(int x,int y,int z){

        int index =  y * file.getWidth() * file.getLength() + z * file.getWidth() + x;
        if(index < 0 || index >= layers.length){
            return 0;
        }
        return layers[index];
    }
    
    /**
     * Set the block layer at a coordinate
     * @param x
     * @param y
     * @param z
     * @param layer
     */
    public void setLayer(int x,int y,int z,byte layer){

        int index =  y * file.getWidth() * file.getLength() + z * file.getWidth() + x;
        if(index < 0 || index >= layers.length){
            return;
        }
        layers[index] = layer;
    }

    @Override
    public SchematicExtension copy(SchematicFile file) {
        LayersExtension le = new LayersExtension();
        le.layers = Arrays.copyOf(layers, layers.length);
        le.file = file;
        return le;
    }

}
