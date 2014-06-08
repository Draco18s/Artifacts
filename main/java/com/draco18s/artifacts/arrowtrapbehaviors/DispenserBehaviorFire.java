package com.draco18s.artifacts.arrowtrapbehaviors;

import com.draco18s.artifacts.api.internals.IBlockSource;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

final class DispenserBehaviorFire extends BehaviorDefaultDispenseItem
{
    private boolean field_96466_b = true;

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing enumfacing = BlockDispenser.func_149937_b/*getFacing*/(par1IBlockSource.getBlockMetadata());
        World world = par1IBlockSource.getWorld();
        int i = par1IBlockSource.getXInt();
        int j = par1IBlockSource.getYInt();
        int k = par1IBlockSource.getZInt();

        if(enumfacing.getFrontOffsetY() == 1) {
        	int n = 0;
        	if(world.isAirBlock(i+1, j+1, k)) {
        		world.setBlock(i+1,j+1,k, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i-1, j+1, k)) {
        		world.setBlock(i-1,j+1,k, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k+1)) {
        		world.setBlock(i,j+1,k+1, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k-1)) {
        		world.setBlock(i,j+1,k-1, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k)) {
        		world.setBlock(i,j+1,k, Blocks.fire);
        		n++;
        	}
        	if (par2ItemStack.attemptDamageItem(n, world.rand))
            {
                par2ItemStack.stackSize = 0;
            }
        }
        else if(enumfacing.getFrontOffsetY() == -1) {
        	while(!world.getBlock(i, j, k).isBlockNormalCube()) {
        		j--;
        	}
        	int n = 0;
        	if(world.isAirBlock(i+1, j+1, k)) {
        		world.setBlock(i+1,j+1,k, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i-1, j+1, k)) {
        		world.setBlock(i-1,j+1,k, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k+1)) {
        		world.setBlock(i,j+1,k+1, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k-1)) {
        		world.setBlock(i,j+1,k-1, Blocks.fire);
        		n++;
        	}
        	if(world.isAirBlock(i, j+1, k)) {
        		world.setBlock(i,j+1,k, Blocks.fire);
        		n++;
        	}
        	if (par2ItemStack.attemptDamageItem(n, world.rand))
            {
                par2ItemStack.stackSize = 0;
            }
        }
        else {
	        for(int times=1; times <= 5; times++) {
		        if (world.isAirBlock(i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times))
		        {
		            world.setBlock(i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times, Blocks.fire);
		
		            if (par2ItemStack.attemptDamageItem(1, world.rand))
		            {
		                par2ItemStack.stackSize = 0;
		            }
		        }
		        else if (world.getBlock(i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times) == Blocks.tnt)
		        {
		            Blocks.tnt.onBlockDestroyedByPlayer(world, i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times, 1);
		            world.setBlockToAir(i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times);
		        }
		        else if(times == 1 && world.getBlock(i+enumfacing.getFrontOffsetX()*times, j+enumfacing.getFrontOffsetY()*times, k+enumfacing.getFrontOffsetZ()*times) != Blocks.fire)
		        {
		            this.field_96466_b = false;
		            break;
		        }
		        else {
		        	break;
		        }
	        }
        }
        return par2ItemStack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        if (this.field_96466_b)
        {
            par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
        else
        {
            par1IBlockSource.getWorld().playAuxSFX(1001, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
    }
}
