package com.tehbeard.forge.schematic.compat;

import buildcraft.BuildCraftTransport;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.handlers.rotations.ForgeDirectionRotationHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "libschematic.compat.buildcraft.transport", name = "LibSchematic::BuildCraft::Transport", version = "1.0", dependencies = "after:BuildCraft|Transport", useMetadata = true)
public class BuildcraftTransportCompat {

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("BuildCraft|Transport")) {
            SchematicDataRegistry.logger().info(
                    "Installing BuildCraft|Transport handler");

            SchematicDataRegistry.setHandler(
                    BuildCraftTransport.genericPipeBlock.blockID,
                    new ForgeDirectionRotationHandler());
            SchematicDataRegistry.setHandler(
                    BuildCraftTransport.filteredBufferBlock.blockID,
                    new ForgeDirectionRotationHandler());
        }
    }
}
