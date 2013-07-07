package com.tehbeard.forge.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tehbeard.forge.schematic.extensions.SchematicExtension;


import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

/**
 * SchematicFile provides a way to read .schematic files
 * SchematicFile acts as a container for SchematicExtensions, which add additional non-standard information to a schematic.
 * Feature supported:
 * <li>Extended Block Ids (WorldEdit feature)</li>
 * <li>Schematic origin and offset (WorldEdit feature)</li>
 * <li>Layers support (Own feature)</li>
 * <li>Support for mod block rotation</li>
 * 
 * Planned features:
 * <li>FML ModItemData dictionary support for automatic block id translation</li>
 * <li>No paste air blocks</li>
 * <li>layer selection support</li>
 * <li>Entity support</li>
 * @author James
 *
 */
public class SchematicFile {

    //schematic size
    private short width = 0;
    private short height = 0;
    private short length = 0;

    //block data
    private int[] blocks;
    private byte[] blockData;



    //Complex NBT objects
    private final List<NBTTagCompound> tileEntities = new ArrayList<NBTTagCompound>();
    private final List<NBTTagCompound> entities = new ArrayList<NBTTagCompound>();


    private List<SchematicExtension> extensions = new ArrayList<SchematicExtension>();
    
    private SchVector initialVector = new SchVector();

    /**
     * Constructs an empty schematic, Not much use right now, will be when copying is implemented
     * @param width
     * @param height
     * @param length
     */
    public SchematicFile(short width,short height,short length){
        this.width = width;
        this.height = height;
        this.length = length;

        resetArrays();

    }

    /**
     * Loads a schematic 
     * @param is InputStream to schematic
     * @throws IOException
     */
    public SchematicFile(InputStream is) throws IOException{

        loadSchematic(is);
    }

    /**
     * Load schematic from file
     * @param file
     * @throws IOException
     */
    public SchematicFile(File file) throws IOException{

        loadSchematic(new FileInputStream(file));
    }

    /**
     * Resets the arrays
     */
    private void resetArrays(){
        int size = width*height*length;
        blocks = new int[size];
        blockData = new byte[size];
    }

    /**
     * loads the schematic data into memory
     * @throws IOException 
     */
    public void loadSchematic(InputStream is) throws IOException{
        NBTTagCompound tag  = CompressedStreamTools.readCompressed(is);

        if(!tag.getName().equalsIgnoreCase("schematic")){
            throw new IOException("File is not a valid schematic");
        }

        width = tag.getShort("Width");
        height = tag.getShort("Height");
        length = tag.getShort("Length");

        SchematicDataRegistry.logger().config("Schematic loaded, [" + width + ", " + height + ", " + length + "]");

        resetArrays();



        //read in block data; Vanilla lower byte array
        byte[] b_lower = tag.getByteArray("Blocks");


        byte[] addBlocks = new byte[0];
        //Check and load Additional blocks array
        if(tag.hasKey("AddBlocks")){
            SchematicDataRegistry.logger().config("Extended block data detected!");
            addBlocks = tag.getByteArray("AddBlocks");
        }


        for (int index = 0; index < b_lower.length; index++) {
            if ((index >> 1) >= addBlocks.length) { 
                blocks[index] = (short) (b_lower[index] & 0xFF);
            } else {
                if ((index & 1) == 0) {
                    blocks[index] = (short) (((addBlocks[index >> 1] & 0x0F) << 8) + (b_lower[index] & 0xFF));
                } else {
                    blocks[index] = (short) (((addBlocks[index >> 1] & 0xF0) << 4) + (b_lower[index] & 0xFF));
                }
            }
        }


        blockData = tag.getByteArray("Data");

        //load tileEntities
        NBTTagList tileEntityTag = tag.getTagList("TileEntities");

        for(int i =0;i<tileEntityTag.tagCount();i++){
            tileEntities.add((NBTTagCompound) tileEntityTag.tagAt(i));
        }
        NBTTagList entityTag = tag.getTagList("Entities");

        for(int i =0;i<entityTag.tagCount();i++){
            entities.add((NBTTagCompound) tileEntityTag.tagAt(i));
        }

        extensions = SchematicDataRegistry.getExtensions(tag, this);
    }

    public void saveSchematic(OutputStream is) throws IOException{
        //throw new UnsupportedOperationException("Not implemented in this version");

        NBTTagCompound tag = new NBTTagCompound("schematic");
        tag.setString("Materials", "Alpha");


        tag.setShort("Width",width);
        tag.setShort("Height",height);
        tag.setShort("Length",length);


        byte[] blocks_lower = new byte[blocks.length];
        byte[] blocks_upper = new byte[blocks.length];

        for (int index = 0; index < blocks.length; index++) {

            blocks_lower[index] = (byte) (blocks[index] & 0xFF);

            if ((index & 1) == 0) {
                //Shift >> 8 
                blocks_lower[index >> 1] = (byte) (blocks_lower[index >> 1] | ((byte)blocks[index] & 0xF00 >> 8)); 
            } else {
                //Shift >> 4 
                blocks_lower[index >> 1] = (byte) (blocks_lower[index >> 1] | ((byte)blocks[index] & 0xF00 >> 4));
            }

        }


        tag.setByteArray("Blocks",blocks_lower);
        tag.setByteArray("AddBlocks",blocks_upper);
        tag.setByteArray("Data",blockData);
        
        for(SchematicExtension ext : extensions){
            ext.onSave(tag, this);
        }


        //Save tile entities
        NBTTagList tel = new NBTTagList("TileEntities");
        for(NBTTagCompound te : tileEntities){
            tel.appendTag(te);
        }
        tag.setTag("TileEntities", tel);
        
        
        
        //save entities
        NBTTagList el = new NBTTagList("Entities");
        for(NBTTagCompound e : entities){
            el.appendTag(e);
        }
        
        tag.setTag("Entities", tel);
        
        
        
        CompressedStreamTools.writeCompressed(tag, is);//Save out
    }
    
