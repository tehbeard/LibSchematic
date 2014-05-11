package com.tehbeard.forge.schematic.factory.output;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;

/**
 * Iterates over a schematic and adds it to a world as a collection of
 * {@link EntityFallingSand} objects <b>!!!WARNING!!! NO LIMITS ARE ENFORCED,
 * TAKE CAUTION WHEN USING THIS {@link IFactoryOuput} WITH LARGE SCHEMATICS</b>
 * 
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

            if (b_id == 0)
                return null;
            EntityFallingBlock block = new EntityFallingBlock(world,
                    worldVector.getX() + 0.5D, worldVector.getY() + 0.5D,
                    worldVector.getZ() + 0.5D, Block.getBlockById(b_id), b_meta);
            block.field_145812_b = 2; // fallTime
            NBTTagCompound te = file.getTileEntityTagAt(x, y, z);
            if (te != null) {
                block.field_145810_d = (NBTTagCompound) te.copy(); //fallingBlockTitleEntityData
            }

            world.spawnEntityInWorld(block);
        
        return null;
    }

    @Override
    protected void postAction(SchVector worldVector, SchematicFile file) {
        for (NBTTagCompound e : file.getEntities()) {
            Entity entity = EntityList.createEntityFromNBT(e, world);
            entity.posX += worldVector.getX();
            entity.posY += worldVector.getY();
            entity.posZ += worldVector.getZ();
            world.spawnEntityInWorld(entity);
        }

    }

}
