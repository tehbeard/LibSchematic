package com.tehbeard.forge.schematic.product;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.data.SchematicRotationHandler;

public class PasteToWorld implements IFactoryOuput {
    
    private World world;
    
    private int rotations;

    private SchVector worldVec;
    
    public SchVector getWorldVec() {
        return worldVec;
    }

    public void setWorldVec(SchVector worldVec) {
        this.worldVec = worldVec;
    }

    public PasteToWorld(World world){
        this.world = world;
    }

    @Override
    public Object process(SchematicFile file) {
        if(file == null){throw new IllegalStateException("No schematic was passed! Aborting paste!");}

        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){
                    
                    SchVector worldSchematicVector = new SchVector(x,y,z);
                    //worldSchematicVector.add();
                    worldSchematicVector.rotateVector(rotations);
                    
                    SchVector worldVector = new SchVector(worldVec);
                    worldVector.add(worldSchematicVector);
                    worldVector.add(file.getInitialVector());
                    
                    int b_id = file.getBlockId(x,y,z);
                    byte b_meta = file.getBlockData(x,y,z);
                    
                    if(b_id == -1){continue;}
                    
                  //Grab handler and try to rotate data
                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];
                    if(handler instanceof SchematicRotationHandler){
                        int old_meta = b_meta;
                        b_meta = (byte) ((SchematicRotationHandler)handler).rotateData(file, x, y, z, b_id, b_meta, rotations);
                        SchematicDataRegistry.logger().config("Transformer found for " + b_id + " : " + old_meta + " -> " + b_meta);
                    }

                    if(Block.blocksList[b_id] != null || b_id == 0){
                        world.setBlock(worldVector.getX(), worldVector.getY(), worldVector.getZ(), b_id, b_meta,2);
                    }
                    else
                    {
                        if(b_id >0){
                            SchematicDataRegistry.logger().severe("UNKNOWN BLOCK [" + x +", " + y +", " + z +"] "  + b_id + ":" + b_meta);
                        }
                    }


                    TileEntity te = file.getTileEntityAt(x,y,z);
                    if(te!=null){
                        SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                        world.setBlockTileEntity(worldVector.getX(), worldVector.getY(), worldVector.getZ(),te);
                        world.setBlockMetadataWithNotify(worldVector.getX(), worldVector.getY(),worldVector.getZ(), b_meta,2);//Fix container blocks being derpy
                    }
                }   
            }   
        }
        return null;
        
    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

}
