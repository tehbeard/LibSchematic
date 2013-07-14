package com.tehbeard.forge.schematic;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Schematic vector, used to do math needed for making a composite vector based on the world vector, offset vector and rotation
 *
 */
public class SchVector {

    @Override
    public String toString() {
        return "" + x + ", " + y + ", " + z + "";
    }

    private int x,y,z;
    
    public SchVector(NBTTagCompound t){
        this(t.getInteger("x"),t.getInteger("y"),t.getInteger("z"));
    }
    
    public SchVector(SchVector initial){
        this();
        add(initial);
    }

    public SchVector(){
        this(0,0,0);
    }

    public SchVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Rotates vector in 90 degree increments clockwise 
     * @param direction
     */
    public SchVector rotateVector(int direction){
        int d[] = new int[4];
        d[0] =  z;
        d[1] =  x;
        d[2] = -z;
        d[3] = -x;
        x = (d[(direction+1)%4]);
        z = (d[(direction)%4]);
        
        return this;
    }

    public SchVector add(SchVector v){
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }
    
    public SchVector sub(SchVector v){
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }
    
    public static SchVector min(SchVector v1,SchVector v2){
        return new SchVector(
                Math.min(v1.x, v2.x),
                Math.min(v1.y, v2.y),
                Math.min(v1.z, v2.z)
                );
    }
    
    public static SchVector max(SchVector v1,SchVector v2){
        return new SchVector(
                Math.max(v1.x, v2.x),
                Math.max(v1.y, v2.y),
                Math.max(v1.z, v2.z)
                );
    }
    
    public NBTTagCompound asTag(){
        NBTTagCompound t = new NBTTagCompound();
        t.setInteger("x", x);
        t.setInteger("y", y);
        t.setInteger("z", z);
        return t;
        
    }
    public int area(){
        return x * y * z; 
    }
}
