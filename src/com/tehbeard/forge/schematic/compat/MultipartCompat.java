package com.tehbeard.forge.schematic.compat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TileMultipart;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.handlers.tileentity.ITileEntityLoadHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "libschematic.compat.multipart", name = "LibSchematic::Multipart", version = "1.0", useMetadata = true)
public class MultipartCompat {

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("ForgeMultipart")) {
            SchematicDataRegistry.logger().info(
                    "Installing Forge Multipart handler");

            ITileEntityLoadHandler multipartLoader = new ITileEntityLoadHandler() {

                @Override
                public TileEntity generateTileEntity(NBTTagCompound tag) {
                    return TileMultipart.createFromNBT(tag);// TODO - Fix when I
                                                            // get a version of
                                                            // FMP with the
                                                            // helper.
                }
            };

            SchematicDataRegistry.tileEntityLoaders.put("savedMultipart",
                    multipartLoader);

        }
    }
}
