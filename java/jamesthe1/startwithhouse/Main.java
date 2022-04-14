package jamesthe1.startwithhouse;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import jamesthe1.startwithhouse.events.WorldListener;
import jamesthe1.startwithhouse.proxy.CommonProxy;
import jamesthe1.startwithhouse.world.ModWorldGenerator;
import jamesthe1.startwithhouse.world.OverworldGenerator;
import jamesthe1.startwithhouse.world.structure.House;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;
	
	private static ModWorldGenerator worldGen;
	
	@EventHandler
	public static void preInit (FMLPreInitializationEvent event) {
		worldGen = new ModWorldGenerator ();
		OverworldGenerator owGen = new OverworldGenerator ();
		owGen.structures.put ("house", new House ());
		worldGen.generators.put (0, owGen);
		GameRegistry.registerWorldGenerator (worldGen, 0);
	}
	
	@EventHandler
	public static void init (FMLInitializationEvent event) {
		WorldListener worldListener = new WorldListener ();
		worldListener.worldGen = worldGen;
		MinecraftForge.EVENT_BUS.register (worldListener);
	}
	
	@EventHandler
	public static void postInit (FMLPostInitializationEvent event) {
		
	}
}
