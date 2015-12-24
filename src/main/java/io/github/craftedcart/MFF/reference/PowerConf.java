package io.github.craftedcart.MFF.reference;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */

public final class PowerConf {

    public static final double powerCubeMaxPower = 100000000;
    public static final double ffProjectorMaxPower = 100000000;
    public static final double crystalRefineryMaxPower = 100000;
    public static final double crystalConstructorMaxPower = 100000;

    public static final double ffProjectorDrawRate = 100000000;
    public static final double crystalRefineryDrawRate = 10000;
    public static final double crystalConstructorDrawRate = 10000;

    public static final double ffProjectorUsagePerWallBlock = 0.21;
    public static final double ffProjectorUsagePerInnerBlock = 0.02;
    public static final double ffProjectorUsagePerBlockToGenerate = 20;
    public static final double crystalRefineryUsage = 99.12;
    public static final double crystalConstructorUsage = 16.32;

    public static final int crystalRefineryBaseTime = 2400; //120s (2400t)

    public static final int ffProjectorMaxDownTime = 100; //5s (100t) //The forcefield projector is allowed 5s without power before everything is reset

}
