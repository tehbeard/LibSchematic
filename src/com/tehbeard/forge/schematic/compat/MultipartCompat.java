package com.tehbeard.forge.schematic.compat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TileMultipart;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.ITileEntityLoadHandler;
import com.tehbeard.forge.schematic.data.SchematicRotationHandler;
import com.tehbeard.forge.schematic.data.rotations.TileEntityRotationHandler;
import com.tehbeard.forge.schematic.data.rotations.VanillaRotations;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.TileEntityIronChest;

@Mod(modid = "libschematic.compat.multipart",name="LibSchematic::Multipart",version="1.0",useMetadata=true)
public class MultipartCompat {


    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(Loader.isModLoaded("ForgeMultipart")){
            SchematicDataRegistry.logger().info("Installing Forge Multipart handler");
            
            ITileEntityLoadHandler multipartLoader = new ITileEntityLoadHandler() {
				
				@Override
				public TileEntity generateTileEntity(NBTTagCompound tag) {
					return TileMultipart.createFromNBT(tag);//TODO - Fix when I get a version of FMP with the helper.
				}
			};
			
			SchematicDataRegistry.tileEntityLoaders.put("savedMultipart",multipartLoader);
            
        }
    }
}
