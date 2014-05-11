//package com.tehbeard.forge.schematic.compat;
//
//import net.minecraft.tileentity.TileEntity;
//import appeng.api.me.tiles.IOrientableTile;
//import appeng.common.AppEngConfiguration;
//
//import com.tehbeard.forge.schematic.SchematicDataRegistry;
//import com.tehbeard.forge.schematic.SchematicFile;
//import com.tehbeard.forge.schematic.handlers.rotations.TileEntityRotationHandler;
//
//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.Mod;
//import cpw.mods.fml.common.Mod.EventHandler;
//import cpw.mods.fml.common.event.FMLPostInitializationEvent;
//
///**
// * Adds rotation support to AE blocks
// * 
// * @author James
// * 
// */
//@Mod(modid = "libschematic.compat.appeng", name = "LibSchematic::AppliedEnergistics", version = "1.0", useMetadata = true)
//public class AppEngCompat {
//    @EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        if (Loader.isModLoaded("AppliedEnergistics")) {
//            SchematicDataRegistry.logger().info(
//                    "Installing AppliedEnergistics handler");
//
//            TileEntityRotationHandler rotatron = new TileEntityRotationHandler() {
//
//                @Override
//                public void rotateTileEntity(SchematicFile schematic, int x,
//                        int y, int z, int blockId, int metadata,
//                        TileEntity tileEntity, int rotations) {
//                    if (tileEntity instanceof IOrientableTile) {
//                        IOrientableTile tile = (IOrientableTile) tileEntity;
//                        tile.setPrimaryOrientation(fdRotate(
//                                tile.getPrimaryOrientation(), rotations));
//                    }
//                }
//            };
//
//            SchematicDataRegistry.setHandler(
//                    AppEngConfiguration.blockMulti.get(), rotatron);
//            SchematicDataRegistry.setHandler(
//                    AppEngConfiguration.blockMulti2.get(), rotatron);
//            SchematicDataRegistry.setHandler(
//                    AppEngConfiguration.blockMulti3.get(), rotatron);
//        }
//    }
//}
