package com.draco18s.artifacts.client;

import com.draco18s.artifacts.block.BlockQuickSand;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderQuickSand implements ISimpleBlockRenderingHandler {
	private int renderType;
	
	public RenderQuickSand(int r) {
		renderType = r;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
        boolean flag = block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1);
        boolean flag1 = block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0);
        boolean[] aboolean = new boolean[] {block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2), block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3), block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4), block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)};

        if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
        {
            return false;
        }
        else
        {
            boolean flag2 = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            double d0 = 0.0D;
            double d1 = 1.0D;
            int i1 = renderer.blockAccess.getBlockMetadata(x, y, z);
            double mainHeight = BlockQuickSand.getQuicksandBlockLevel(world, x, y, z);
            double zHeight = mainHeight;
            if(world.getBlock(x, y, z + 1) == BlockQuickSand.instance)  zHeight = BlockQuickSand.getQuicksandBlockLevel(world, x, y, z + 1);
            double xzHeight = mainHeight;
            if(world.getBlock(x + 1, y, z + 1) == BlockQuickSand.instance)  xzHeight = BlockQuickSand.getQuicksandBlockLevel(world, x + 1, y, z + 1);
            double xHeight = mainHeight;
            if(world.getBlock(x + 1, y, z) == BlockQuickSand.instance)  xHeight = BlockQuickSand.getQuicksandBlockLevel(world, x + 1, y, z);
            double offset = 0.0010000000474974513D;
            float f9;
            float f10;
            float f11;
            
            IIcon iicon = block.getIcon(0, 0);
            float uMin = iicon.getInterpolatedU(0);
            float uMax = iicon.getInterpolatedU(16);
            float vMin = iicon.getInterpolatedV(0);
            float vMax = iicon.getInterpolatedV(16);

            if (renderer.renderAllFaces || flag)
            {
                flag2 = true;

                mainHeight -= offset;
                zHeight -= offset;
                xzHeight -= offset;
                xHeight -= offset;
                double d7;
                double d8;
                double d10;
                double d12;
                double d14;
                double d16;
                double d18;
                double d20;


                tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
                tessellator.setColorOpaque_F(f4 * f, f4 * f1, f4 * f2);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + mainHeight, (double)(z + 0), uMin, vMin);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + zHeight, (double)(z + 1), uMin, vMax);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + xzHeight, (double)(z + 1), uMax, vMax);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + xHeight, (double)(z + 0), uMax, vMin);
            }

            if (renderer.renderAllFaces || flag1)
            {
                tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
                tessellator.setColorOpaque_F(f3, f3, f3);
                renderer.renderFaceYNeg(block, (double)x, (double)y + offset, (double)z, renderer.getBlockIconFromSide(block, 0));
                flag2 = true;
            }

            for (int k1 = 0; k1 < 4; ++k1)
            {
                int l1 = x;
                int j1 = z;

                if (k1 == 0)
                {
                    j1 = z - 1;
                }

                if (k1 == 1)
                {
                    ++j1;
                }

                if (k1 == 2)
                {
                    l1 = x - 1;
                }

                if (k1 == 3)
                {
                    ++l1;
                }


                if (renderer.renderAllFaces || aboolean[k1])
                {
                    double d9;
                    double d11;
                    double d13;
                    double d15;
                    double d17;
                    double d19;

                    if (k1 == 0)
                    {
                        d9 = mainHeight;
                        d11 = xHeight;
                        d13 = (double)x;
                        d17 = (double)(x + 1);
                        d15 = (double)z + offset;
                        d19 = (double)z + offset;
                    }
                    else if (k1 == 1)
                    {
                        d9 = xzHeight;
                        d11 = zHeight;
                        d13 = (double)(x + 1);
                        d17 = (double)x;
                        d15 = (double)(z + 1) - offset;
                        d19 = (double)(z + 1) - offset;
                    }
                    else if (k1 == 2)
                    {
                        d9 = zHeight;
                        d11 = mainHeight;
                        d13 = (double)x + offset;
                        d17 = (double)x + offset;
                        d15 = (double)(z + 1);
                        d19 = (double)z;
                    }
                    else
                    {
                        d9 = xHeight;
                        d11 = xzHeight;
                        d13 = (double)(x + 1) - offset;
                        d17 = (double)(x + 1) - offset;
                        d15 = (double)z;
                        d19 = (double)(z + 1);
                    }

                    flag2 = true;
                    tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, l1, y, j1));
                    float f13 = 1.0F;
                    f13 *= k1 < 2 ? f5 : f6;
                    tessellator.addVertexWithUV(d13, (double)y + d9, d15, uMax, vMin);
                    tessellator.addVertexWithUV(d17, (double)y + d11, d19, uMax, vMax);
                    tessellator.addVertexWithUV(d17, (double)(y + 0), d19, uMin, vMax);
                    tessellator.addVertexWithUV(d13, (double)(y + 0), d15, uMin, vMin);
                }
            }

            renderer.renderMinY = d0;
            renderer.renderMaxY = d1;
            return flag2;
        }
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderType;
	}

}
