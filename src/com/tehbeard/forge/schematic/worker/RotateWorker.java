package com.tehbeard.forge.schematic.worker;

import net.minecraft.nbt.NBTTagCompound;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.data.SchematicRotationHandler;

/**
 * Rotates a schematic, handles rotating metadata using DataHandlers.
 * NOTE: Add to call stack after OffsetWorker to ensure correct offset vector is generated!
 * @author James
 *
 */
public class RotateWorker extends AbstractSchematicWorker{

    private int rotations;
    
    public RotateWorker(int rotations){
        this.rotations = rotations;
    }
    
    @Override
    public SchematicFile modifySchematic(SchematicFile original) {
        
        //Rotate the initial vector
        //original.getInitialVector().rotateVector(rotations);
        
        short width = (rotations % 2) == 0 ? original.getWidth() : original.getLength();
        short height = original.getHeight();
        short length = (rotations % 2) == 0 ? original.getLength() : original.getHeight();
        
        SchematicFile sch = new SchematicFile(width, height, length);
        
        for(int y = 0;y<original.getHeight();y++){
            for(int x = 0;x<original.getWidth();x++){
                for(int z = 0;z<original.getLength();z++){
                    SchVector v = new SchVector(x,y,z);
                    
                    //Get data from original
                    int b_id   = original.getBlockId(v);
                    byte b_meta = original.getBlockData(v);
                    NBTTagCompound te = original.getTileEntityTagAt(v);
                    
                    //rotate vector
                    v.rotateVector(rotations);
                    
                    //place block id
                    sch.setBlockId(v, b_id);
                    
                    //Grab handler and try to rotate data
                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];
                    if(handler instanceof SchematicRotationHandler){
                        int old_meta = b_meta;
                        b_meta = (byte) ((SchematicRotationHandler)handler).rotateData(original, x, y, z, b_id, b_meta, rotations);
                        SchematicDataRegistry.logger().config("Transformer found for " + b_id + " : " + old_meta + " -> " + b_meta);
                    }
                   
                    sch.setBlockData(v, b_meta);//set rotation value
                    
                    //Move TileEntity as needed
                    if(te != null){
                        te = (NBTTagCompound) te.copy();
                        te.setInteger("x", v.getX());
                        te.setInteger("y", v.getY());
                        te.setInteger("z", v.getZ());
                        sch.getTileEntities().add(te);
                    }
                    

                }
            }
        }
        
        
        
        return null;
    }
}
