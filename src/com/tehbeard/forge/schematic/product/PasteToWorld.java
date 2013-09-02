package com.tehbeard.forge.schematic.product;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

/**
 * Pastes a schematic into a world as solid blocks
 * @author James
 *
 */
public class PasteToWorld extends ActOnWorld {


    public PasteToWorld(World world) {
        super(world);
    }

    @Override
    protected Object action(int x,int y,int z,int b_id, int b_meta,TileEntity te, SchVector worldVector, SchematicFile file) {
        if(Block.blocksList[b_id] != null || b_id == 0){

            //world.setBlock(worldVector.getX(), worldVector.getY(), worldVector.getZ(), b_id, b_meta,2);// - Vanilla Standard


            Chunk chunk = world.getChunkFromBlockCoords(worldVector.getX(), worldVector.getZ());
            ExtendedBlockStorage storageArray = chunk.getBlockStorageArray()[worldVector.getY() >> 4];
            
            if (storageArray == null) {
                ExtendedBlockStorage[] storageArrays = chunk.getBlockStorageArray();
                storageArrays[worldVector.getY() >> 4] = new ExtendedBlockStorage(storageArrays[(worldVector.getY() >> 4) - 1].getYLocation() + 16 ,!world.provider.hasNoSky);
                storageArray = chunk.getBlockStorageArray()[worldVector.getY() >> 4];
            }

            storageArray.setExtBlockID(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_id);
            storageArray.setExtBlockMetadata(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_meta);
            chunk.isModified = true;
            
            world.updateAllLightTypes(worldVector.getX(), worldVector.getY(), worldVector.getZ());

            world.markBlockForUpdate(worldVector.getX(), worldVector.getY(), worldVector.getZ());
            //world;

            if(te != null){
                world.setBlockTileEntity(worldVector.getX(), worldVector.getY(), worldVector.getZ(),te);
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

    @Override
    protected void postAction(SchVector worldVector, SchematicFile file) {
        for(NBTTagCompound e : file.getEntities()){
            //Entity entity = EntityList.createEntityFromNBT(e, world);
            SchematicDataRegistry.logger().info("World is " + (world.isRemote ? " client " : " server "));
            Entity entity = EntityList.createEntityFromNBT(e, world);
            if(entity == null){continue;}
            entity.setPosition(entity.posX + worldVector.getX(),
            entity.posY + worldVector.getY(),
            entity.posZ + worldVector.getZ()
            );
            world.spawnEntityInWorld(entity);
        }
    }

}
