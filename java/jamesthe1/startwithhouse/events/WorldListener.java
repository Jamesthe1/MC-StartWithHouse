package jamesthe1.startwithhouse.events;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import jamesthe1.startwithhouse.world.ModWorldGenerator;

public class WorldListener {
	public ModWorldGenerator worldGen;
	
	@SubscribeEvent
	public void unloadWorld (WorldEvent.Unload event) {
		worldGen.refresh ();
	}
}
