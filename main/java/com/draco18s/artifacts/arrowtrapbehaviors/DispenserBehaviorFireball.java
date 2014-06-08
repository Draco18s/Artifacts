package com.draco18s.artifacts.arrowtrapbehaviors;

import java.util.Random;

import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.draco18s.artifacts.api.internals.IBlockSource;
import com.draco18s.artifacts.block.BlockTrap;

final class DispenserBehaviorFireball extends BehaviorDefaultDispenseItem
{
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing enumfacing = BlockTrap.getFacing(par1IBlockSource.getBlockMetadata());
        IPosition iposition = BlockTrap.getIPositionFromBlockSource(par1IBlockSource);
        double d0 = iposition.getX() + (double)((float)enumfacing.getFrontOffsetX() * 0.3F);
        double d1 = iposition.getY() + (double)((float)enumfacing.getFrontOffsetX() * 0.3F);
        double d2 = iposition.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 0.3F);
        World world = par1IBlockSource.getWorld();
        Random random = world.rand;
        double d3 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetX();
        double d4 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetY();
        double d5 = random.nextGaussian() * 0.05D + (double)enumfacing.getFrontOffsetZ();
        world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.getWorld().playAuxSFX(1009, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
    }
}
