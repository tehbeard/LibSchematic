package com.tehbeard.forge.schematic;

import java.util.List;

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
 * Generates a schematic of an area of a world.
 * Default behaviour mimics WorldEdit (save TE's, do not save entities)
 * @author James
 *
 */
public class Blueprint {

    private boolean ignoreTileEntityData = false;//By default save tile entities
    private boolean ignoreEntityData     = true;//By default 

    private World world;

    private SchVector min;
    private SchVector max;


    public Blueprint(World world, SchVector v1,
            SchVector v2) {
        this.world = world;
        this.min = SchVector.min(v1, v2);
        this.max = SchVector.max(v1, v2);
    }

    /**
     * Set true to not capture TileEntity states
     * @param ignoreTileEntityData
     * @return
     */
    public Blueprint setIgnoreTileEntityData(boolean ignoreTileEntityData){
        this.ignoreTileEntityData = ignoreTileEntityData;
        return this;
    }

    /**
     * Set true to not capture entities
     * @param ignoreEntityData
     * @return
     */
    public Blueprint setIgnoreEntityData(boolean ignoreEntityData){
        this.ignoreEntityData = ignoreEntityData;
        return this;
    }



    public SchematicFile createSchematicFile(){
        SchVector size = new SchVector(max).add(new SchVector(1,1,1));
        size.sub(min);

        SchematicFile file = new SchematicFile((short)size.getX(), (short)size.getY(), (short)size.getZ());

        if(!ignoreEntityData){
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
            @SuppressWarnings("rawtypes")
            List entities = world.getEntitiesWithinAABB(EntityCreature.class, bb);

            for(Object o : entities){
                Entity e = (Entity)o;
                if(e instanceof EntityPlayer){continue;}

                System.out.println("located entity of type " + e.getTranslatedEntityName() + ", preparing cryogenics");
                NBTTagCompound tag = new NBTTagCompound();
                if(e.writeToNBTOptional(tag)){
                    //Overrride location
                    double x = e.posX - min.getX();
                    double y = e.posY - min.getY();
                    double z = e.posZ - min.getZ();
                    NBTTagList l = new NBTTagList("Pos");
                    l.appendTag(new NBTTagDouble(null, x));
                    l.appendTag(new NBTTagDouble(null, y));
                    l.appendTag(new NBTTagDouble(null, z));


                    tag.setTag("Pos", l);
                    file.getEntities().add(tag);
                }
            }
        }

        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){
                    int wx = x + min.getX();
                    int wy = y + min.getY();
                    int wz = z + min.getZ();

                    file.setBlockId(x, y,z,world.getBlockId(wx, wy, wz));
                    file.setBlockData(x, y,z,(byte) (world.getBlockMetadata(wx, wy, wz) & 0xFF));

                    if(!ignoreTileEntityData){
                        TileEntity te = world.getBlockTileEntity(wx, wy, wz);
                        if(te!=null){
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
