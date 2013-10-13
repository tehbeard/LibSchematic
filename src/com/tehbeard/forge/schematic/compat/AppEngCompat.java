package com.tehbeard.forge.schematic.compat;

import net.minecraft.tileentity.TileEntity;
import appeng.api.me.tiles.IOrientableTile;
import appeng.common.AppEngConfiguration;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.rotations.TileEntityRotationHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

/**
 * Adds rotation support to AE blocks
 * 
 * @author James
 * 
 */
@Mod(modid = "libschematic.compat.appeng", name = "LibSchematic::AppliedEnergistics", version = "1.0", dependencies = "after:AppliedEnergistics", useMetadata = true)
public class AppEngCompat {
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("AppliedEnergistics")) {
            SchematicDataRegistry.logger().info(
                    "Installing AppliedEnergistics handler");

            TileEntityRotationHandler rotatron = new TileEntityRotationHandler() {

                @Override
                public void rotateTileEntity(SchematicFile schematic, int x,
                        int y, int z, int blockId, int metadata,
                        TileEntity tileEntity, int rotations) {
                    if (tileEntity instanceof IOrientableTile) {
                        IOrientableTile tile = (IOrientableTile) tileEntity;
                        tile.setPrimaryOrientation(fdRotate(
                                tile.getPrimaryOrientation(), rotations));
                    }
                }
            };

            /*
             * new TileEntityRotationHandler() {
             * 
             * private Field getDatField(Class c,String fieldName,int depth){
             * 
             * try { Field f = c.getDeclaredField(fieldName);
             * f.setAccessible(true); return f; } catch (NoSuchFieldException e)
             * { if(depth > 1){ return getDatField(c.getSuperclass(), fieldName,
             * depth-1); } else { return null; } } catch (SecurityException e) {
             * e.printStackTrace(); return null; } }
             * 
             * @Override public void rotateTileEntity(SchematicFile schematic,
             * int x, int y, int z, int blockId, int metadata, TileEntity
             * tileEntity,int rotations) {
             * 
             * if(tileEntity == null){return;} if(tileEntity instanceof
             * AppEngTile){
             * 
             * try{ SchematicDataRegistry.logger().info("AE: " +
             * tileEntity.getClass().getName());
             * 
             * Field f = getDatField(tileEntity.getClass(),"orientation",2);
             * 
             * if(f == null){
             * SchematicDataRegistry.logger().info("NO FIELD FOUND");return;}
             * 
             * ForgeDirection dir = (ForgeDirection) f.get(tileEntity); dir =
             * fdRotate(dir, rotations); f.set(tileEntity, dir); } catch
             * (IllegalArgumentException e) { // TODO Auto-generated catch block
             * e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
             * Auto-generated catch block e.printStackTrace(); }
             * 
             * }
             * 
             * //facing =
             * VanillaRotations.CONTAINER_PISTON.rotateData(schematic, x, y, z,
             * blockId, facing, rotations); //teic.setFacing((byte) (facing &
             * 0xFF));
             * 
             * }
             * 
             * 
             * };
             */

            SchematicDataRegistry.setHandler(
                    AppEngConfiguration.blockMulti.get(), rotatron);
            SchematicDataRegistry.setHandler(
                    AppEngConfiguration.blockMulti2.get(), rotatron);
            SchematicDataRegistry.setHandler(
                    AppEngConfiguration.blockMulti3.get(), rotatron);
        }
    }
}
