package com.tehbeard.forge.schematic.data;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * An implementation of a schematic rotation handler
 * That uses an array lookup table + bitmask as the data for rotation
 * @author James
 *
 */
public class ArrayRotationHandler implements SchematicRotationHandler{

    private int [] rotationsArray;
    private int mask;
    
    /**
     * 
     * @param mask mask for data that is NOT rotation bits
     * i.e. if top two bits are not rotation data, then the mask is 0xC
     * @param rotationsArray array of ints to rotate around
     */
    public ArrayRotationHandler(int mask, int... rotationsArray) {
        super();
        this.mask = mask;
        this.rotationsArray = rotationsArray;
    }

    public int rotateData(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, int rotations) {
        
        int extraData = metadata & mask;
        int rotate = metadata & ~mask;
        int idx = getIdx(rotationsArray, rotate);

        if(idx == -1){return metadata;}

        idx = (idx + rotations) % rotationsArray.length;

        if(idx < 0){
            idx += rotationsArray.length;
        }

        return rotationsArray[idx]  | extraData;
    }
    
    private int getIdx(int[] array,int value){
        for(int i =0;i<array.length;i++){
            if(array[i] == value){
                return i;
            }
        }
        return -1;

    }

}
