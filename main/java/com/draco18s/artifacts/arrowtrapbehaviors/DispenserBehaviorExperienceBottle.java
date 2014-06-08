package com.draco18s.artifacts.arrowtrapbehaviors;

import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.world.World;

final class DispenserBehaviorExperienceBottle extends BehaviorProjectileDispense 
{
	protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
	{
	    return new EntityExpBottle(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
	}
	
	protected float func_82498_a()
	{
	    return super.func_82498_a() * 0.5F;
	}
	
	protected float func_82500_b()
	{
	    return super.func_82500_b() * 1.25F;
	}
}
