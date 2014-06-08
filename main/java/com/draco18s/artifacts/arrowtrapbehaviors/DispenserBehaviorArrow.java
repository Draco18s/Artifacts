package com.draco18s.artifacts.arrowtrapbehaviors;

import com.draco18s.artifacts.entity.EntitySpecialArrow;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

final class DispenserBehaviorArrow extends BehaviorProjectileDispense
{
    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        /*EntityArrow entityarrow = new EntityArrow(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
        entityarrow.canBePickedUp = 1;
        entityarrow.setDamage(6);
        return entityarrow;*/
    	EntitySpecialArrow entityarrow = new EntitySpecialArrow(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ(), 2);
    	entityarrow.canBePickedUp = 1;
    	return entityarrow;
    }
}
