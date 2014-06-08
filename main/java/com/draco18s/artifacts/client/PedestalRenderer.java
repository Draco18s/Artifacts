package com.draco18s.artifacts.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.entity.TileEntityDisplayPedestal;
import com.draco18s.artifacts.item.ItemArtifact;
import com.draco18s.artifacts.item.ItemArtifactArmor;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class PedestalRenderer extends TileEntitySpecialRenderer{
	private ModelPedestal stand = new ModelPedestal();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		stand.render(tileentity, d0, d1, d2);
		
		if(tileentity instanceof TileEntityDisplayPedestal) {
			TileEntityDisplayPedestal ped = (TileEntityDisplayPedestal)tileentity;
			if(ped.itemEnt != null && DragonArtifacts.renderNamesInPedestals && (ped.itemEnt.getEntityItem().getItem() == ItemArtifact.instance || ped.itemEnt.getEntityItem().getItem() instanceof ItemArtifactArmor)) {
				//"Artifact"
				//String s = ped.itemEnt.getEntityItem().stackTagCompound.getString("name");
				NBTTagCompound nbt = ped.itemEnt.getEntityItem().stackTagCompound;
				String n = "";
				
				if(ItemArtifact.doEnchName) {
    				n += StatCollector.translateToLocal(nbt.getString("enchName")) + " ";
    			}
    			if(ItemArtifact.doAdjName) {
    				n += nbt.getString("preadj") + " ";
    			}
    			if(ItemArtifact.doMatName) {
    				n += nbt.getString("matName");
    			}
				renderLabel(ped, n, d0+0.5, d1+0.625, d2+0.5, 16);
				n = "";
    			if(!(ItemArtifact.doEnchName || ItemArtifact.doMatName || ItemArtifact.doAdjName)) {
    				n += "Artifact ";
    			}
    			n += nbt.getString("iconName");
    			if(ItemArtifact.doAdjName) {
    				n += " " + nbt.getString("postadj");
    			}
				renderLabel(ped, n, d0+0.5, d1+0.55, d2+0.5, 16);
			}
		}
	}
	
	protected void renderLabel(TileEntityDisplayPedestal entity, String s, double d, double d1, double d2, int i) {
		double f = entity.getDistanceFrom(this.field_147501_a.staticPlayerX, this.field_147501_a.staticPlayerY, this.field_147501_a.staticPlayerZ);
	    if (f > i) {
	      return;
	    }
	    FontRenderer fontrenderer = this.field_147501_a.getFontRenderer();
	    float f1 = 1.6F;
	    float f2 = 0.01666667F * f1;
	    double angle = Math.toRadians(entity.rotation);
		float sin = (float)(Math.sin(angle));
		float cos = (float)(Math.cos(angle));
		sin = Math.round(sin*100)/100;
		cos = Math.round(cos*100)/100;
	    
	    GL11.glPushMatrix();
	    GL11.glTranslatef((float)d+(float)(sin*0.35), (float)d1, (float)d2-(float)(cos*0.35));
	    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	    GL11.glRotatef(-1*entity.rotation, 0, 1, 0);
	    GL11.glScalef(0.3f, 0.3f, 0.3f);
	    //GL11.glRotatef(-this.tileEntityRenderer.playerYaw, 0.0F, 1.0F, 0.0F);
	    //GL11.glRotatef(this.tileEntityRenderer.playerPitch, 1.0F, 0.0F, 0.0F);
	    GL11.glScalef(-f2, -f2, f2);
	    GL11.glDisable(2896);
	    GL11.glDepthMask(false);
	    GL11.glDisable(2929);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    Tessellator tessellator = Tessellator.instance;
	    byte byte0 = 0;
	    GL11.glDisable(3553);
	    tessellator.disableColor();
	    tessellator.startDrawingQuads();
	    int j = fontrenderer.getStringWidth(s) / 2;
	    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.35F);
	    tessellator.addVertex(-j - 1, -1 + byte0, 0.0D);
	    tessellator.addVertex(-j - 1, 8 + byte0, 0.0D);
	    tessellator.addVertex(j + 1, 8 + byte0, 0.0D);
	    tessellator.addVertex(j + 1, -1 + byte0, 0.0D);
	    tessellator.draw();
	    //tessellator.func_78381_a();
	    GL11.glEnable(3553);
	    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, 553648127);
	    GL11.glEnable(2929);
	    GL11.glDepthMask(true);
	    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, -1);
	    GL11.glEnable(2896);
	    GL11.glDisable(3042);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glPopMatrix();
	  }
}
