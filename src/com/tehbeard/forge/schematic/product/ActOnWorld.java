package com.tehbeard.forge.schematic.product;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.data.SchematicRotationHandler;

/**
 * Abstract {@link IFactoryOuput} that has methods for manipulating a world
 * It is designed for Output's that iterate over a schematic with relation to a world's coords
 * @author James
 *
 */
public abstract class ActOnWorld  implements IFactoryOuput {

    protected final World world;
    private int rotations;
    private SchVector worldVec;

    public ActOnWorld(World world){
        this.world = world;
    }


    public SchVector getWorldVec() {
        return worldVec;
    }

    public void setWorldVec(SchVector worldVec) {
        this.worldVec = worldVec;
    }

    public Object process(SchematicFile file) {
        if(file == null){throw new IllegalStateException("No schematic was passed! Aborting paste!");}

        int total = file.getHeight() * file.getWidth() * file.getLength();
        int idx   = 0;
        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){

                    idx ++;
                    if(idx % 100 == 0){
                        System.out.println(idx + "/" + total + "Blocks processed");
                    }

                    SchVector schematicWorldOffsetVector = new SchVector(x,y,z).add(file.getInitialVector()).rotateVector(rotations);

                    SchVector worldVector = new SchVector(worldVec);
                    worldVector.add(schematicWorldOffsetVector);

                    int b_id = file.getBlockId(x,y,z);
                    byte b_meta = file.getBlockData(x,y,z);
                    
                    if(b_id == -1){continue;}
                    
                    TileEntity te = file.getTileEntityAt(x,y,z);
                    if(te!=null){
                        SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                    }

                    //Grab handler and try to rotate data
                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];
                    if(handler instanceof SchematicRotationHandler){
                        int old_meta = b_meta;
                        b_meta = (byte) ((SchematicRotationHandler)handler).rotateData(file, x, y, z, b_id, b_meta, rotations);
                        if(te!=null){
                            ((SchematicRotationHandler)handler).rotateTileEntity(file, x, y, z, b_id, b_meta, te, rotations);
                        }
                        SchematicDataRegistry.logger().config("Transformer found for " + b_id + " : " + old_meta + " -> " + b_meta);
                    }

                    

                    Object res = action(x, y, z, b_id, b_meta,te, worldVector, file);
                    
                    if(res!= null){
                        return res;
                    }



                }   
            }   
        }
        System.out.println(idx + "/" + total + "Blocks processed");

        postAction(worldVec,file);
        return null;

    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    protected abstract Object action(int x,int y,int z,int b_id, int b_meta,TileEntity tileEntity, SchVector worldVector, SchematicFile file);


    protected abstract void postAction( SchVector worldVector, SchematicFile file);
}