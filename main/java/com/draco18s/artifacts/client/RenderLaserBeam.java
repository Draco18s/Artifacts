package com.draco18s.artifacts.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import com.draco18s.artifacts.block.BlockLaserBeam;

public class RenderLaserBeam implements ISimpleBlockRenderingHandler {

	public int renderID = 0;

	public RenderLaserBeam(int r) {
		renderID = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
        int l = world.getBlockMetadata(x, y, z);
        IIcon icon = renderer.getBlockIconFromSide(block, l&1);
        boolean flag = (l & 4) == 4;
        boolean flag1 = (l & 2) == 2;
        /*if (this.hasOverrideBlockTexture())
        {
            icon = this.overrideBlockTexture;
        }*/

		tessellator.setBrightness(15728880);
        //float f = block.getBlockBrightness(world, x, y, z) * 0.75F;
		float f = 0.75f;
        tessellator.setColorOpaque_F(f, f, f);
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getInterpolatedV(flag ? 2.0D : 0.0D);
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getInterpolatedV(flag ? 4.0D : 2.0D);
        double d4 = 0.5D;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean flag5 = false;
        if(world.getBlock(x, y, z) != block) {
            flag2 = BlockLaserBeam.func_72148_a(world, x, y, z, 0, 1);
            flag3 = BlockLaserBeam.func_72148_a(world, x, y, z, 0, 3);
            flag4 = BlockLaserBeam.func_72148_a(world, x, y, z, 0, 2);
            flag5 = BlockLaserBeam.func_72148_a(world, x, y, z, 0, 0);
            if(flag2 && (l&3) == 1)
            	flag3 = true;
            else
            	flag2 = false;
            if(flag3 && (l&3) == 3)
            	flag2 = true;
            else
            	flag3 = false;
            if(flag4 && (l&3) == 2)
            	flag5 = true;
            else
            	flag4 = false;
            if(flag5 && (l&3) == 0)
            	flag4 = true;
            else
            	flag5 = false;
            if (!flag4 && !flag3 && !flag5 && !flag2) {
            	switch(l&3) {
            		case 0:
            		case 2:
            			flag4 = flag5 = true;
            			break;
            		case 1:
            		case 3:
            			flag2 = flag3 = true;
            			break;
            	}
            }
        }
        else {
        	/*flag2 = BlockLaserBeam.func_72148_a(world, x, y, z, l, 1);
            flag3 = BlockLaserBeam.func_72148_a(world, x, y, z, l, 3);
            flag4 = BlockLaserBeam.func_72148_a(world, x, y, z, l, 2);
            flag5 = BlockLaserBeam.func_72148_a(world, x, y, z, l, 0);*/
        	double bb = block.getBlockBoundsMinZ() - block.getBlockBoundsMinX();
        	if(bb == 0) {
        		//both
        		flag2 = flag3 = flag4 = flag5 = true;
        	}
        	else if(bb > 0) {
        		//x-axis;
        		flag2 = flag3 = true;
        	}
        	else {
        		//return true;
        		flag4 = flag5 = true;
        		//z-axis;
        	}
        }
        float f1 = 0.03125F;
        float f2 = 0.5F - f1 / 2.0F;
        float f3 = f2 + f1;

        if (!flag4 && !flag2)
        {
        	return true;
        }

        if (flag4)
        {
            tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4, (double)z + 1D, d0, d1);
            tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4, (double)z + 1D, d0, d3);
            tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4, (double)z, d2, d3);
            tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4, (double)z, d2, d1);
            tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4, (double)z, d2, d1);
            tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4, (double)z, d2, d3);
            tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4, (double)z + 1D, d0, d3);
            tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4, (double)z + 1D, d0, d1);
            if(!world.getBlock(x, y, z-1).isOpaqueCube() && !world.isAirBlock(x, y, z-1) && world.getBlock(x, y, z-1) != BlockLaserBeam.instance) {
            	tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4+.001D, (double)z, d0, d1);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4+.001D, (double)z, d0, d3);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4+.001D, (double)z-0.5D, d2, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4+.001D, (double)z-0.5D, d2, d1);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4-.001D, (double)z-0.5D, d2, d1);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4-.001D, (double)z-0.5D, d2, d3);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4-.001D, (double)z, d0, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4-.001D, (double)z, d0, d1);
            }
            if(!world.getBlock(x, y, z+1).isOpaqueCube() && !world.isAirBlock(x, y, z+1) && world.getBlock(x, y, z+1) != BlockLaserBeam.instance) {
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4+.001D, (double)z+ 1.5D, d0, d1);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4+.001D, (double)z+ 1.5D, d0, d3);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4+.001D, (double)z + 1D, d2, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4+.001D, (double)z + 1D, d2, d1);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4-.001D, (double)z + 1D, d2, d1);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4-.001D, (double)z + 1D, d2, d3);
                tessellator.addVertexWithUV((double)((float)x + f3), (double)y + d4-.001D, (double)z+ 1.5D, d0, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)y + d4-.001D, (double)z+ 1.5D, d0, d1);
            }
        }

        if (flag2)
        {
            tessellator.addVertexWithUV((double)x, (double)y + d4, (double)((float)z + f3), d0, d3);
            tessellator.addVertexWithUV((double)x + 1D, (double)y + d4, (double)((float)z + f3), d2, d3);
            tessellator.addVertexWithUV((double)x + 1D, (double)y + d4, (double)((float)z + f2), d2, d1);
            tessellator.addVertexWithUV((double)x, (double)y + d4, (double)((float)z + f2), d0, d1);
            tessellator.addVertexWithUV((double)x, (double)y + d4, (double)((float)z + f2), d0, d1);
            tessellator.addVertexWithUV((double)x + 1D, (double)y + d4, (double)((float)z + f2), d2, d1);
            tessellator.addVertexWithUV((double)x + 1D, (double)y + d4, (double)((float)z + f3), d2, d3);
            tessellator.addVertexWithUV((double)x, (double)y + d4, (double)((float)z + f3), d0, d3);
            if(!world.getBlock(x-1, y, z).isOpaqueCube() && !world.isAirBlock(x-1, y, z) && world.getBlock(x-1, y, z) != BlockLaserBeam.instance) {
                tessellator.addVertexWithUV((double)x-0.5D, (double)y + d4+.001D, (double)((float)z + f3), d0, d3);
                tessellator.addVertexWithUV((double)x, (double)y + d4+.001D, (double)((float)z + f3), d2, d3);
                tessellator.addVertexWithUV((double)x, (double)y + d4+.001D, (double)((float)z + f2), d2, d1);
                tessellator.addVertexWithUV((double)x-0.5D, (double)y + d4+.001D, (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)x-0.5D, (double)y + d4-.001D, (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)x, (double)y + d4-.001D, (double)((float)z + f2), d2, d1);
                tessellator.addVertexWithUV((double)x, (double)y + d4-.001D, (double)((float)z + f3), d2, d3);
                tessellator.addVertexWithUV((double)x-0.5D, (double)y + d4-.001D, (double)((float)z + f3), d0, d3);
            }
            if(!world.getBlock(x+1, y, z).isOpaqueCube() && !world.isAirBlock(x+1, y, z) && world.getBlock(x+1, y, z) != BlockLaserBeam.instance) {
                tessellator.addVertexWithUV((double)x + 1D, (double)y + d4+.001D, (double)((float)z + f3), d0, d3);
                tessellator.addVertexWithUV((double)x+ 1.5D, (double)y + d4+.001D, (double)((float)z + f3), d2, d3);
                tessellator.addVertexWithUV((double)x+ 1.5D, (double)y + d4+.001D, (double)((float)z + f2), d2, d1);
                tessellator.addVertexWithUV((double)x + 1D, (double)y + d4+.001D, (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)x + 1D, (double)y + d4-.001D, (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)x+ 1.5D, (double)y + d4-.001D, (double)((float)z + f2), d2, d1);
                tessellator.addVertexWithUV((double)x+ 1.5D, (double)y + d4-.001D, (double)((float)z + f3), d2, d3);
                tessellator.addVertexWithUV((double)x + 1D, (double)y + d4-.001D, (double)((float)z + f3), d0, d3);
            }
        }

        return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderID ;
	}
}
