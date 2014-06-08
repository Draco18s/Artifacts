package com.draco18s.artifacts.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import com.draco18s.artifacts.entity.TileEntityDisplayPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelPedestal extends ModelBase
{
	//fields
	ModelRenderer glassCase;
	ModelRenderer pillar;
	ModelRenderer platform;
	ModelRenderer base;

	public ModelPedestal()
	{
		textureWidth = 64;
		textureHeight = 32;

		glassCase = new ModelRenderer(this, 0, 17);
		glassCase.addBox(0F, 0F, 0F, 10, 5, 10);
		glassCase.setRotationPoint(-5F, 19F, -5F);
		glassCase.setTextureSize(64, 32);
		glassCase.mirror = true;
		setRotation(glassCase, 0F, 0F, 0F);
		pillar = new ModelRenderer(this, 40, 0);
		pillar.addBox(0F, 0F, 0F, 6, 8, 6);
		pillar.setRotationPoint(-3F, 10F, -3F);
		pillar.setTextureSize(64, 32);
		pillar.mirror = true;
		setRotation(pillar, 0F, 0F, 0F);
		platform = new ModelRenderer(this, 0, 0);
		platform.addBox(0F, 0F, 0F, 10, 1, 10);
		platform.setRotationPoint(-5F, 18F, -5F);
		platform.setTextureSize(64, 32);
		platform.mirror = true;
		setRotation(platform, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 10, 2, 10);
		base.setRotationPoint(-5F, 8F, -5F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
	}

	public void render(TileEntity te, double x, double y, double z) {
		TileEntityDisplayPedestal es = (TileEntityDisplayPedestal)te;

		if(es.itemEnt != null) {
			GL11.glPushMatrix();
			//System.out.println("Rot: " + es.rotation);
			double angle = Math.toRadians(es.rotation);
			float sin = (float)(Math.sin(angle));
			float cos = (float)(Math.cos(angle));
			sin = Math.round(sin*100)/100;
			cos = Math.round(cos*100)/100;
			//System.out.println("Sin: " + sin);
			//System.out.println("Cos: " + cos);
			
			GL11.glTranslatef((float)x + 0.5F + (float)(sin*0.25), (float)y + 0.76F, (float)z + 0.5F - (float)(cos*0.25));
			
			GL11.glRotatef(-1*es.rotation, 0, 1, 0);
			GL11.glRotatef(90, 1, 0, 0);
			
			//RenderHelper.enableStandardItemLighting();
			RenderManager.instance.renderEntityWithPosYaw(es.itemEnt, 0, 0, 0, 0, 0);
			//RenderHelper.disableStandardItemLighting();
			GL11.glPopMatrix();
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y - 0.5f, (float)z + 0.5f);
		//GL11.glScalef(0.5f, 0.5f, 0.5f);
		//FMLClientHandler.instance().getClient().renderEngine.bindTexture(es.getModelTexture());
		ResourceLocation rl = new ResourceLocation("artifacts:textures/blocks/pedestal.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
		this.render();
		GL11.glPopMatrix();
	}

	private void render() {
		pillar.render(0.0625F);
		platform.render(0.0625F);
		base.render(0.0625F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glassCase.render(0.0625F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}
}
