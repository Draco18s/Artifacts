package com.draco18s.artifacts;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
	public void registerRenders() {

	}
	
	public void registerTickHandlers() {
		
		FMLCommonHandler.instance().bus().register(new ArtifactTickHandler());
		
	}
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new ArtifactServerEventHandler());
	}
}
