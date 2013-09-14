package com.tehbeard.forge.schematic.compat;

import java.lang.reflect.Field;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import appeng.common.AppEng;
import appeng.common.base.AppEngTile;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
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

@Mod(modid = "libschematic.compat.buildcraft.factory",name="LibSchematic::BuildCraft::Factory",version="1.0",dependencies="after:BuildCraft|Factory",useMetadata=true)
public class BuildcraftFactoryCompat {
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("BuildCraft|Factory")){
			SchematicDataRegistry.logger().info("Installing BuildCraft|Factory handler");
			
			SchematicDataRegistry.setHandler(BuildCraftFactory.quarryBlock.blockID,new ForgeDirectionRotationHandler(0x0));//Borked?
			SchematicDataRegistry.setHandler(BuildCraftFactory.refineryBlock.blockID,new ForgeDirectionRotationHandler(0x0));
		}
	}
}
