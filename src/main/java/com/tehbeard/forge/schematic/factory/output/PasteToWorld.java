package com.tehbeard.forge.schematic.factory.output;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import codechicken.multipart.MultipartHelper;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import java.util.logging.Level;

/**
 * Pastes a schematic into a world as solid blocks
 * 
 * @author James
 * 
 */
public class PasteToWorld extends ActOnWorld {

    public PasteToWorld(World world) {
        super(world);
    }

    @Override
    protected Object action(int x, int y, int z, int b_id, int b_meta,
            TileEntity te, SchVector worldVector, SchematicFile file) {

            // Make sure chunk exists.
            Chunk chunk = world.getChunkFromBlockCoords(worldVector.getX(),
                    worldVector.getZ());
            ExtendedBlockStorage storageArray = chunk.getBlockStorageArray()[worldVector
                    .getY() >> 4];

            if (storageArray == null) {
                ExtendedBlockStorage[] storageArrays = chunk
                        .getBlockStorageArray();
                storageArrays[worldVector.getY() >> 4] = new ExtendedBlockStorage(
                        storageArrays[(worldVector.getY() >> 4) - 1]
                                .getYLocation() + 16,
                        !world.provider.hasNoSky);
                storageArray = chunk.getBlockStorageArray()[worldVector.getY() >> 4];
            }

            // Set the block data
            storageArray.func_150818_a(worldVector.getX() & 15,worldVector.getY() & 15, worldVector.getZ() & 15, Block.getBlockById(b_id)); //setBlock
            storageArray.setExtBlockMetadata(worldVector.getX() & 15,worldVector.getY() & 15, worldVector.getZ() & 15, b_meta);
            chunk.isModified = true;

            world.func_147451_t(worldVector.getX(), worldVector.getY(),worldVector.getZ()); //updateAllLightTypes

            world.markBlockForUpdate(worldVector.getX(), worldVector.getY(),worldVector.getZ());

            // place Tile Entity
            if (te != null) {
                world.setTileEntity(worldVector.getX(),
                        worldVector.getY(), worldVector.getZ(), te);
                if(file.getTileEntityTagAt(x, y, z).getString("id").equals("savedMultipart")){
                    MultipartHelper.sendDescPacket(world, te);
                }
            }

        return null;
    }

    @Override
    protected void postAction(SchVector worldVector, SchematicFile file) {
        for (NBTTagCompound e : file.getEntities()) {
            // Entity entity = EntityList.createEntityFromNBT(e, world);
            Entity entity = EntityList.createEntityFromNBT(e, world);
            if (entity == null) {
                continue;
            }
            entity.setPosition(entity.posX + worldVector.getX(), entity.posY
                    + worldVector.getY(), entity.posZ + worldVector.getZ());
            world.spawnEntityInWorld(entity);
        }
    }

}
