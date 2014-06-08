package com.draco18s.artifacts.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.BlockIllusionary;
import com.draco18s.artifacts.block.PseudoBlockIllusionary;

public class RenderFakeBlock implements ISimpleBlockRenderingHandler {
	int renderType = 0;

	public RenderFakeBlock(int r) {
		renderType = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		renderer.renderBlockAsItem(PseudoBlockIllusionary.instance, 3, 1.0F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		float f = 0.03125F;
		float xn = 0F;
		float xp = 1F;
		float yn = 0F;
		float yp = 1F;
		float zn = 0F;
		float zp = 1F;
		if(!world.getBlock(x-1, y, z).isOpaqueCube()) {
			if(world.getBlock(x-1, y, z) != block)
				xn = f;
		}
		if(!world.getBlock(x+1, y, z).isOpaqueCube()) {
			if(world.getBlock(x+1, y, z) != block)
				xp = 1F-f;
		}
		if(!world.getBlock(x, y-1, z).isOpaqueCube()) {
			if(world.getBlock(x, y-1, z) != block)
				yn = f;
		}
		if(!world.getBlock(x, y+1, z).isOpaqueCube()) {
			if(world.getBlock(x, y+1, z) != block)
				yp = 1F-f;
		}
		if(!world.getBlock(x, y, z-1).isOpaqueCube()) {
			if(world.getBlock(x, y, z-1) != block)
				zn = f;
		}
		if(!world.getBlock(x, y, z+1).isOpaqueCube()) {
			if(world.getBlock(x, y, z+1) != block)
				zp = 1F-f;
		}
		//block.setBlockBounds(xn, yn, zn, xp, yp, zp);
		renderer.setRenderBounds(xn, yn, zn, xp, yp, zp);
		
		int l = block.colorMultiplier(world, x, y, z);
        float f0 = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f0 = f3;
            f1 = f4;
            f2 = f5;
        }
        
        //if() {
        /*f0 *= 1.5;
        f1 *= 1.5;
        f2 *= 1.5;*/
        //}

        return renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, f0, f1, f2);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderType;
	}
}
