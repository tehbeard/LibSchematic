package com.tehbeard.forge.schematic.data;

import net.minecraftforge.common.ForgeDirection;

import com.tehbeard.forge.schematic.SchematicFile;

/**
 * An implementation of a schematic rotation handler
 * That uses the ForgeDirection class to handle rotation on blocks using that.
 * @author James
 *
 */
public class ForgeDirectionRotationHandler extends SimpleRotationHandler{

    private int mask;
    
    /**
     * 
     * @param mask mask for data that is NOT rotation bits
     * i.e. if top two bits are not rotation data, then the mask is 0xC
     */
    public ForgeDirectionRotationHandler(int mask) {
        super();
        this.mask = mask;

    }

    public int rotateData(SchematicFile schematic, int x, int y, int z,
            int blockId, int metadata, int rotations) {
        
        int extraData = metadata & mask;
        int rotate = metadata & ~mask;
        return fdRotate(ForgeDirection.values()[rotate], rotations).ordinal() | extraData;
    }
    
}
