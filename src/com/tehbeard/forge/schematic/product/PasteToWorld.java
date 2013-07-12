package com.tehbeard.forge.schematic.product;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;


public class PasteToWorld extends ActOnWorld {
    

    public PasteToWorld(World world) {
        super(world);
    }

    @Override
    protected Object action(int x,int y,int z,int b_id, int b_meta, SchVector worldVector, SchematicFile file) {
        if(Block.blocksList[b_id] != null || b_id == 0){
            
            //world.setBlock(worldVector.getX(), worldVector.getY(), worldVector.getZ(), b_id, b_meta,2);// - Vanilla Standard
            
            
            Chunk chunk = world.getChunkFromBlockCoords(worldVector.getX(), worldVector.getZ());
            ExtendedBlockStorage storageArray = chunk.getBlockStorageArray()[worldVector.getY() >> 4];
            
            storageArray.setExtBlockID(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_id);
            storageArray.setExtBlockMetadata(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_meta);
            chunk.isModified = true;
            
            world.markBlockForUpdate(worldVector.getX(), worldVector.getY(), worldVector.getZ());
            //world;
            TileEntity te = file.getTileEntityAt(x,y,z);
            if(te!=null){
                SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                world.setBlockTileEntity(worldVector.getX(), worldVector.getY(), worldVector.getZ(),te);
                //world.setBlockMetadataWithNotify(worldVector.getX(), worldVector.getY(),worldVector.getZ(), b_meta,2);//Fix container blocks being derpy
            }
        }
        else
        {
            if(b_id >0){
                SchematicDataRegistry.logger().severe("UNKNOWN BLOCK [" + x +", " + y +", " + z +"] "  + b_id + ":" + b_meta);
            }
        }
        return null;
    }

}
