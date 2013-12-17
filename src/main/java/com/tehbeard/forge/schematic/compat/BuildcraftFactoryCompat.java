package com.tehbeard.forge.schematic.compat;

import buildcraft.BuildCraftFactory;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.handlers.rotations.ForgeDirectionRotationHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "libschematic.compat.buildcraft.factory", name = "LibSchematic::BuildCraft::Factory", version = "1.0", dependencies = "after:BuildCraft|Factory", useMetadata = true)
public class BuildcraftFactoryCompat {

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("BuildCraft|Factory")) {
            SchematicDataRegistry.logger().info(
                    "Installing BuildCraft|Factory handler");

            SchematicDataRegistry.setHandler(
                    BuildCraftFactory.quarryBlock.blockID,
                    new ForgeDirectionRotationHandler(0x0));// TODO: Figure out how to serialize?
            SchematicDataRegistry.setHandler(
                    BuildCraftFactory.refineryBlock.blockID,
                    new ForgeDirectionRotationHandler(0x0));
        }
    }
}
