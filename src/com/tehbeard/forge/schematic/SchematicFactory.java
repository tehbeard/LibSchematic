package com.tehbeard.forge.schematic;

import java.util.ArrayList;
import java.util.List;

import com.tehbeard.forge.schematic.product.IFactoryOuput;
import com.tehbeard.forge.schematic.worker.AbstractSchematicWorker;

/**
 * <p>A SchematicFactory processes {@link SchematicFile}s , passing them to multiple {@link AbstractSchematicWorker}s.<br/>  
 * These workers manipulate a Schematic, such as changing the owner of a block, removing blocks or rotating a schematic.<br/>
 * The schematic passed into a SchematicFactory is copied before use, preserving the value of the original, additionally {@link AbstractSchematicWorker}s<br/>
 * may generate new a {@link SchematicFile} and pass this along the chain (an example would be the rotation worker, which writes a new schematic based on<br/>
 * rotating the old one.</p>
 * <p>The lifecycle of a SchematicFactory is as follows:<br/>
 * new() -> loadWorkers()... -> loadSchematic() -> produce({@link IFactoryOuput})<br/>
 * Alternatively, in a scenario where you apply the same workers to multiple schematics:<br/>
 * <pre>
 * factory = new() -> loadWorkers()...;
 * ...
 * factory.loadSchematic();
 * produce({@link IFactoryOuput})
 * ...
 * </pre>
 * @author James
 *
 */
public class SchematicFactory {

    private SchematicFile processedSchematic;
    
    private List<AbstractSchematicWorker> workers = new ArrayList<AbstractSchematicWorker>();

    /**
     * Load and process a {@link SchematicFile}
     * @param file {@link SchematicFile} to process
     * @return this {@link SchematicFactory} to allow method linking
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
     * Add workers to this SchematicFactory
     * @param workers {@link AbstractSchematicWorker}s to add
     * @return this {@link SchematicFactory} to allow method linking
     */
    public SchematicFactory loadWorkers(AbstractSchematicWorker... workers){
        for(AbstractSchematicWorker worker : workers){
            this.workers.add(worker);
        }
        return this;
    }

    /**
     * Returns the processed {@link SchematicFile} or null if none exists
     * @return
     */
    public SchematicFile getSchematic(){
        return processedSchematic;
    }
    
    /**
     * Passes the output of this factory to a {@link IFactoryOuput}
     * @param output {@link IFactoryOuput}
     * @return result of {@link IFactoryOuput}
     */
    public Object produce(IFactoryOuput output){
        return output.process(getSchematic());
    }

}
