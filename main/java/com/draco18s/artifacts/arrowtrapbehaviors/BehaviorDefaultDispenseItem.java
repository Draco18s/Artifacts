package com.draco18s.artifacts.arrowtrapbehaviors;

import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.draco18s.artifacts.api.interfaces.IBehaviorTrapItem;
import com.draco18s.artifacts.api.internals.IBlockSource;
import com.draco18s.artifacts.block.BlockTrap;

public class BehaviorDefaultDispenseItem implements IBehaviorTrapItem
{
    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    public final ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        ItemStack itemstack1 = this.dispenseStack(par1IBlockSource, par2ItemStack);
        //this.playDispenseSound(par1IBlockSource);
        //this.spawnDispenseParticles(par1IBlockSource, BlockArrowTrap.getFacing(par1IBlockSource.getBlockMetadata()));
        return itemstack1;
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        /*EnumFacing enumfacing = BlockArrowTrap.getFacing(par1IBlockSource.getBlockMetadata());
        IPosition iposition = BlockArrowTrap.getIPositionFromBlockSource(par1IBlockSource);
        ItemStack itemstack1 = par2ItemStack.splitStack(1);
        doDispense(par1IBlockSource.getWorld(), itemstack1, 6, enumfacing, iposition);*/
        return par2ItemStack;
    }

    public static void doDispense(World par0World, ItemStack par1ItemStack, int par2, EnumFacing par3EnumFacing, IPosition par4IPosition)
    {
        /*double d0 = par4IPosition.getX();
        double d1 = par4IPosition.getY();
        double d2 = par4IPosition.getZ();
        EntityItem entityitem = new EntityItem(par0World, d0, d1 - 0.3D, d2, par1ItemStack);
        double d3 = par0World.rand.nextDouble() * 0.1D + 0.2D;
        entityitem.motionX = (double)par3EnumFacing.getFrontOffsetX() * d3;
        entityitem.motionY = 0.20000000298023224D;
        entityitem.motionZ = (double)par3EnumFacing.getFrontOffsetZ() * d3;
        entityitem.motionX += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        entityitem.motionY += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        entityitem.motionZ += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        par0World.spawnEntityInWorld(entityitem);*/
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
    }

    /**
     * Order clients to display dispense particles from the specified block and facing.
     */
    protected void spawnDispenseParticles(IBlockSource par1IBlockSource, EnumFacing par2EnumFacing)
    {
        par1IBlockSource.getWorld().playAuxSFX(2000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), this.func_82488_a(par2EnumFacing));
    }

    private int func_82488_a(EnumFacing par1EnumFacing)
    {
        return par1EnumFacing.getFrontOffsetX() + 1 + (par1EnumFacing.getFrontOffsetZ() + 1) * 3;
    }
}
