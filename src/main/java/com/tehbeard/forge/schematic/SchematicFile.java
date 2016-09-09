package com.tehbeard.forge.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.tehbeard.forge.schematic.extensions.id.IdTranslateExtension;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.*;

import com.tehbeard.forge.schematic.extensions.SchematicExtension;
import com.tehbeard.forge.schematic.factory.worker.AbstractSchematicWorker;

/**
 * SchematicFile provides a way to read/write .schematic files. <br/>
 * SchematicFile acts as a container for {@link SchematicExtension}s, which add
 * additional non-standard information to a schematic.<br/>
 * Extensions supported currently:
 * <ul>
 * <li>Schematic origin and offset (WorldEdit feature) - access to the vectors
 * WorldEdit stores with .schematic files</li>
 * <li>metadata tags (own feature) - Add arbitrary flags to a .schematic (e.g.
 * could be used to classify a .schematic's type for worldgen?)</li>
 * <li>blockId <-> name dictionary to make .schematic portable across different
 * configurations (read: different block ids for the same block)</li>
 * <p>
 * {@link SchematicExtension}s can be extended by mods to provide new kinds of
 * metadata with a {@link SchematicFile}
 * </p>
 * 
 * @author James
 * 
 */
public class SchematicFile {

    // schematic size
    private short width = 0;
    private short height = 0;
    private short length = 0;

    // block data
    private int[] blocks;
    private byte[] blockData;

    // Complex NBT objects
    private final List<NBTTagCompound> tileEntities = new ArrayList<NBTTagCompound>();
    private final List<NBTTagCompound> entities = new ArrayList<NBTTagCompound>();

    //Schematic extensions
    private List<SchematicExtension> extensions = new ArrayList<SchematicExtension>();

    private SchVector initialVector = new SchVector(0,0,0);

    /**
     * Constructs an empty schematic, Not much use right now, will be when
     * copying is implemented
     * 
     * @param width
     *            size along x axis
     * @param height
     *            size along y axis
     * @param length
     *            size along z axis
     */
    public SchematicFile(short width, short height, short length) {
        this.width = width;
        this.height = height;
        this.length = length;

        resetArrays();

    }

    /**
     * Load schematic from a resource on the classpath
     * 
     * @param name
     *            full path on class path, with starting /
     * @throws IOException
     */
    public SchematicFile(String name) throws IOException {
        this(SchematicFile.class.getResourceAsStream(name));
    }

    /**
     * Loads a schematic from an inputstream
     * 
     * @param is
     * @throws IOException
     */
    public SchematicFile(InputStream is) throws IOException {
        this(CompressedStreamTools.readCompressed(is));
    }

