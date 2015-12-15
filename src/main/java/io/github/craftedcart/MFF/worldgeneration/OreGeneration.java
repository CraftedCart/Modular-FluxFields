package io.github.craftedcart.MFF.worldgeneration;

import io.github.craftedcart.MFF.init.ModBlocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by CraftedCart on 26/11/2015 (DD/MM/YYYY)
 */

public class OreGeneration implements IWorldGenerator {

    private WorldGenerator genOreAmethyst;
    private WorldGenerator genOreRuby;

    public OreGeneration() {
        this.genOreAmethyst = new WorldGenMinable(ModBlocks.oreAmethyst.getDefaultState(), 8); //Up to 8 in a vein
        this.genOreRuby = new WorldGenMinable(ModBlocks.oreRuby.getDefaultState(), 4); //Up to 4 in a vein
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimensionId()) {
            case 0: //Overworld
                this.runGenerator(genOreAmethyst, world, random, chunkX, chunkZ, 10, 0, 32);
                this.runGenerator(genOreRuby, world, random, chunkX, chunkZ, 2, 0, 20);
                break;
            case -1: //Nether
                break;
            case 1: //End
                break;
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i ++) {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }

}
