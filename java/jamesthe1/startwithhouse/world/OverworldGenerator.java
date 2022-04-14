package jamesthe1.startwithhouse.world;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import jamesthe1.startwithhouse.interfaces.IDimensionGenerator;
import jamesthe1.startwithhouse.interfaces.IStructureGenerator;

public class OverworldGenerator implements IDimensionGenerator {
	public Map<String, IStructureGenerator> structures = new TreeMap<String, IStructureGenerator> ();
	
	private static int getGroundFromAbove (IStructureGenerator structure, World world, int x, int z) {
		int y = 255;
		boolean isLegal = false;
		while (!isLegal && y-- >= structure.getMinimumHeight ()) {
			Block block = world.getBlockState (new BlockPos (x, y, z)).getBlock ();
			for (Block allowed : structure.getAllowedBlocks ())
				isLegal |= block == allowed;
		}
		
		return y;
	}
	
	@Override
	public void generateDimension (World world, Random rand, int chunkX, int chunkZ) {
		for (Entry<String, IStructureGenerator> entry : structures.entrySet ()) {
			IStructureGenerator structure = entry.getValue ();
			
			BlockPos spawn = structure.hasCustomWorldPosition ()
						   ? structure.getSpawnPosition (world, rand)
						   : new BlockPos (chunkX * 16 + 8, 0, chunkZ * 16 + 8);
			int y = getGroundFromAbove (structure, world, spawn.getX (), spawn.getZ ());
			structure.generateStructure (world, rand, chunkX, y, chunkZ);
		}
	}

	@Override
	public void refresh () {
		for (Entry<String, IStructureGenerator> entry : structures.entrySet ())
			entry.getValue ().refresh ();
	}
}
