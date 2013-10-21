package com.tehbeard.forge.schematic.compat;

import org.bouncycastle.crypto.RuntimeCryptoException;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.BlockMultipart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.handler.MultipartMod;
import codechicken.multipart.handler.MultipartProxy;
import codechicken.multipart.minecraft.LeverPart;
import codechicken.multipart.minecraft.McBlockPart;
import codechicken.multipart.minecraft.McMetaPart;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.handlers.SchematicDataHandler;
import com.tehbeard.forge.schematic.handlers.rotations.SimpleRotationHandler;
import com.tehbeard.forge.schematic.handlers.rotations.TileEntityRotationHandler;
import com.tehbeard.forge.schematic.handlers.schematic.SchematicRotationHandler;
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
            
            //Hacky way to find MultiPart block
            int id = -1;
            for(int i =0;i<Block.blocksList.length;i++){
                if(Block.blocksList[i] instanceof BlockMultipart){
                    id = i;
                    return;
                }
            }
            if(id == -1){
                throw new RuntimeException("Could not locate Forge Multipart block ID");
            }
            
            SchematicRotationHandler multiPartRotationHandler = new TileEntityRotationHandler() {
                
                @Override
                public void rotateTileEntity(SchematicFile schematic, int x, int y, int z,
                        int blockId, int metadata, TileEntity tileEntity, int rotations) {
                    TileMultipart multipartTile = (TileMultipart)tileEntity;
                    for(TMultiPart part : multipartTile.jPartList()){
                        if(part instanceof McMetaPart){
                            McMetaPart metaPart = (McMetaPart)part;
                            SchematicDataHandler handler = SchematicDataRegistry.getHandler(metaPart.getBlockId());
                            if(handler instanceof SchematicRotationHandler){
                                SchematicRotationHandler rotator = (SchematicRotationHandler)handler;
                                rotator.rotateData(schematic, x, y, z, metaPart.getBlockId(), metaPart.meta, rotations);
                                rotator.rotateTileEntity(schematic, x, y, z, metaPart.getBlockId(), metaPart.meta, metaPart.getTile(), rotations);
                            }
                            
                            
                        }
                    }
                multipartTile.markDirty();    
                }
            };
            SchematicDataRegistry.setHandler(id, multiPartRotationHandler);

            SchematicDataRegistry.tileEntityManager.put("savedMultipart",
                    multipartLoader);

        }
    }
}
