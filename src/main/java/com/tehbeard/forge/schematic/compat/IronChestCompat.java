package com.tehbeard.forge.schematic.compat;

import net.minecraft.tileentity.TileEntity;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.rotations.TileEntityRotationHandler;
import com.tehbeard.forge.schematic.handlers.rotations.VanillaRotations;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.TileEntityIronChest;

@Mod(modid = "libschematic.compat.ironchest", name = "LibSchematic::IronChest", version = "1.0", useMetadata = true)
public class IronChestCompat {

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("IronChest")) {
            SchematicDataRegistry.logger().info("Installing IronChest handler");
            SchematicDataRegistry.setHandler(IronChest.ironChestBlock.blockID,
                    new TileEntityRotationHandler() {

                        @Override
                        public void rotateTileEntity(SchematicFile schematic,
                                int x, int y, int z, int blockId, int metadata,
                                TileEntity tileEntity, int rotations) {
                            TileEntityIronChest teic = (TileEntityIronChest) tileEntity;
                            int facing = teic.getFacing();
                            facing = VanillaRotations.CONTAINER_PISTON
                                    .rotateData(schematic, x, y, z, blockId,
                                            facing, rotations);
                            teic.setFacing((byte) (facing & 0xFF));

                        }
                    });
        }
    }
}
