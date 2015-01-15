package com.tehbeard.forge.schematic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Generates a schematic of an area of a world. Default behaviour mimics
 * WorldEdit (save TE's, do not save entities)
 * 
 * @author James
 * 
 */
public class Blueprint {

    private boolean ignoreTileEntityData = false;// By default save tile
                                                 // entities
    private boolean ignoreEntityData = true;// By default ignore entities

    private World world;

    private SchVector min;
    private SchVector max;

    public Blueprint(World world, SchVector v1, SchVector v2) {
        this.world = world;
        min = SchVector.min(v1, v2);
        max = SchVector.max(v1, v2);
    }

    /**
     * Set true to not capture TileEntity states
     * 
     * @param ignoreTileEntityData
     * @return
     */
    public Blueprint setIgnoreTileEntityData(boolean ignoreTileEntityData) {
        this.ignoreTileEntityData = ignoreTileEntityData;
        return this;
    }

    /**
     * Set true to not capture entities
     * 
     * @param ignoreEntityData
     * @return
     */
    public Blueprint setIgnoreEntityData(boolean ignoreEntityData) {
        this.ignoreEntityData = ignoreEntityData;
        return this;
    }

    public SchematicFile createSchematicFile() {
        SchVector size = new SchVector(max).add(new SchVector(1, 1, 1));
        size.sub(min);

        SchematicFile file = new SchematicFile((short) size.getX(),
                (short) size.getY(), (short) size.getZ());

        if (!ignoreEntityData) {
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(min.getX(),
                    min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
            @SuppressWarnings("rawtypes")
            List entities = world.getEntitiesWithinAABB(EntityCreature.class,
                    bb);

            for (Object o : entities) {
                Entity e = (Entity) o;
                if (e instanceof EntityPlayer) {
                    continue;
                }

                NBTTagCompound tag = new NBTTagCompound();
                if (e.writeToNBTOptional(tag)) {
                    // Overrride location
                    double x = e.posX - min.getX();
                    double y = e.posY - min.getY();
                    double z = e.posZ - min.getZ();
                    NBTTagList l = new NBTTagList();
                    l.appendTag(new NBTTagDouble(x));
                    l.appendTag(new NBTTagDouble(y));
                    l.appendTag(new NBTTagDouble(z));

                    tag.setTag("Pos", l);
                    file.getEntities().add(tag);
                }
            }
        }

        for (int y = 0; y < file.getHeight(); y++) {
            for (int x = 0; x < file.getWidth(); x++) {
                for (int z = 0; z < file.getLength(); z++) {
                    int wx = x + min.getX();
                    int wy = y + min.getY();
                    int wz = z + min.getZ();

                    SchematicDataRegistry.logger().debug(String.format(
                            "ID at world %d-%d-%d of block %s is %d",
                            x, y, z, world.getBlock(wx, wy, wz).getLocalizedName(),
                            Block.getIdFromBlock(world.getBlock(wx, wy, wz))
                    ));

                    file.setBlockId(x, y, z, Block.getIdFromBlock(world.getBlock(wx, wy, wz)));
                    file.setBlockData(x, y, z,
                            (byte) (world.getBlockMetadata(wx, wy, wz) & 0xFF));

                    if (!ignoreTileEntityData) {
                        TileEntity te = world.getTileEntity(wx, wy, wz);
                        if (te != null) {
                            NBTTagCompound c = new NBTTagCompound();
                            te.writeToNBT(c);
                            c.setInteger("x", x);
                            c.setInteger("y", y);
                            c.setInteger("z", z);
                            file.getTileEntities().add(c);
                        }
                    }
                }
            }
        }

        return file;
    }

}
