package com.tehbeard.forge.schematic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Generates a schematic of an area of a world.
 * @author James
 *
 */
public class Blueprint {

    private boolean noTileEntityData;
    private World world;

    private SchVector min;
    private SchVector max;
    public Blueprint(boolean noTileEntityData, World world, SchVector v1,
            SchVector v2) {
        super();
        this.noTileEntityData = noTileEntityData;
        this.world = world;
        this.min = SchVector.min(v1, v2);
        this.max = SchVector.max(v1, v2);
    }

    public SchematicFile createSchematicFile(){
        SchVector size = new SchVector(max);
        size.sub(min);
        SchematicFile file = new SchematicFile((short)size.getX(), (short)size.getY(), (short)size.getZ());

        for(int y = 0;y<file.getHeight();y++){
            for(int x = 0;x<file.getWidth();x++){
                for(int z = 0;z<file.getLength();z++){
                    int wx = x + min.getX();
                    int wy = y + min.getY();
                    int wz = z + min.getZ();
                    
                    file.setBlockId(x, y,z,world.getBlockId(wx, wy, wz));
                    file.setBlockData(x, y,z,(byte) (world.getBlockMetadata(wx, wy, wz) & 0xFF));
                    
                    if(!noTileEntityData){
                        TileEntity te = world.getBlockTileEntity(wx, wy, wz);
                        if(te!=null){
                            NBTTagCompound c = new NBTTagCompound();
                            te.writeToNBT(c);
                            file.getTileEntities().add(c);
                        }
                    }
                }
            }
        }

        return file;
    }




}
