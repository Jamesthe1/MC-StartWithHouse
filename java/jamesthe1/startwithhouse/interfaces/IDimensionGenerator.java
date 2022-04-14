package jamesthe1.startwithhouse.interfaces;

import java.util.Random;

import net.minecraft.world.World;

public interface IDimensionGenerator {
	public void generateDimension (World world, Random rand, int chunkX, int chunkZ);
	public void refresh ();
}
