package com.tehbeard.forge.schematic.worker;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.data.SchematicDataHandler;
import com.tehbeard.forge.schematic.data.SchematicOwnerHandler;

/**
 * Uses the {@link SchematicDataRegistry}s list of {@link SchematicDataHandler}s, specifically {@link SchematicOwnerHandler}s to change the owner of a schematic
 * @author James
 *
 */
public class SetOwnerWorker extends AbstractSchematicWorker {
    
    private String newOwner;
    
    public SetOwnerWorker(String newOwner){
        this.newOwner = newOwner;
    }

    public SchematicFile modifySchematic(SchematicFile original) {
        for(int y = 0;y<original.getHeight();y++){
            for(int x = 0;x<original.getWidth();x++){
                for(int z = 0;z<original.getLength();z++){
                    SchematicDataHandler handler = SchematicDataRegistry.getHandler(original.getBlockId(x,y,z));
                    if(handler==null){continue;}
                    if(handler instanceof SchematicOwnerHandler){
                        SchematicOwnerHandler ownerChanger = (SchematicOwnerHandler)handler;
                        ownerChanger.setOwner(original, x, y, z, original.getBlockId(x,y,z), original.getBlockData(x, y, z), original.getTileEntityAt(x, y, z), newOwner);
                    }
                    
                }
            }
        }
        
        
        return original;
        
    }
}
