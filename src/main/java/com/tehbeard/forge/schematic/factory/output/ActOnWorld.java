package com.tehbeard.forge.schematic.factory.output;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.SchematicDataHandler;
import com.tehbeard.forge.schematic.handlers.SchematicRotationHandler;
import com.tehbeard.forge.schematic.handlers.tileentity.TileEntityTranslator;

/**
 * Abstract {@link IFactoryOuput} that has methods for manipulating a world It
 * is designed for Output's that iterate over a schematic with relation to a
 * world's coords
 * 
 * @author James
 * 
 */
public abstract class ActOnWorld implements IFactoryOuput {

    protected final World world;
    private int rotations;
    private SchVector worldVec;

    public ActOnWorld(World world) {
        this.world = world;
    }

    public SchVector getWorldVec() {
        return worldVec;
    }

    public void setWorldVec(SchVector worldVec) {
        this.worldVec = worldVec;
    }

    @Override
    public Object process(SchematicFile file) {
        if (file == null)
            throw new IllegalStateException(
                    "No schematic was passed! Aborting paste!");

        int total = file.getHeight() * file.getWidth() * file.getLength();
        int idx = 0;
        for (int y = 0; y < file.getHeight(); y++) {
            for (int x = 0; x < file.getWidth(); x++) {
                for (int z = 0; z < file.getLength(); z++) {

                    idx++;
                    if (idx % 100 == 0) {
                        SchematicDataRegistry.logger().debug("{0}/{1} Blocks processed", new Object[]{idx, total});
                    }

                    // Generate relative location based on rotation and offset.
                    SchVector schematicWorldOffsetVector = new SchVector(x, y,
                            z).add(file.getInitialVector()).rotateVector(
                            rotations);

                    // Generate world location based on start location +
                    // relative location.
                    SchVector worldVector = new SchVector(worldVec);
                    worldVector.add(schematicWorldOffsetVector);

                    // Get block and meta ids, if block id is -1, we skip this
                    // block
                    int b_id = file.getBlockId(x, y, z);
                    byte b_meta = file.getBlockData(x, y, z);

                    SchematicDataRegistry.logger().debug(String.format(
                            "Block %d/%d/%d has id %d and is made of %s",
                            x, y, z, b_id, Block.getBlockById(b_id).getLocalizedName()
                    ));

                    if (b_id == -1) {
                        continue;
                    }

                    // Get TileEntity and process
                    TileEntity te = null;
                    NBTTagCompound tileEntityTag = file.getTileEntityTagAt(x,
                            y, z);
                    if (tileEntityTag != null) {
                        SchematicDataRegistry.logger().debug("Initialising Tile Entity {0}", tileEntityTag.toString());
                        TileEntityTranslator teHandler = SchematicDataRegistry.tileEntityManager.get(tileEntityTag.getString("id"));
                        if (teHandler != null) {
                            SchematicDataRegistry.logger().debug("Loaded tile entity manager for id {0}", tileEntityTag.getString("id"));
                            te = teHandler.unpack(tileEntityTag,world,
                                    worldVector.getX(), worldVector.getY(),
                                    worldVector.getZ());
                        } else {
                            te = SchematicDataRegistry.defaultTileEntityManager
                                    .unpack(tileEntityTag,world, worldVector.getX(),
                                            worldVector.getY(),
                                            worldVector.getZ());
                        }

                        // Something went wrong
                        if (te == null)
                            throw new RuntimeException(
                                    "Could not deserialize TileEntity correctly");
                    }

                    // Grab handler and try to rotate data
                    SchematicDataHandler handler = SchematicDataRegistry.dataHandlers[b_id];
                    if (handler instanceof SchematicRotationHandler && rotations > 0) {
                        b_meta = (byte) ((SchematicRotationHandler) handler)
                                .rotateData(file, x, y, z, b_id, b_meta,
                                        rotations);
                        if (te != null) {
                            ((SchematicRotationHandler) handler)
                                    .rotateTileEntity(file, x, y, z, b_id,
                                            b_meta, te, rotations);
                        }
                    }

                    // Run the action
                    Object res = action(x, y, z, b_id, b_meta, te, worldVector,
                            file);

                    if (res != null)
                        return res;

                }
            }
        }
        // System.out.println(idx + "/" + total + "Blocks processed");

        postAction(worldVec, file);
        return null;

    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    protected abstract Object action(int x, int y, int z, int b_id, int b_meta,
            TileEntity tileEntity, SchVector worldVector, SchematicFile file);

    protected abstract void postAction(SchVector worldVector, SchematicFile file);
}