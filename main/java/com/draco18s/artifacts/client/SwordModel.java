package com.draco18s.artifacts.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.artifacts.entity.TileEntitySword;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class SwordModel extends ModelBase {
	public void render(TileEntity tileent, double x, double y, double z) {
		
		TileEntitySword te = (TileEntitySword)tileent;
		int l = te.metadata & 7;
		if(te.itemEnt != null) {
			int r = 0;
			switch(l) {
				case 0:
				case 1:
					r = -1;
					break;
				case 2:
					r = 270;
					break;
				case 3:
					r = 90;
					break;
				case 4:
					r = 0;
					break;
				case 5:
					r = 180;
					break;
			}
			if(r >= 0) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b/*getFacing*/(l);
				int a = te.itemEnt.age;
				//if(a < 8) {
					te.itemEnt.age = 0;
					GL11.glPushMatrix();
					float angle = (r - 22.5f*a);
					//move sword to center of block
					float ox = 0.5f-enumfacing.getFrontOffsetX()*0.75F;
					float oz = 0.5f-enumfacing.getFrontOffsetZ()*0.75F;
					GL11.glTranslatef((float)x + ox, (float)y - 0.5F, (float)z + oz);
					//rotate sword based on age and facing
					GL11.glRotatef(angle, 0, 1, 0);
					//rotate sword to lay on side
					GL11.glRotatef(90, 1, 0, 0);
					GL11.glScalef(4, 4, 2);
					RenderManager.instance.renderEntityWithPosYaw(te.itemEnt, 0, 0, 0, 0, 0);
					GL11.glPopMatrix();
					te.itemEnt.age = a;
				//}
			}
			else {
				//animate
				int a = te.itemEnt.age;
				te.itemEnt.age = 0;
				float m = Math.abs(a - 4);
				GL11.glPushMatrix();
				if(l == 1) {
					GL11.glTranslatef((float)x + 0.5F, (float)y + 0.0F - (m/15F) - 1f, (float)z + 0.5F);
				}
				else {
					//m = Math.abs(a - 4) * -1;
					GL11.glTranslatef((float)x + 0.5F, (float)y - 0.0F + (m/15F) + 2f, (float)z + 0.5F);
					GL11.glRotatef(180, 0, 0, 1);
				}
				//GL11.glRotatef(-45, 0, 0, 1);
				GL11.glScaled(2, 2, 2);
				RenderManager.instance.renderEntityWithPosYaw(te.itemEnt, 0, 0, 0, 0, 0);
				GL11.glPopMatrix();
				te.itemEnt.age = a;
			}
		}
	}
}
