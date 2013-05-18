package com.tehbeard.forge.schematic.worker;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicWorker;

/**
 * represents a process that can occur to a schematic during pasting
 * @author James
 *
 */
public abstract class ISchematicWorker {

    
    /**
     * Called to modify the value of the offset vector
     * The offset vector is the location in world relative to the initial start point that a block from a schematic will be pasted to
     * Final location can be calculated by summing the init vector with this one.
     * Hooked into for rotation and offsets.
     * @param vector
     * @param worker
     * @return
     */
    public SchVector modifyOffsetVector(SchVector vector,SchematicWorker worker){
        return vector;
    }
    
    
    /**
     * Called to check if paste should paste a block
     * @param world world to paste into
     * @param x coord in world
     * @param y coord in world
     * @param z coord in world
     * @param b_id block that will be placed
     * @param b_meta metadata of block that will be placed
     * @return whether to paste or not
     */
    public boolean canPaste(World world,int x,int y,int z,int b_id,int b_meta,SchematicWorker worker){
        return true;
    }
    
    /**
     * Called before the paste to modify the block id or metadata
     * @param block [BlockId, BlockMetadata]
     * @return
     */
    public int[] modifyBlock(int[] block,int sx,int sy,int sz,SchematicWorker worker){
        return block;
    }
    
    /**
     * Called to modify a tile Entity before placement
     * @param tileEntity
     * @param worker
     * @return
     */
    public TileEntity transformTileEntity(TileEntity tileEntity,SchematicWorker worker){
        return tileEntity;
    }
}
