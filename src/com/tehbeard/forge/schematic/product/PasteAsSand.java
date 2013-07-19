package com.tehbeard.forge.schematic.product;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Iterates over a schematic and adds it to a world as a collection of {@link EntityFallingSand} objects
 * <b>!!!WARNING!!! NO LIMITS ARE ENFORCED, TAKE CAUTION WHEN USING THIS {@link IFactoryOuput} WITH LARGE SCHEMATICS</b>
 * @author James
 *
 */
public class PasteAsSand extends ActOnWorld {
    

    public PasteAsSand(World world) {
        super(world);
    }

    @Override
    protected Object action(int x, int y, int z, int b_id, int b_meta,
            TileEntity tileEntity, SchVector worldVector, SchematicFile file) {
        if(Block.blocksList[b_id] != null || b_id == 0){
            
            //world.setBlock(,2);// - Vanilla Standard
            if(b_id==0){return null;}
            EntityFallingSand block = new EntityFallingSand(world,worldVector.getX() + 0.5D, worldVector.getY() + 0.5D, worldVector.getZ() + 0.5D, b_id, b_meta);
            block.fallTime = 2;
            NBTTagCompound te = file.getTileEntityTagAt(x, y, z);
            if(te!=null){
                block.fallingBlockTileEntityData = (NBTTagCompound) te.copy();
            }
            
            world.spawnEntityInWorld(block);
            
            /*Chunk chunk = world.getChunkFromBlockCoords(worldVector.getX(), worldVector.getZ());
            ExtendedBlockStorage storageArray = chunk.getBlockStorageArray()[worldVector.getY() >> 4];
            
            storageArray.setExtBlockID(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_id);
            storageArray.setExtBlockMetadata(worldVector.getX() & 15, worldVector.getY() & 15, worldVector.getZ() & 15, b_meta);
            world.markBlockForUpdate(worldVector.getX(), worldVector.getY(), worldVector.getZ());
            //world;
            TileEntity te = file.getTileEntityAt(x,y,z);
            if(te!=null){
                SchematicDataRegistry.logger().config("Initialising Tile Entity " + te.toString());
                world.setBlockTileEntity(worldVector.getX(), worldVector.getY(), worldVector.getZ(),te);
                //world.setBlockMetadataWithNotify(worldVector.getX(), worldVector.getY(),worldVector.getZ(), b_meta,2);//Fix container blocks being derpy
            }*/
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
            Entity entity = EntityList.createEntityFromNBT(e, world);
            entity.posX += worldVector.getX();
            entity.posY += worldVector.getY();
            entity.posZ += worldVector.getZ();
            world.spawnEntityInWorld(entity);
        }
        
    }

   

}
