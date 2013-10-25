package draco18s.artifacts.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import draco18s.artifacts.DragonArtifacts;
import draco18s.artifacts.block.BlockTrap;
import draco18s.artifacts.block.PseudoBlockTrap;
import draco18s.artifacts.entity.TileEntityTrap;

public class RenderArrowTrap implements ISimpleBlockRenderingHandler {
	private int renderType;
	
	public RenderArrowTrap(int r) {
		renderType = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		renderer.renderBlockAsItem(PseudoBlockTrap.instance, 3, 1.0F);
	}
	
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int l = world.getBlockMetadata(x, y, z);
		
		l = l & 7;
		int i = x;
		int j = y;
		int k = z;
		int xm = 0;
		int zm = 0;
		switch(l) {
			case 0: 
	        case 2:
	        case 3:
	        	xm = 1;
	        	break;
	        case 1:
	        case 4:
	        case 5:
	        	zm = 1;
	        	break;
	    }
		int meta = 0;
		int wid1 = world.getBlockId(x+xm, y, z+zm);
		int wid2 = world.getBlockId(x-xm, y, z-zm);
		int wid = 0;
		if(wid1 != block.blockID && Block.blocksList[wid1] != null && Block.blocksList[wid1].isOpaqueCube()) {
			wid = wid1;
			meta = world.getBlockMetadata(x+xm, y, z+zm);
		}
		else if(wid2 != block.blockID && Block.blocksList[wid2] != null && Block.blocksList[wid2].isOpaqueCube()) {
			wid = wid2;
			meta = world.getBlockMetadata(x-xm, y, z-zm);
		}
		else {
			if(l <= 1) {
				wid1 = world.getBlockId(x+zm, y, z+xm);
				wid2 = world.getBlockId(x-zm, y, z-xm);
				if(wid1 != block.blockID && Block.blocksList[wid1] != null && Block.blocksList[wid1].isOpaqueCube()) {
					wid = wid1;
					meta = world.getBlockMetadata(x+zm, y, z+xm);
				}
				else if(wid2 != block.blockID && Block.blocksList[wid2] != null && Block.blocksList[wid2].isOpaqueCube()) {
					wid = wid2;
					meta = world.getBlockMetadata(x-zm, y, z-xm);
				}
				else {
					wid1 = world.getBlockId(x, y+1, z);
					wid2 = world.getBlockId(x, y-1, z);
					if(wid1 != block.blockID && Block.blocksList[wid1] != null && Block.blocksList[wid1].isOpaqueCube()) {
						wid = wid1;
					}
					else if(wid2 != block.blockID && Block.blocksList[wid2] != null && Block.blocksList[wid2].isOpaqueCube()) {
						wid = wid2;
					}
					else {
						wid = Block.stoneBrick.blockID;
					}
				}
			}
			else {
				wid1 = world.getBlockId(x, y+1, z);
				wid2 = world.getBlockId(x, y-1, z);
				if(wid1 != block.blockID && Block.blocksList[wid1] != null && Block.blocksList[wid1].isOpaqueCube()) {
					wid = wid1;
				}
				else if(wid2 != block.blockID && Block.blocksList[wid2] != null && Block.blocksList[wid2].isOpaqueCube()) {
					wid = wid2;
				}
				else {
					wid = Block.stoneBrick.blockID;
				}
			}
		}
		Icon camo = Block.blocksList[wid].getIcon(l, meta);
		Icon furnace = Block.blocksList[61].getIcon(2,3);
		Icon top = Block.blocksList[61].getIcon(1,0);
		//Icon furnace = Block.furnaceIdle.getIcon(2,3);
		//Icon top = Block.furnaceIdle.getIcon(1,0);
		Icon self = block.getIcon(0, l);
		renderer.renderBlockUsingTexture(Block.stone, x, y, z, camo);
		switch(l) {
			case 0:
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
			case 1:
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
			case 2:
				renderer.renderFaceZNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceZPos(Block.blocksList[wid], (double)x, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
			case 3:
				renderer.renderFaceZPos(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceZNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
			case 4:
				renderer.renderFaceXNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceXPos(Block.blocksList[wid], (double)x, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
			case 5:
				renderer.renderFaceXPos(Block.blocksList[wid], (double)x, (double)y, (double)z, self);
				renderer.renderFaceXNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, furnace);
				renderer.renderFaceYPos(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				renderer.renderFaceYNeg(Block.blocksList[wid], (double)x, (double)y, (double)z, top);
				break;
		}
		
		/*TileEntityTrap te = (TileEntityTrap)world.getBlockTileEntity(x, y, z);
		if(te != null) {
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
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderType;
	}
}
