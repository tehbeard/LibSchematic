package com.tehbeard.forge.schematic.compat;


import thermalexpansion.block.TEBlocks;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.compat.te.COFHTileRotator;
import com.tehbeard.forge.schematic.handlers.SchematicDataHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
/**
 * Adds rotation support for Thermal Expansion machines
 * 
 * @author James
 * 
 */
@Mod(modid = "libschematic.compat.thermalexpansion", name = "LibSchematic::ThermalExpansion", version = "1.0", useMetadata = true)
public class ThermalExpansionCompat {
	private SchematicDataHandler handler = new COFHTileRotator();

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded("ThermalExpansion")) {
			SchematicDataRegistry.logger().info("Installing ThermalExpansion handler");
			cfgCOFH(TEBlocks.blockMachine.blockID);
			cfgCOFH(TEBlocks.blockConduit.blockID);
			cfgCOFH(TEBlocks.blockDevice.blockID);
			cfgCOFH(TEBlocks.blockDynamo.blockID);
			cfgCOFH(TEBlocks.blockEnergyCell.blockID);
			cfgCOFH(TEBlocks.blockStorage.blockID);
			cfgCOFH(TEBlocks.blockStrongbox.blockID);
		}
	}
	
	public void cfgCOFH(int id){
		SchematicDataRegistry.setHandler(id, handler );
	}
}

