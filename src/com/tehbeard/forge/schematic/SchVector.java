package com.tehbeard.forge.schematic;

/**
 * Schematic vector, used to do math needed for making a composite vector based on the world vector, offset vector and rotation
 *
 */
public class SchVector {

    @Override
    public String toString() {
        return "SchVector [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

    int x,y,z;
    
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

}
