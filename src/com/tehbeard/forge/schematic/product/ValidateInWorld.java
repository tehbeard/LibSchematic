package com.tehbeard.forge.schematic.product;

import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

public class ValidateInWorld implements IFactoryOuput {

    private World world;

    public ValidateInWorld(World world){
        this.world = world;
    }

    @Override
    public Object process(SchematicFile file) {
        if(file == null){throw new IllegalStateException("No schematic was passed to the factory! Aborting paste!");}

        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){

                    SchVector schVector = new SchVector(x,y,z);

                    SchVector worldVector = new SchVector();
                    worldVector.add(file.getInitialVector());
                    worldVector.add(schVector);

                    int b_id = file.getBlockId(schVector);
                    byte b_meta = file.getBlockData(schVector);

                    if(b_id == -1){continue;}

                    int w_id = world.getBlockId(worldVector.getX(), worldVector.getY(), worldVector.getZ());
                    byte w_meta = (byte) world.getBlockMetadata(worldVector.getX(), worldVector.getY(), worldVector.getZ());

                    if(b_id != w_id || b_meta != w_meta){
                        return false;
                    }

                }   
            }   
        }
        return true;

    }

}
