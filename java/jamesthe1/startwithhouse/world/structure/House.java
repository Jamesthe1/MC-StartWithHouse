package jamesthe1.startwithhouse.world.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import jamesthe1.startwithhouse.Reference;
import jamesthe1.startwithhouse.interfaces.IStructureGenerator;

public class House implements IStructureGenerator {
	private static final int CHUNK_SIZE = 16;
	private static final int UPDATE_FLAGS = 1 | 2;
	
	private boolean itemsGenned = false;
	private Mirror mirror;
	private Rotation orient;
	private int yOff;
	
	@Override
	public Block[] getAllowedBlocks () {
		return new Block[] { Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.STONE, Blocks.GRAVEL, Blocks.SAND, Blocks.SANDSTONE };
	}

	@Override
	public int getMinimumHeight () {
		return 5;
	}
	
	private boolean posInChunk (BlockPos pos, ChunkPos chunk) {
		int xMax = chunk.x * CHUNK_SIZE;
		int zMax = chunk.z * CHUNK_SIZE;
		int xMin = xMax - CHUNK_SIZE;
		int zMin = zMax - CHUNK_SIZE;
		return (pos.getX () >= xMin && pos.getX () < xMax) && (pos.getZ () >= zMin && pos.getZ () < zMax);
	}
	
	private boolean structureNearChunk (BlockPos root, BlockPos lwh, ChunkPos chunk) {
		// Fast check
		if (posInChunk (root, chunk))
			return true;

		int xMax = chunk.x * CHUNK_SIZE;
		int zMax = chunk.z * CHUNK_SIZE;
		int xMin = xMax - CHUNK_SIZE;
		int zMin = zMax - CHUNK_SIZE;
		
		BlockPos blockEnd = root.add (lwh);
		boolean lower = xMin < blockEnd.getX () || xMax >= root.getX ();
		boolean upper = zMin < blockEnd.getZ () || zMax >= root.getZ ();
		return lower && upper;
	}
	
	public void refresh () {
		itemsGenned = false;
	}

	@Override
	public void generateStructure (World world, Random rand, int chunkX, int y, int chunkZ) {
		WorldServer worldServer = (WorldServer)world;
		MinecraftServer server = worldServer.getMinecraftServer ();
		TemplateManager mgr = worldServer.getStructureTemplateManager ();
		Template template = mgr.getTemplate (server, new ResourceLocation (Reference.MODID + ":house"));

		BlockPos pos = getSpawnPosition (world, rand);
		if (!itemsGenned) {
			itemsGenned = true;
			mirror = Mirror.values ()[rand.nextInt (3)];
			orient = Rotation.values ()[rand.nextInt (4)];
			yOff = y - pos.getY ();
		}
		
		// Only spawn house on spawn
		BlockPos size = template.getSize ();
		int xOff = mirror == Mirror.FRONT_BACK
				 ? 5
				 : -size.getX ();
		int zOff = mirror == Mirror.LEFT_RIGHT
				 ? size.getZ ()
				 : 0;
		BlockPos offset = (new BlockPos (xOff + 5, yOff, zOff - 4)).rotate (orient);
		size = size.rotate (orient);
		
		pos = pos.add (offset);
		ChunkPos chunk = new ChunkPos (chunkX, chunkZ);

		// We only care about relevant chunks
		if (!structureNearChunk (pos, size, chunk))
			return;
		
		IBlockState state = world.getBlockState (pos);
		world.notifyBlockUpdate (pos, state, state, UPDATE_FLAGS);
		PlacementSettings settings = (new PlacementSettings ()).setMirror (mirror).setRotation (orient)
									 .setChunk (chunk);
		template.addBlocksToWorld (worldServer, pos, settings);
		
		// Now to set chests
		Map<BlockPos, String> map = template.getDataBlocks (pos, settings);
		for (Entry<BlockPos, String> entry : map.entrySet ()) {
			if (!entry.getValue().equals ("chest"))
				continue;
			
			BlockPos chestPos = entry.getKey ().down ();
			world.setBlockState (chestPos.up (), Blocks.AIR.getDefaultState (), UPDATE_FLAGS);
			TileEntity te = worldServer.getTileEntity (chestPos);
			((TileEntityChest)te).setLootTable (LootTableList.CHESTS_SPAWN_BONUS_CHEST, rand.nextLong ());
		}
	}

	@Override
	public boolean hasCustomWorldPosition () {
		return true;
	}

	@Override
	public BlockPos getSpawnPosition (World world, Random rand) {
		return world.getSpawnPoint ();
	}

}
