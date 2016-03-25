package io.github.craftedcart.modularfluxfields.client.gui.guiutils;

/**
 * Created by CraftedCart on 25/01/2015 (DD/MM/YYYY)
 */
public class PosXY {

    protected double x;
    protected double y;

    public PosXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected PosXY add(double x, double y) {
        return new PosXY(this.x + x, this.y + y);
    }

}
