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
    public void rotateVector(int direction){
        int d[] = new int[4];
        d[0] =  z;
        d[1] =  x;
        d[2] = -z;
        d[3] = -x;
        setX(d[(direction+1)%4]);
        setZ(d[(direction)%4]);
    }

    /**
     * Rotates vector in 90 degree increments clockwise 
     * @param direction
     */
    /*public void rotateVectorAbs(int direction,SchVector limit){
        int d[] = new int[4];
        d[0] =  z;
        d[1] =  x;
        d[2] = - z;
        d[3] = - x;
        x = (d[(direction+1)%4]) % limit.x;
        z = (d[(direction)%4]) % limit.z;
        
        if(x < 0){
            x = (limit.x - 1) - x; 
        }
        
        if(z < 0){
            z = (limit.z - 1) - z; 
        }
    }*/

    public void add(SchVector v){
        x += v.x;
        y += v.y;
        z += v.z;
    }

}
