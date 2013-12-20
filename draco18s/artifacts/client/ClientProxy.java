package draco18s.artifacts.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import draco18s.artifacts.CommonProxy;
import draco18s.artifacts.GuiHandler;
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

public class ClientProxy extends CommonProxy {
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
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySword.class, new TESwordRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityClayGolem.class, new RenderClayGolem());
        
        TileEntitySpecialRenderer render = new PedestalRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayPedestal.class, render);
        MinecraftForgeClient.registerItemRenderer(BlockPedestal.instance.blockID, new ItemRenderPedestal(render, new TileEntityDisplayPedestal()));
        
        render = new SpikesRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(EntitySpikes.class, render);
        MinecraftForgeClient.registerItemRenderer(BlockSpikes.instance.blockID, new ItemRenderPedestal(render, new EntitySpikes()));
	}
}
