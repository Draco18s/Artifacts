package draco18s.artifacts.arrowtrapbehaviors;

import java.util.List;

import draco18s.artifacts.DamageSourceSword;
import draco18s.artifacts.api.internals.IBlockSource;
import draco18s.artifacts.entity.TileEntityTrap;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;

final class DispenserBehaviorSword extends BehaviorDefaultDispenseItem
{
	public int damage = 1;
    public DispenserBehaviorSword(int i) {
    	damage = i;
	}

	/**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing enumfacing = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
        double d0, d1, d2, d3, d4, d5;
        d0 = (int)par1IBlockSource.getX() - 1;
        d1 = (int)par1IBlockSource.getY() - 1;
        d2 = (int)par1IBlockSource.getZ() - 1;
        d3 = (int)par1IBlockSource.getX() + 1;
        d4 = (int)par1IBlockSource.getY() + 1;
        d5 = (int)par1IBlockSource.getZ() + 1;
        //System.out.println((int)par1IBlockSource.getX() + "," + (int)par1IBlockSource.getZ());
        d0 += 0.5 * d0/Math.abs(d0);
        d1 += 0.5 * d1/Math.abs(d1);
        d2 += 0.5 * d2/Math.abs(d2);
        d3 += 0.5 * d3/Math.abs(d3);
        d4 += 0.5 * d4/Math.abs(d4);
        d5 += 0.5 * d5/Math.abs(d5);
        
        d0 += enumfacing.getFrontOffsetX();
        d3 += enumfacing.getFrontOffsetX();
        d2 += enumfacing.getFrontOffsetZ();
        d5 += enumfacing.getFrontOffsetZ();
        
        if(enumfacing.getFrontOffsetX() != 0) {
        	d2 += 0.5;
        	d5 -= 0.5;
        }
        if(enumfacing.getFrontOffsetZ() != 0) {
        	d0 += 0.5;
        	d3 -= 0.5;
        }
        
        AxisAlignedBB par2AxisAlignedBB = AxisAlignedBB.getAABBPool().getAABB(d0, d1, d2, d3, d4, d5);
        //System.out.println(par2AxisAlignedBB);
        List<EntityLivingBase> ents = par1IBlockSource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, par2AxisAlignedBB);
        if(ents.size() > 0) {
        	for(int l=ents.size() - 1; l >= 0; l--) {
        		EntityLivingBase ent = ents.get(l);
        		ent.attackEntityFrom(DamageSourceSword.instance, damage);
        		par2ItemStack.damageItem(1, ent);
        	}
        }
        TileEntityTrap te = (TileEntityTrap) par1IBlockSource.getBlockTileEntity();
        if(te != null) {
			/*int a = par2ItemStack.getItem().itemID;
			int b = 0;
			switch(a) {
				case 11:
					b = 1;
					break;
				case 12:
					b = 2;
					break;
				case 16:
					b = 3;
					break;
				case 20:
					b = 4;
					break;
				case 27:
					b = 5;
					break;
			}*/
			te.startSwordRender(par1IBlockSource.getWorld(), par1IBlockSource.getXInt()+enumfacing.getFrontOffsetX(), par1IBlockSource.getYInt()+enumfacing.getFrontOffsetY(), par1IBlockSource.getZInt()+enumfacing.getFrontOffsetZ(), par1IBlockSource.getBlockMetadata(), par2ItemStack);
		}
        
        /*EntitySword entsw = new EntitySword(par1IBlockSource.getWorld(), par1IBlockSource.getX(), par1IBlockSource.getY(), par1IBlockSource.getZ());
        par1IBlockSource.getWorld().spawnEntityInWorld((Entity)entsw);*/
        return par2ItemStack;
    }
}
