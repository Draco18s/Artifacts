package com.draco18s.artifacts.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.BlockCoverPlate;
import com.draco18s.artifacts.block.PseudoCoverplate;

public class RenderCoverPlate implements ISimpleBlockRenderingHandler {
	private int renderType;
	
	public RenderCoverPlate(int r) {
		renderType = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		renderer.renderBlockAsItem(PseudoCoverplate.instance, 0, 1.0F);
	}
	
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int l = world.getBlockMetadata(x, y, z);
		switch(l) {
	        case 2:
	        	z++;
	        	break;
	        case 3:
	        	z--;
	        	break;
	        case 4:
	        	x++;
	        	break;
	        case 5:
	        	x--;
	        	break;
	    }
		Block blockToCopy = world.getBlock(x, y, z);
		if(blockToCopy == null) {
			blockToCopy = Blocks.planks;
		}
		int meta = world.getBlockMetadata(x, y, z);
		IIcon camo = blockToCopy.getIcon(l, meta);
	    y--;
		renderer.renderBlockUsingTexture(blockToCopy, x, y, z, camo);
		renderer.renderBlockUsingTexture(Blocks.ice, x, y, z, block.getIcon(0, 0));
		//renderer.renderStandardBlock(Block.ice, x, y, z);
		return true;
	}

	/*@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		//renderer.renderBlockAllFaces(block, x, y-1, z);
        
		Tessellator tessellator = Tessellator.instance;
        int l = world.getBlockMetadata(x, y, z);
        Icon icon = block.getBlockTexture(world, x, y, z, 2);
        
        switch(l) {
	        case 2:
	        	z++;
	        	break;
	        case 3:
	        	z--;
	        	break;
	        case 4:
	        	x++;
	        	break;
	        case 5:
	        	x--;
	        	break;
        }
        y--;
        
        //System.out.println("getBright: " + block.getMixedBrightnessForBlock(world, x, y, z)); //only sun light?
        //System.out.println("getLight:  " + (int)(world.getLightBrightness(x, y, z) * 16777215)); //more accurate, causes alpha
        //System.out.println(">" + ((int)(world.getBrightness(x, y, z, l) * 16777215) & 11534591));
        int brightness = Block.blocksList[Block.stone.blockID].getMixedBrightnessForBlock(world, x, y, z);
        //int brightness = (int)(world.getLightBrightness(x, y, z) * 16777215);
        //int brightness = (int)(world.getBrightness(x, y, z, l) * 16777215);
        //int brightness = 16318463;
        //brightness = brightness | 15728640;
        //brightness = brightness & 15728895;//15728895
        //brightness = 15790080;
        //15728640 - 111100000000000000000000
        //14680064 - 111000000000000000000000
        //13631488 - 110100000000000000000000
        //12582912 - 110000000000000000000000
        //11534336 - 
        //10485760 - 
        // 9437184 - 
        // 8388608 - 100000000000000000000000
        
        //6835162 - 0110 1000 0100 1011 1101 1010
        //3011295 - 0010 1101 1111 0010 1101 1111
        //0110 0110 0000 0000 1101 1010
        //brig nusd --alpha-- --color--
        //1011 0000 0000 0000 1111 1111
        brightness = brightness & 11534591;
        //System.out.println(brightness);
        //int br = brightness >> 16 & 255;
        //int bg = (brightness >> 8) & 255;
        //int bb = brightness & 255;
        //br *= 0.8;
        //bg *= 0.8;
        //bb *= 0.8;
        //brightness = br << 16 + bg << 8 + bb;
        tessellator.setBrightness(brightness);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMinV();
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getMaxV();
        double d5 = (double)(x + 1);
        double d7 = (double)(x);
        double d9 = (double)(z);
        double d11 = (double)(z + 1);
        double d13 = (double)(y + 1);
        double d15 = (double)(y);

        tessellator.addVertexWithUV(d5, d13, d9, d0, d1);
        tessellator.addVertexWithUV(d5, d15, d9, d0, d3);
        tessellator.addVertexWithUV(d7, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d7, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d5, d13, d11, d0, d1);
        tessellator.addVertexWithUV(d5, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d5, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d7, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d7, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d7, d13, d11, d0, d1);

        tessellator.addVertexWithUV(d7, d13, d11, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d5, d15, d11, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d11, d2, d1);


        double f = 1F/256;
        icon = block.getIcon(0, 0);
        System.out.println("Icon: " + icon.getIconName());
        d0 = (double)icon.getMinU();
        d1 = (double)icon.getMinV();
        d2 = (double)icon.getMaxU();
        d3 = (double)icon.getMaxV();
        d5 = (double)(x + 1 + f);
        d7 = (double)(x - f);
        d9 = (double)(z - f);
        d11 = (double)(z + 1 + f);
        d13 = (double)(y + 1);
        d15 = (double)(y);

        tessellator.addVertexWithUV(d5, d13, d9, d0, d1);
        tessellator.addVertexWithUV(d5, d15, d9, d0, d3);
        tessellator.addVertexWithUV(d7, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d7, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d5, d13, d11, d0, d1);
        tessellator.addVertexWithUV(d5, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d5, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d7, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d7, d15, d9, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d7, d13, d11, d0, d1);

        tessellator.addVertexWithUV(d7, d13, d11, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d5, d15, d11, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d11, d2, d1);
		
		return false;
	}*/

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderType;
	}
}
