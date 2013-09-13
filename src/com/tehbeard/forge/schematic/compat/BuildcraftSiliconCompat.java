package com.tehbeard.forge.schematic.compat;

import java.lang.reflect.Field;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import appeng.common.AppEng;
import appeng.common.base.AppEngTile;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftSilicon;
import buildcraft.energy.TileEngine;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.ForgeDirectionRotationHandler;
import com.tehbeard.forge.schematic.data.SimpleRotationHandler;
import com.tehbeard.forge.schematic.data.TileEntityRotationHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "libschematic.compat.buildcraft.silicon",name="LibSchematic::BuildCraft::Silicon",version="1.0",dependencies="after:BuildCraft|Silicon",useMetadata=true)
public class BuildcraftSiliconCompat {
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("BuildCraft|Silicon")){
			SchematicDataRegistry.logger().info("Installing BuildCraft|Silicon handler");
			
			SchematicDataRegistry.setHandler(BuildCraftSilicon.laserBlock.blockID,new ForgeDirectionRotationHandler(0x0));
		}
	}
}
