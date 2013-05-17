package com.tehbeard.forge.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.data.SchematicRotationHandler;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;
import com.tehbeard.forge.schematic.worker.ISchematicWorker;

/**
 * A schematic worker processes schematics and worlds, with hooks to handle runtime transformations
 * @author James
 *
 */
public class SchematicWorker {

    private SchematicFile file;

    private List<ISchematicWorker> workers = new ArrayList<ISchematicWorker>();

    public SchematicWorker loadSchematic(SchematicFile file){
        this.file = file;
        return this;

    }

    public SchematicWorker loadWorkers(ISchematicWorker... workers){
        for(ISchematicWorker worker : workers){
            this.workers.add(worker);
        }
        return this;
    }

    public SchematicFile getSchematic(){
        return file;
    }

    /**
     * Paste schematic into a world
     * @param world world object to paste into
     * @param initVector Location to paste at, If the schematic contains an offset this is where the offset will be in world.
     */
    public void paste(World world,SchVector initVector){

        SchVector initialVector = new SchVector();
        initialVector.add(initVector);


        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){

                    //Construct the vector for this location, taking into account rotation
                    SchVector v = new SchVector();
                    v.add(initialVector);
                    SchVector offset = new SchVector(x,y,z);

                    //offset.add(file.getExtension(WorldEditVectorExtension.class).getOffset());
                    //offset.rotateVector(rotations);
                    for(ISchematicWorker worker : workers){
                        offset = worker.modifyOffsetVector(offset, this);
                    }

                    v.add(offset);

                    int b_id = file.getBlockId(x, y, z);
                    int b_meta = file.getBlockData(x, y, z);

                    //                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];
                    //
                    //                    if(handler instanceof SchematicRotationHandler){
                    //                        int old_meta = b_meta;
                    //
                    //                        b_meta = ((SchematicRotationHandler)handler).rotateData(file, x, y, z, b_id, b_meta, rotations);
                    //
                    //                        SchematicDataRegistry.logger().config("Transformer found for " + b_id + " : " + old_meta + " -> " + b_meta);
                    //                    }
                    int[] b = {b_id,b_meta};

                    for(ISchematicWorker worker : workers){
                        b = worker.modifyBlock(b,v.getX(),v.getY(),v.getZ(),this);
                    }

                    b_id = b[0];
                    b_meta = b[1];



                    //                  if(b_id==0 && !pasteAir){
                    //                  continue;
                    //              }
                    boolean skip = false;
                    for(ISchematicWorker worker : workers){
                        if(!worker.canPaste(world,v.getX(), v.getY(), v.getZ(), b_id, b_meta,this)){
                            skip = true;
                        }
                    }
                    if(skip){
                        continue;
                    }


                    if(Block.blocksList[b_id] != null){
                        world.setBlock(v.getX(), v.getY(), v.getZ(), b_id, b_meta,0);
                    }
                    else
                    {
                        if(b_id !=0){
                            SchematicDataRegistry.logger().severe("UNKNOWN BLOCK [" + v.getX() +", " + v.getY() +", " + v.getZ() +"] "  + b_id + ":" + b_meta);
                        }
                    }


                    TileEntity te = file.getTileEntityAt(x, y, z);
                    if(te!=null){
                        for(ISchematicWorker worker : workers){
                            te = worker.transformTileEntity(te, this);
                        }
                        SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                        world.setBlockTileEntity(v.getX(), v.getY(), v.getZ(),te);
                        world.setBlockMetadataWithNotify(v.getX(), v.getY(), v.getZ(), b_meta,2);//Fix container blocks being derpy
                    }
                }   
            }   
        }

        //TODO: ENTITY SPAWNING
    }


    /**
     * Validates a world area against a schematic, returns true if world area matches schematic
     * @param world world object to validate
     * @param initVector Location to validate at
     * @param rotations Amount of times to rotate the schematic 90 degrees clockwise.
     * @param maskId id of block to act as a mask, any block in the schematic of this type is not checked. 
     */
    public boolean validateInWorld(World world,SchVector initVector,int rotations,int maskId){

        SchVector initialVector = new SchVector();
        initialVector.add(initVector);


        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){

                    //Construct the vector for this location, taking into account rotation
                    SchVector v = new SchVector();
                    v.add(initialVector);
                    SchVector offset = new SchVector(x,y,z);
                    offset.add(file.getExtension(WorldEditVectorExtension.class).getOffset());
                    offset.rotateVector(rotations);
                    v.add(offset);

                    int b_id = file.getBlockId(x, y, z);
                    int b_meta = file.getBlockData(x, y, z);


                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];

                    if(handler instanceof SchematicRotationHandler){
                        int old_meta = b_meta;

                        b_meta = ((SchematicRotationHandler)handler).rotateData(file, x, y, z, b_id, b_meta, rotations);

                        SchematicDataRegistry.logger().config("Transformer found for " + b_id + " : " + old_meta + " -> " + b_meta);
                    }

                    if(b_id != maskId &&
                            (world.getBlockId(v.getX(), v.getY(), v.getZ()) !=  b_id ||
                            world.getBlockMetadata(v.getX(), v.getY(), v.getZ()) != b_meta)
                            ){
                        return false;
                    }

                }   
            }   
        }

        return true;
    }

}
