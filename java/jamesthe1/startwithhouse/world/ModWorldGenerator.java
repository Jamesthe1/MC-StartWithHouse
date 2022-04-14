package jamesthe1.startwithhouse.world;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import jamesthe1.startwithhouse.interfaces.IDimensionGenerator;

public class ModWorldGenerator extends WorldGenerator implements IWorldGenerator {
	public Map<Integer, IDimensionGenerator> generators = new TreeMap<Integer, IDimensionGenerator> ();

	@Override
	public boolean generate (World worldIn, Random rand, BlockPos position) {
		return false;
	}

	@Override
	public void generate (Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int dim = world.provider.getDimension ();
		boolean hasDim = generators.containsKey (dim);
		if (!hasDim)
			return;
		
		generators.get (dim).generateDimension (world, random, chunkX, chunkZ);
	}
	
	public void refresh () {
		for (Entry<Integer, IDimensionGenerator> entry : generators.entrySet ())
			entry.getValue ().refresh ();
	}
}
