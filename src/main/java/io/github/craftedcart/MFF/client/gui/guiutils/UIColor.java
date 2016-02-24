package io.github.craftedcart.MFF.client.gui.guiutils;

/**
 * Created by CraftedCart on 29/12/2015 (DD/MM/YYYY)
 */

public class UIColor {

    public double r;
    public double g;
    public double b;
    public double a = 1;

    public UIColor(double r, double g, double b) {
        this.r = r / 255;
        this.g = g / 255;
        this.b = b / 255;
    }

    public UIColor(double r, double g, double b, double a) {
        this.r = r / 255;
        this.g = g / 255;
        this.b = b / 255;
        this.a = a;
    }

    public static UIColor transparent() {
        return new UIColor(0, 0, 0, 0);
    }

    public static UIColor pureWhite(double a) {
        return new UIColor(255, 255, 255, a);
    }

    public static UIColor pureWhite() {
        return pureWhite(1);
    }

    public static UIColor matBlue(double a) { //Short for Material Blue
        return new UIColor(33, 150, 243, a);
    }

    public static UIColor matBlue() {
        return matBlue(1);
    }

    public static UIColor matBlue700(double a) { //Short for Material Blue
        return new UIColor(25, 118, 210, a);
    }

    public static UIColor matBlue700() {
        return matBlue700(1);
    }

    public static UIColor matWhite(double a) {
        return new UIColor(250, 250, 250, a);
    }

    public static UIColor matWhite() {
        return matWhite(1);
    }

    public static UIColor matGrey300(double a) {
        return new UIColor(244, 244, 244, a);
    }

    public static UIColor matGrey300() {
        return matGrey300(1);
    }

    public static UIColor matGrey900(double a) {
        return new UIColor(33, 33, 33, a);
    }

    public static UIColor matGrey900() {
        return matGrey900(1);
    }

    public static UIColor matBlueGrey(double a) {
        return new UIColor(96, 125, 139, a);
    }

    public static UIColor matBlueGrey() {
        return matBlueGrey(1);
    }

    public static UIColor matBlueGrey300(double a) {
        return new UIColor(144, 164, 174, a);
    }

    public static UIColor matBlueGrey300() {
        return matBlueGrey300(1);
    }

    public static UIColor matBlueGrey700(double a) {
        return new UIColor(69, 90, 100, a);
    }

    public static UIColor matBlueGrey700() {
        return matBlueGrey700(1);
    }

    public static UIColor matRed(double a) {
        return new UIColor(244, 67, 54, a);
    }

    public static UIColor matRed() {
        return matRed(1);
    }

    public static UIColor matOrange(double a) { //Short for Material Orange
        return new UIColor(255, 152, 0, a);
    }

    public static UIColor matOrange() {
        return matOrange(1);
    }

    public static UIColor matGreen(double a) { //Short for Material Green
        return new UIColor(76, 175, 80, a);
    }

    public static UIColor matGreen() {
        return matGreen(1);
    }

}
