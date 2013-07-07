package com.tehbeard.forge.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tehbeard.forge.schematic.product.IFactoryOuput;
import com.tehbeard.forge.schematic.worker.AbstractSchematicWorker;

/**
 * {@link SchematicFactory} Processes a {@link SchematicFile} using one or more {@link AbstractSchematicWorker} workers
 * The factory will initially clone the SchematicFile, after which this clone may be modified or even replaced with another {@link SchematicFile}
 * 
 * The lifecycle of a {@link SchematicFactory} is as follows:
 * new() -> loadWorkers()... -> loadSchematic() -> paste()...
 * @author James
 *
 */
public class SchematicFactory {

    private SchematicFile processedSchematic;
    
    private List<AbstractSchematicWorker> workers = new ArrayList<AbstractSchematicWorker>();

    /**
     * set the initial schematic file
     * @param file
     * @return
     */
    public SchematicFactory loadSchematic(SchematicFile file){
        this.processedSchematic = process(file);
        return this;

    }

    private SchematicFile process(SchematicFile file) {
        try{
        SchematicFile tmp = file.copy();
        
        for(AbstractSchematicWorker worker : workers){
            tmp = worker.modifySchematic(tmp);
        }
        return tmp;
        }
        catch(Exception e){
            System.out.println("An error occured while processing the schematic");
            e.printStackTrace();
            return null;
        }
       
    }

    /**
     * Add workers to a schematic
     * @param workers
     * @return
     */
    public SchematicFactory loadWorkers(AbstractSchematicWorker... workers){
        for(AbstractSchematicWorker worker : workers){
            this.workers.add(worker);
        }
        return this;
    }

    public SchematicFile getSchematic(){
        return processedSchematic;
    }
    
    public Object produce(IFactoryOuput output){
        return output.process(processedSchematic);
    }

}
