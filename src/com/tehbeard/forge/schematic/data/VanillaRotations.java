package com.tehbeard.forge.schematic.data;

import net.minecraft.tileentity.TileEntity;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Container class for all vanilla blocks/tile entities requiring rotation. 
 * If your mod implements the same rotation as a vanilla block, look for it here!
 * @author James
 *
 */
public class VanillaRotations {

    public static final SchematicRotationHandler WALL_MOUNTED = new ArrayRotationHandler(0x8,1,3,2,4);
    public static final SchematicRotationHandler STAIRS = new ArrayRotationHandler(0x4,3,0,2,1);
    public static final SchematicRotationHandler BED = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler COCOA = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler HOOK = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler REPEATER = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler FENCEGATE = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler END_PORTAL = new ArrayRotationHandler(0xC,0,1,2,3);
    public static final SchematicRotationHandler CONTAINER_PISTON = new ArrayRotationHandler(0x8,2,5,3,4);
    public static final SchematicRotationHandler PUMPKIN = new ArrayRotationHandler(0,0,1,2,3);
    public static final SchematicRotationHandler TRAPDOOR = new ArrayRotationHandler(0xC,0,3,1,2);    
    public static final SchematicRotationHandler HEAD = new ArrayRotationHandler(0,2,4,3,5);
    public static final SchematicRotationHandler QUARTZ = new ArrayRotationHandler(0,3,4);
    public static final SchematicRotationHandler WOOD = new ArrayRotationHandler(0x3,0x4,0x8);
    public static final SchematicRotationHandler ANVIL = new ArrayRotationHandler(0xE,0,1);
    public static final SchematicRotationHandler RAIL = new SimpleRotationHandler(){

        @Override
        public int rotateData(SchematicFile schematic, int x, int y, int z,
                int blockId, int metadata, int rotations) {
            int r = metadata & ~0x8;
            int d = metadata & 0x8;
            int rot = rotations;

            if(rot < 0){
                rot = 4 - (rot%4);
            }
            for(int i =0; i< rot;i++){
                switch(r){
                    //flat straight
                    case 0: r = 1; break;
                    case 1: r = 0; break;
                    //SLOPE
                    case 2: r = 5; break;
                    case 5: r = 3; break;
                    case 3: r = 4; break;
                    case 4: r = 2; break;

                    //corner
                    case 6: r = 7; break;
                    case 7: r = 8; break;
                    case 8: r = 9; break;
                    case 9: r = 6; break;

                }
            }
            return r | d;
            //          0x0: flat track going north-south
            //          0x1: flat track going west-east
            //          0x2: track ascending to the east
            //          0x3: track ascending to the west
            //          0x4: track ascending to the north
            //          0x5: track ascending to the south
            //          Regular minecart tracks can make a circle from four rails:
            //          0x6: northwest corner (connecting east and south)
            //          0x7: northeast corner (connecting west and south)
            //          0x8: southeast corner (connecting west and north)
            //          0x9: southwest corner (connecting east and north)
        }

        

    };
    
    public static final SchematicDataHandler VINES = new SimpleRotationHandler() {
        
        @Override
        public int rotateData(SchematicFile schematic, int x, int y, int z,
                int blockId, int metadata, int rotations) {
            int data = metadata;
            data += (data << 4);
            return (data >> (rotations %4)) & 0xF;
        }
    };

    public static final SchematicDataHandler SIGN_POST = new SimpleRotationHandler() {
        
        @Override
        public int rotateData(SchematicFile schematic, int x, int y, int z,
                int blockId, int metadata, int rotations) {
            return (metadata + (4*rotations)) % 16;
        }

        
    };
    
    public static final SchematicDataHandler DOOR = new SimpleRotationHandler() {
        
        @Override
        public int rotateData(SchematicFile schematic, int x, int y, int z,
                int blockId, int metadata, int rotations) {
            
            int isTop = metadata & 0x8;
            if(isTop != 0 ){
                return metadata;

            }else{
                if(rotations < 0){
                    rotations = 4 - (rotations%4);
                }
                int extra = metadata & ~0x3;
                int rotateData = metadata & 0x3;
                for(int i =0; i<rotations;i++){

                    switch(rotateData){
                        case 0: rotateData = 1;break;
                        case 1: rotateData = 2;break;
                        case 2: rotateData = 3;break;
                        case 3: rotateData = 0;break;
                    }
                }
                return rotateData | extra;
            }

        }
        
    };
    
}
