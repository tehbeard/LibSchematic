package com.tehbeard.forge.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.worker.AbstractSchematicWorker;

/**
 * {@link SchematicFactory} Processes a {@link SchematicFile} using one or more {@link AbstractSchematicWorker} workers
 * The factory will initially clone the SchematicFile, after which this clone may be modified or even replaced with another {@link SchematicFile}
 * 
 * The lifecycle of a {@link SchematicFactory} is as follows:
 * new() -> loadWorkers()... -> loadSchematic() -> paste()...
 * @author James
 *
 */
public class SchematicFactory {

    private SchematicFile processedSchematic;
    
    private List<AbstractSchematicWorker> workers = new ArrayList<AbstractSchematicWorker>();

    /**
     * set the initial schematic file
     * @param file
     * @return
     */
    public SchematicFactory loadSchematic(SchematicFile file){
        this.processedSchematic = process(file);
        return this;

    }

    private SchematicFile process(SchematicFile file) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Add workers to a schematic
     * @param workers
     * @return
     */
    public SchematicFactory loadWorkers(AbstractSchematicWorker... workers){
        for(AbstractSchematicWorker worker : workers){
            this.workers.add(worker);
        }
        return this;
    }

    public SchematicFile getSchematic(){
        return processedSchematic;
    }

    /**
     * Paste schematic into a world
     * @param world world object to paste into
     */
    public void paste(World world){


        for(int y = 0;y<processedSchematic.getHeight();y++){
            for(int x = 0;x<processedSchematic.getWidth();x++){
                for(int z = 0;z<processedSchematic.getLength();z++){
                    
                    SchVector v = new SchVector(x,y,z);

                    int b_id = processedSchematic.getBlockId(v);
                    byte b_meta = processedSchematic.getBlockData(v);
                    
                    if(b_id == -1){continue;}

                    if(Block.blocksList[b_id] != null || b_id == 0){
                        world.setBlock(v.getX(), v.getY(), v.getZ(), b_id, b_meta,0);
                    }
                    else
                    {
                        if(b_id >0){
                            SchematicDataRegistry.logger().severe("UNKNOWN BLOCK [" + v.getX() +", " + v.getY() +", " + v.getZ() +"] "  + b_id + ":" + b_meta);
                        }
                    }


                    TileEntity te = processedSchematic.getTileEntityAt(x,y,z);
                    if(te!=null){
                        SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                        world.setBlockTileEntity(v.getX(), v.getY(), v.getZ(),te);
                        world.setBlockMetadataWithNotify(v.getX(), v.getY(),v.getZ(), b_meta,2);//Fix container blocks being derpy
                    }
                }   
            }   
        }

        //TODO: ENTITY SPAWNING
    }

    

    /**
     * Validates a world area against a schematic, returns true if world area matches schematic
     * @param world world object to validate
   */
    public boolean validateInWorld(World world){

        throw new UnsupportedOperationException("NOT IMPLEMENTED YET");

        /*
        if(b_id != maskId &&
                               (world.getBlockId(v.getX(), v.getY(), v.getZ()) !=  b_id ||
                               world.getBlockMetadata(v.getX(), v.getY(), v.getZ()) != b_meta)
                               ){
                           return false;
                       }
        */
    }

}
