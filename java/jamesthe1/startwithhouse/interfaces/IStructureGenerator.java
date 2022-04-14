package jamesthe1.startwithhouse.interfaces;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStructureGenerator {
	public boolean hasCustomWorldPosition ();
	public BlockPos getSpawnPosition (World world, Random rand);
	public Block[] getAllowedBlocks ();
	public int getMinimumHeight ();
	public void refresh ();
	public void generateStructure (World world, Random rand, int chunkX, int y, int chunkZ);
}
