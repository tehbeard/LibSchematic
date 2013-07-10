package com.tehbeard.forge.schematic.product;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class PasteToWorld extends ActOnWorld {
    

    public PasteToWorld(World world) {
        super(world);
    }

    @Override
    protected Object action(int x,int y,int z,int b_id, int b_meta, SchVector worldVector, SchematicFile file) {
        if(Block.blocksList[b_id] != null || b_id == 0){
            world.setBlock(worldVector.getX(), worldVector.getY(), worldVector.getZ(), b_id, b_meta,2);
            
            TileEntity te = file.getTileEntityAt(x,y,z);
            if(te!=null){
                SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                world.setBlockTileEntity(worldVector.getX(), worldVector.getY(), worldVector.getZ(),te);
                world.setBlockMetadataWithNotify(worldVector.getX(), worldVector.getY(),worldVector.getZ(), b_meta,2);//Fix container blocks being derpy
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
