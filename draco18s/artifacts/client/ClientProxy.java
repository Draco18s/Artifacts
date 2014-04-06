package draco18s.artifacts.client;

import hunternif.mc.atlas.api.AtlasAPI;
import hunternif.mc.atlas.api.TileAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import draco18s.artifacts.CommonProxy;
import draco18s.artifacts.GuiHandler;
import draco18s.artifacts.block.BlockLaserBeam;
import draco18s.artifacts.block.BlockLaserBeamSource;
import draco18s.artifacts.block.BlockPedestal;
import draco18s.artifacts.block.BlockSpikes;
import draco18s.artifacts.block.BlockTrap;
import draco18s.artifacts.block.BlockCoverPlate;
import draco18s.artifacts.block.BlockIllusionary;
import draco18s.artifacts.entity.EntityClayGolem;
import draco18s.artifacts.entity.EntitySpikes;
import draco18s.artifacts.entity.TileEntityDisplayPedestal;
import draco18s.artifacts.entity.TileEntitySword;
import draco18s.artifacts.entity.TileEntityTrap;
import draco18s.artifacts.item.ItemCalendar;

public class ClientProxy extends CommonProxy {
	public static TextureAtlasSprite calendar;
	
	@Override
	public void registerRenders() {
		int r = RenderingRegistry.getNextAvailableRenderId();
		ISimpleBlockRenderingHandler handler = new RenderFakeBlock(r);
		RenderingRegistry.registerBlockHandler(handler);
		((BlockIllusionary)BlockIllusionary.instance).renderType = r;
		GameRegistry.registerTileEntity(EntitySpikes.class, "draco18s.spike_trap");
		r = RenderingRegistry.getNextAvailableRenderId();
		handler = new RenderArrowTrap(r);
		RenderingRegistry.registerBlockHandler(handler);
		((BlockTrap)BlockTrap.instance).renderType = r;
		GameRegistry.registerTileEntity(TileEntityTrap.class, "TEtrap");
		r = RenderingRegistry.getNextAvailableRenderId();
		handler = new RenderCoverPlate(r);
		RenderingRegistry.registerBlockHandler(handler);
		((BlockCoverPlate)BlockCoverPlate.instance).renderType = r;
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySword.class, new TESwordRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityClayGolem.class, new RenderClayGolem());
        
        TileEntitySpecialRenderer render = new PedestalRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayPedestal.class, render);
        MinecraftForgeClient.registerItemRenderer(BlockPedestal.instance.blockID, new ItemRenderPedestal(render, new TileEntityDisplayPedestal()));
        
        render = new SpikesRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(EntitySpikes.class, render);
        MinecraftForgeClient.registerItemRenderer(BlockSpikes.instance.blockID, new ItemRenderPedestal(render, new EntitySpikes()));
        
        r = RenderingRegistry.getNextAvailableRenderId();
        handler = new RenderLaserBeam(r);
		RenderingRegistry.registerBlockHandler(handler);
		((BlockLaserBeam)BlockLaserBeam.instance).renderID = r;

        r = RenderingRegistry.getNextAvailableRenderId();
        handler = new RenderLaserSource(r);
		RenderingRegistry.registerBlockHandler(handler);
		((BlockLaserBeamSource)BlockLaserBeamSource.instance).renderID = r;
		
		if(Loader.isModLoaded("antiqueatlas")) {
			AtlasAPI.getTileAPI().setTexture("wizardtower", new ResourceLocation("artifacts:textures/gui/tower.png"));
		}
	}
}
