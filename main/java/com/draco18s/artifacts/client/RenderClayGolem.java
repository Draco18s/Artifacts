package com.draco18s.artifacts.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.draco18s.artifacts.entity.EntityClayGolem;

@SideOnly(Side.CLIENT)
public class RenderClayGolem extends RenderLiving
{
    /** Iron Golem's Model. */
    private ModelClayGolem ironGolemModel;
	private static final ResourceLocation textures = new ResourceLocation("artifacts:textures/mob/claygolem.png");

    public RenderClayGolem()
    {
        super(new ModelClayGolem(), 0.5F);
        this.ironGolemModel = (ModelClayGolem)this.mainModel;
    }

    /**
     * Renders the Iron Golem.
     */
    public void doRenderTreant(EntityClayGolem par1EntityTreant, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(par1EntityTreant, par2, par4, par6, par8, par9);
    }

    /**
     * Rotates Iron Golem corpse.
     */
    protected void rotateIronGolemCorpse(EntityClayGolem par1EntityTreant, float par2, float par3, float par4)
    {
        super.rotateCorpse(par1EntityTreant, par2, par3, par4);

        if ((double)par1EntityTreant.limbSwing >= 0.01D)
        {
            float f3 = 13.0F;
            float f4 = par1EntityTreant.limbSwing - par1EntityTreant.limbSwing * (1.0F - par4) + 6.0F;
            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
        }
    }

    /**
     * Renders Iron Golem Equipped items.
     */
    protected void renderTreantEquippedItems(EntityClayGolem par1EntityTreant, float par2)
    {
        super.renderEquippedItems(par1EntityTreant, par2);
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        this.renderTreantEquippedItems((EntityClayGolem)par1EntityLiving, par2);
    }

    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        this.rotateIronGolemCorpse((EntityClayGolem)par1EntityLiving, par2, par3, par4);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderTreant((EntityClayGolem)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderTreant((EntityClayGolem)par1Entity, par2, par4, par6, par8, par9);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return textures;
	}
}
