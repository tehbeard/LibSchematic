package com.tehbeard.forge.schematic.compat;

import java.lang.reflect.Field;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.BuildCraftEnergy;
import buildcraft.energy.TileEngine;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.rotations.SimpleRotationHandler;
import com.tehbeard.forge.schematic.data.rotations.TileEntityRotationHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "libschematic.compat.buildcraft.energy",name="LibSchematic::BuildCraft::Energy",version="1.0",dependencies="after:BuildCraft|Energy",useMetadata=true)
public class BuildcraftEnergyCompat {
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("BuildCraft|Energy")){
			SchematicDataRegistry.logger().info("Installing BuildCraft|Energy handler");
			
			TileEntityRotationHandler engineRotation = new TileEntityRotationHandler() {
				
				@Override
				public void rotateTileEntity(SchematicFile schematic, int x, int y, int z,
						int blockId, int metadata, TileEntity tileEntity, int rotations) {
					if(tileEntity instanceof TileEngine == false){
						SchematicDataRegistry.logger().warning("CALLED ENGINE ROTATION CODE ON NON ENGINE BLOCK");
						return;
					}
					TileEngine engine = (TileEngine)tileEntity;
					engine.orientation = fdRotate(engine.orientation, rotations);
				}
			};
			
			SchematicDataRegistry.setHandler(BuildCraftEnergy.engineBlock.blockID,engineRotation);
		}
	}
}