    /**
     * get block id by vector
     * @param vector
     * @return
     */
    public int getBlockId(SchVector vector){
        return getBlockId(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Get the block id at a coordinate, This value may be 0-4096 (Includes extended block support)
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getBlockId(int x,int y,int z){

        int index =  (y * width * length) + (z * width) + x;

        if(index < 0 || index >= blocks.length){
            throw new IllegalStateException("Invalid coordinates for block get!");
        }

        return blocks[index];
    }
    
    public byte getBlockData(SchVector vector){
        return getBlockData(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Get the block data at a coordinate (0-15)
     * @param x
     * @param y
     * @param z
     * @return
     */
    public byte getBlockData(int x,int y,int z){

        int index =  y * width *length + z * width + x;
        if(index < 0 || index >= blockData.length){
            return 0;
        }
        return blockData[index];
    }
    
    public void setBlockId(SchVector v, int b_id) {
        setBlockId(v.getX(), v.getY(), v.getZ(), b_id);
    }

    /**
     * Set the block id at a coordinate (May not work, not tested with extended block support)
     * @param x
     * @param y
     * @param z
     * @param block
     */
    public void setBlockId(int x,int y,int z,int block){

        int index =  (y * width * length) + (z * width) + x;
        if(index < 0 || index >= blocks.length){
            throw new IllegalStateException("Invalid coordinates for block set! ["+ x +", "+ y +", "+z +"] " + block);
        }
        blocks[index] = block;
    }
    
    public void setBlockData(SchVector v, byte data) {
        setBlockData(v.getX(), v.getY(), v.getZ(), data);
    }

    /**
     * Set the block data at a coordinate
     * @param x
     * @param y
     * @param z
     * @param data
     */
    public void setBlockData(int x,int y,int z,byte data){

        int index =  y * width *length + z * width + x;
        if(index < 0 || index >= blockData.length){
            return;
        }
        blockData[index] = data;
    }

    /**
     * Size along x axis
     * @return
     */
    public final short getWidth() {
        return width;
    }

    /**
     * Size along y axis
     * @return
     */
    public final short getHeight() {
        return height;
    }

    /**
     * Size along z axis
     * @return
     */
    public final short getLength() {
        return length;
    }

    /**
     * Get all tile entities in this schematic
     * @return
     */
    public final List<NBTTagCompound> getTileEntities() {
        return tileEntities;
    }

    public final NBTTagCompound getTileEntityTagAt(SchVector vector){
        return getTileEntityTagAt(vector.getX(), vector.getY(), vector.getZ());
    }
    /**
     * Grab tile entity at location
     * @param x
     * @param y
     * @param z
     * @return tag for tile entity, or null for none found
     */
    public final NBTTagCompound getTileEntityTagAt(int x,int y, int z){

        for(NBTTagCompound tileEntity:  getTileEntities()){

            if( 
                    tileEntity.getInteger("x") == x &&
                    tileEntity.getInteger("y") == y &&
                    tileEntity.getInteger("z") == z 
                    ){
                return tileEntity;
            }
        }
        return null;
    }

    /**
     * Returns a tile entity object or null if none found
     * @param x
     * @param y
     * @param z
     * @return
     */
    public final TileEntity getTileEntityAt(int x,int y, int z){
        NBTTagCompound te = getTileEntityTagAt(x, y, z);
        if(te==null){return null;}
        return TileEntity.createAndLoadEntity(te);
    }

    /**
     * Return list of entity tags, not used currently
     * @return
     */
    public final List<NBTTagCompound> getEntities() {
        return entities;
    }

    @SuppressWarnings("unchecked")
    public <T extends SchematicExtension> T getExtension(Class<T> cl){
        for(SchematicExtension se : extensions){
            if(cl.isInstance(se)){
                return (T) se;
            }
        }

        return null;

    }

    public SchVector getInitialVector() {
        return initialVector;
    }

    
    public SchematicFile copy(){
        SchematicFile copy = new SchematicFile(width, height, length);
        
        copy.blocks    = Arrays.copyOf(blocks, blocks.length);
        copy.blockData = Arrays.copyOf(blockData, blockData.length);
        copy.initialVector.add(initialVector);
        
        for(NBTTagCompound t : entities){
            copy.entities.add((NBTTagCompound) t.copy());
        }
        
        for(NBTTagCompound t : tileEntities){
            copy.tileEntities.add((NBTTagCompound) t.copy());
        }
        
        for(SchematicExtension ex : extensions){
            copy.extensions.add(ex.copy(copy));
        }
        return copy;
    }


  
    
}

