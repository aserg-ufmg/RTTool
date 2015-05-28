/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans;

/**
 *
 * @author fernando
 */
public class CoordXY {
     private float x,y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    public CoordXY setXY(float x , float y) {
        this.y = y;
        this.x = x;
        return this;
    }
}