    /**
     * Load schematic from file
     * 
     * @param file
     * @throws IOException
     */
    public SchematicFile(File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Resets the arrays
     */
    private void resetArrays() {
        int size = width * height * length;
        blocks = new int[size];
        blockData = new byte[size];
    }

    /**
     * loads the schematic from an {@link NBTTagCompound}
     * 
     * @throws IOException
     */
    public SchematicFile(NBTTagCompound tag) throws IOException {

        //TODO - No more getName(), how to validate schematic now?
//        if (!tag.getName().equalsIgnoreCase("schematic"))
//            throw new IOException("File is not a valid schematic");

        width = tag.getShort("Width");
        height = tag.getShort("Height");
        length = tag.getShort("Length");

        String mats = tag.getString("Materials");

        SchematicDataRegistry.logger().debug(
                "Schematic loaded, [" + width + ", " + height + ", " + length
                        + "]");

        resetArrays();

        // read in block data; Vanilla lower byte array
        byte[] b_lower = tag.getByteArray("Blocks");

        byte[] addBlocks = new byte[0];
        // Check and load Additional blocks array
        if (tag.hasKey("AddBlocks")) {
            SchematicDataRegistry.logger().debug(
                    "Extended block data detected!");
            addBlocks = tag.getByteArray("AddBlocks");
        }

        for (int index = 0; index < b_lower.length; index++) {
            if (index >> 1 >= addBlocks.length) {
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

        // load tileEntities
        NBTTagList tileEntityTag = tag.getTagList("TileEntities",10);

        for (int i = 0; i < tileEntityTag.tagCount(); i++) {
            tileEntities.add(tileEntityTag.getCompoundTagAt(i)); 
        }
        NBTTagList entityTag = tag.getTagList("Entities",10);

        for (int i = 0; i < entityTag.tagCount(); i++) {
            entities.add(entityTag.getCompoundTagAt(i)); //tagAt
        }

        extensions = SchematicDataRegistry.getExtensions(tag, this);

        SchematicDataRegistry.logger().info("Block array of file: " + Arrays.toString(blocks));
        SchematicDataRegistry.logger().info("Some kind of material... " + mats);
    }

    /**
     * Save schematic to a stream
     * 
     * @param is
     * @throws IOException
     */
    public void saveSchematic(OutputStream is) throws IOException {
        CompressedStreamTools.writeCompressed(saveSchematicToTag(), is);
    }

    /**
     * Save schematic to a {@link NBTTagCompound}
     * 
     * @return
     * @throws IOException
     */
    public NBTTagCompound saveSchematicToTag() throws IOException {

        NBTTagCompound tag = new NBTTagCompound(); // No Schematic name added
        tag.setString("Materials", "Alpha");

        tag.setShort("Width", width);
        tag.setShort("Height", height);
        tag.setShort("Length", length);

        byte[] blocks_lower = new byte[blocks.length];
        double d = blocks.length / 2D;
        byte[] blocks_upper = new byte[(int) Math.ceil(d)];

        for (int index = 0; index < blocks.length; index++) {

            blocks_lower[index] = (byte) (blocks[index] & 0xFF);

            byte upperbits = (byte) ((blocks[index] & 0xF00) >> 8);

            if ((index & 1) == 0) {
                // Shift >> 8
                blocks_upper[index >> 1] = (byte) (blocks_upper[index >> 1] | upperbits);
            } else {
                // Shift >> 4
                blocks_upper[index >> 1] = (byte) (blocks_upper[index >> 1] | upperbits << 4);
            }

        }

        tag.setByteArray("Blocks", blocks_lower);
        boolean tripAddBlocks = false;
        for (byte b : blocks_upper) {
            if (b > 0) {
                tripAddBlocks = true;
                break;
            }
        }

        if (tripAddBlocks) {
            tag.setByteArray("AddBlocks", blocks_upper);
        }

        tag.setByteArray("Data", blockData);

        for (SchematicExtension ext : extensions) {
            ext.onSave(tag, this);
        }

        // Save tile entities
        NBTTagList tel = new NBTTagList();
        for (NBTTagCompound te : tileEntities) {
            tel.appendTag(te);
        }
        if (tel.tagCount() > 0) {
            tag.setTag("TileEntities", tel);
        }

        // save entities
        NBTTagList el = new NBTTagList();
        for (NBTTagCompound e : entities) {
            el.appendTag(e);
        }
        if (el.tagCount() > 0) {
            tag.setTag("Entities", el);
        }

        return tag;
    }

    /**
     * get block id by vector
     * 
     * @param vector
     * @return
     */
    public int getBlockId(SchVector vector) {
        return getBlockId(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Get the block id at a coordinate, This value may be 0-4096 (Includes
     * extended block support)
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getBlockId(int x, int y, int z) {

        int index = y * width * length + z * width + x;

        if (index < 0 || index >= blocks.length)
            throw new IllegalStateException("Invalid coordinates for block get! [" + x + ", " + y + ", " + z + "]");

        SchematicDataRegistry.logger().debug(String.format(
                "We've translated location %d/%d/%d to index %d in the blocks array, which is %d",
                x, y, z, index, blocks[index]
        ));

        return blocks[index];
    }

    /**
     * Get the block data by vector
     * 
     * @param vector
     * @return
     */
    public byte getBlockData(SchVector vector) {
        return getBlockData(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Get the block data at a coordinate (0-15)
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public byte getBlockData(int x, int y, int z) {

        int index = y * width * length + z * width + x;
        if (index < 0 || index >= blockData.length)
            return 0;
        return blockData[index];
    }

    /**
     * Set the block id at vector
     * 
     * @param v
     * @param b_id
     * @param b_ns
     */
    public void setBlock(SchVector v, int b_id, String b_ns) {
        setBlock(v.getX(), v.getY(), v.getZ(), b_id, b_ns);
    }

    /**
     * Set the block id at a coordinate
     * 
     * @param x
     * @param y
     * @param z
     * @param blockId
     * @param blockNamespace
     */
    public void setBlock(int x, int y, int z, int blockId, String blockNamespace) {

        int index = y * width * length + z * width + x;
        if (index < 0 || index >= blocks.length)
            throw new IllegalStateException(
                    "Invalid coordinates for block set! [" + x + ", " + y
                            + ", " + z + "] " + blockId + " / sch : [" + width
                            + ", " + height + ", " + length + "]");
        blocks[index] = blockId;
    }

    /**
     * Set the block data at vector
     * 
     * @param v
     * @param data
     */
    public void setBlockData(SchVector v, byte data) {
        setBlockData(v.getX(), v.getY(), v.getZ(), data);
    }

    /**
     * Set the block data at a coordinate
     * 
     * @param x
     * @param y
     * @param z
     * @param data
     */
    public void setBlockData(int x, int y, int z, byte data) {

        int index = y * width * length + z * width + x;
        if (index < 0 || index >= blockData.length)
            return;
        blockData[index] = data;
    }

    /**
     * Size along x axis
     * 
     * @return
     */
    public final short getWidth() {
        return width;
    }

    /**
     * Size along y axis
     * 
     * @return
     */
    public final short getHeight() {
        return height;
    }

    /**
     * Size along z axis
     * 
     * @return
     */
    public final short getLength() {
        return length;
    }

    /**
     * Get all tile entities in this schematic
     * 
     * @return
     */
    public final List<NBTTagCompound> getTileEntities() {
        return tileEntities;
    }

    /**
     * Get the {@link NBTTagCompound} for a tile entity at this vector
     * 
     * @param vector
     * @return
     */
    public final NBTTagCompound getTileEntityTagAt(SchVector vector) {
        return getTileEntityTagAt(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Grab {@link NBTTagCompound} for a tile entity at these coordinates
     * 
     * @param x
     * @param y
     * @param z
     * @return tag for tile entity, or null for none found
     */
    public final NBTTagCompound getTileEntityTagAt(int x, int y, int z) {

        for (NBTTagCompound tileEntity : getTileEntities()) {

            if (tileEntity.getInteger("x") == x
                    && tileEntity.getInteger("y") == y
                    && tileEntity.getInteger("z") == z)
                return tileEntity;
        }
        return null;
    }

    /**
     * Return list of entity tags, not used currently
     * 
     * @return
     */
    public final List<NBTTagCompound> getEntities() {
        return entities;
    }

    @SuppressWarnings("unchecked")
    public <T extends SchematicExtension> T getExtension(Class<T> cl) {
        for (SchematicExtension se : extensions) {
            if (cl.isInstance(se))
                return (T) se;
        }

        return null;

    }

    /**
     * Adds a SchematicExtension to this Schematic
     * @param extension 
     */
    public void addExtension(SchematicExtension extension) {
        extensions.add(extension);
    }

    /**
     * Returns the initial vector for a schematic, this vector allows
     * {@link AbstractSchematicWorker}s to modify the relative final position of
     * a schematic, such as with WorldEdit's offset vector
     * 
     * @return
     */
    public SchVector getInitialVector() {
        return initialVector;
    }

    /**
     * Creates a duplicate of this {@link SchematicFile}
     * 
     * @return
     */
    public SchematicFile copy() {
        SchematicFile copy = new SchematicFile(width, height, length);

        copy.blocks = Arrays.copyOf(blocks, blocks.length);
        copy.blockData = Arrays.copyOf(blockData, blockData.length);
        copy.initialVector.add(initialVector);

        for (NBTTagCompound t : entities) {
            copy.entities.add((NBTTagCompound) t.copy());
        }

        for (NBTTagCompound t : tileEntities) {
            copy.tileEntities.add((NBTTagCompound) t.copy());
        }

        for (SchematicExtension ex : extensions) {
            copy.extensions.add(ex.copy(copy));
        }
        return copy;
    }

    /**
     * Prepare the IdTranslator for loading in this schematic by
     * putting in all the blocks found within this schematic to
     * the IdTranslator Schematic section
     *
     * @param translator The translator for which will be used to
     *                   verify all the blocks within the schematic
     */
    public void prepareToLoad(IdTranslateExtension translator) {

        //For every block in the schematic...
        for (int y=0; y<getHeight(); ++y) {
            for (int x=0; x<getWidth(); ++x) {
                for (int z=0; z<getLength(); ++z) {

                    //Get each block
                    int _id = getBlockId(x, y, z);
                    Block block = Block.getBlockById(_id);

                    //Now add our block to the schematics registry so that it's
                    // listed in preparation for reloading the cache
                    translator.addSchematicBlock(GameData.getBlockRegistry().getNameForObject(block), _id);

                }
            }
        }

    }

}