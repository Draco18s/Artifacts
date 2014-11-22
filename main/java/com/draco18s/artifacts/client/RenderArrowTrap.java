package com.draco18s.artifacts.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.BlockTrap;
import com.draco18s.artifacts.block.PseudoBlockTrap;
import com.draco18s.artifacts.entity.TileEntityTrap;

public class RenderArrowTrap implements ISimpleBlockRenderingHandler {
	private int renderType;
	
	public RenderArrowTrap(int r) {
		renderType = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
		renderer.renderBlockAsItem(PseudoBlockTrap.instance, 0, 1.0f);

	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int blockMeta = world.getBlockMetadata(x, y, z);
		blockMeta = blockMeta & 7;
		
		int[] blockToCopyLocation = BlockTrap.getNeighbourBlockPosition(world, x, y, z, block, blockMeta);
		Block blockToCopy = Blocks.stonebrick;
		int metaToCopy = 0;
		if(! (blockToCopyLocation[0] == x && blockToCopyLocation[1] == y && blockToCopyLocation[2] == z )) {
			blockToCopy = world.getBlock(blockToCopyLocation[0], blockToCopyLocation[1], blockToCopyLocation[2]);
			metaToCopy = world.getBlockMetadata(blockToCopyLocation[0], blockToCopyLocation[1], blockToCopyLocation[2]);
		}
			
		IIcon camo = blockToCopy.getIcon(blockMeta, metaToCopy);
		IIcon furnace = Blocks.furnace.getIcon(2,3);
		IIcon top = Blocks.furnace.getIcon(1,0);
		//Icon furnace = Block.furnaceIdle.getIcon(2,3);
		//Icon top = Block.furnaceIdle.getIcon(1,0);
		IIcon self = block.getIcon(0, blockMeta);
		renderer.renderBlockUsingTexture(Blocks.stone, x, y, z, camo);
		
		double offset = 0.001;
		
		switch(blockMeta) {
			case 0:
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, self);
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, top);
				break;
			case 1:
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, self);
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, top);
				break;
			case 2:
				renderer.renderFaceZNeg(blockToCopy, (double)x, (double)y, (double)z - offset, self);
				renderer.renderFaceZPos(blockToCopy, (double)x, (double)y, (double)z + offset, furnace);
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, top);
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, top);
				break;
			case 3:
				renderer.renderFaceZPos(blockToCopy, (double)x, (double)y, (double)z + offset, self);
				renderer.renderFaceZNeg(blockToCopy, (double)x, (double)y, (double)z - offset, furnace);
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, top);
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, top);
				break;
			case 4:
				renderer.renderFaceXNeg(blockToCopy, (double)x - offset, (double)y, (double)z, self);
				renderer.renderFaceXPos(blockToCopy, (double)x + offset, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, top);
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, top);
				break;
			case 5:
				renderer.renderFaceXPos(blockToCopy, (double)x + offset, (double)y, (double)z, self);
				renderer.renderFaceXNeg(blockToCopy, (double)x - offset, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(blockToCopy, (double)x, (double)y + offset, (double)z, top);
				renderer.renderFaceYNeg(blockToCopy, (double)x, (double)y - offset, (double)z, top);
				break;
		}
		
//		TileEntity te = world.getTileEntity(x, y, z);
//		
//		if(te != null && te instanceof TileEntityTrap) {
//			TileEntityTrap tet = (TileEntityTrap) te;
//			
//			if(tet.swordRenderTicks > 0) {
//				
//				if(tet.swordToRender != null) {
//					System.out.println("Rendering Sword. Ticks: " + tet.swordRenderTicks + ", Sword: " + tet.swordToRender.getItemStackDisplayName(new ItemStack(tet.swordToRender)));
//					IIcon swordIcon = tet.swordToRender.getIcon(new ItemStack(tet.swordToRender), 0);
//					
//					Tessellator tess = Tessellator.instance;
					
	//				renderer.setOverrideBlockTexture(swordIcon);
					
//					renderer.renderFaceXPos(blockToCopy, (double)x - 0.5, (double)y + 1.0, (double)z, swordIcon);
//					renderer.renderFaceXNeg(blockToCopy, (double)x + 0.5, (double)y + 1.0, (double)z, swordIcon);
					
	//				tess.addVertexWithUV(x, y+1, z, 0, 0);
	//				tess.addVertexWithUV(x, y+2, z, 1, 0);
	//				tess.addVertexWithUV(x, y+1, z+1, 0, 1);
	//				tess.addVertexWithUV(x, y+2, z+1, 1, 1);
	
	//				tess.addVertexWithUV(x, y+1, z, 0, 0);
	//				tess.addVertexWithUV(x, y+2, z, 0, 1);
	//				tess.addVertexWithUV(x, y+1, z+1, 1, 0);
	//				tess.addVertexWithUV(x, y+2, z+1, 1, 1);
	
	//				renderer.clearOverrideBlockTexture();
					
//					tet.swordToRender = null;
//					}
//				tet.swordRenderTicks--;
//			}
//			else {
//				System.out.println("Not rendering sword. Ticks:" + tet.swordRenderTicks + ", Sword: " + tet.swordToRender);
//			}
//		}
		
		
		/*if(te != null) {
			if(te.swordObj != null) {
				System.out.println("Rendering sword");
				GL11.glPushMatrix();
				int r = 0;
				switch(l) {
					case 0:
					case 1:
						r = -1;
						break;
					case 2:
						r = 0;
						break;
					case 3:
						r = 180;
						break;
					case 4:
						r = 270;
						break;
					case 5:
						r = 90;
						break;
				}
				if(r >= 0) {
					float angle = (r + 18*te.swordObj.age);
					//move sword to center of block
					GL11.glTranslatef((float)x + 0.5F, (float)y + 1F, (float)z + 0.5F);
					//rotate sword based on age and facing
					GL11.glRotatef(angle, 0, 1, 0);
					//rotate sword to lay on side
					GL11.glRotatef(90, 1, 0, 0);
					RenderManager.instance.renderEntityWithPosYaw(te.swordObj, 0, 0, 0, 0, 0);
				}
				else {
					//animate stab
					float m = te.swordObj.age - 5;
					GL11.glTranslatef((float)x + 0.5F, (float)y + 1F + (m/5F), (float)z + 0.5F);
					RenderManager.instance.renderEntityWithPosYaw(te.swordObj, 0, 0, 0, 0, 0);
				}
				GL11.glPopMatrix();
			}
		}*/
		
		return true;
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
