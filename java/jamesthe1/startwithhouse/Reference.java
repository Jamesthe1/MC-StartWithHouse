package jamesthe1.startwithhouse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
	public static final String MODID = "startwithhouse";
	public static final String NAME = "Start With House";
	public static final String VERSION = "1.0";
	public static final String PACKAGE = "jamesthe1." + MODID;
	
	public static final String PROXIES = PACKAGE + ".proxy";
	public static final String CLIENT = PROXIES + ".ClientProxy";
	public static final String COMMON = PROXIES + ".CommonProxy";
	
	public static final Logger LOGGER = LogManager.getLogger ();
}
