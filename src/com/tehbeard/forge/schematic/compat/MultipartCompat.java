package com.tehbeard.forge.schematic.compat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TileMultipart;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.handlers.tileentity.TileEntityTranslator;

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

            TileEntityTranslator multipartLoader = new TileEntityTranslator() {

                @Override
                public TileEntity unpack(NBTTagCompound tag, int x, int y, int z) {
                    return TileMultipart.createFromNBT(tag);
                }
            };

            SchematicDataRegistry.tileEntityManager.put("savedMultipart",
                    multipartLoader);

        }
    }
}
