package  com.draco18s.artifacts.arrowtrapbehaviors;

import  com.draco18s.artifacts.api.internals.IBlockSource;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

final class DispenserBehaviorMobEgg extends BehaviorDefaultDispenseItem
{
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing enumfacing = BlockDispenser.func_149937_b/*getFacing*/(par1IBlockSource.getBlockMetadata());
        double d0 = par1IBlockSource.getX() + (double)enumfacing.getFrontOffsetX();
        double d1 = (double)((float)par1IBlockSource.getYInt() + 0.2F) + enumfacing.getFrontOffsetY();
        double d2 = par1IBlockSource.getZ() + (double)enumfacing.getFrontOffsetZ();
        Entity entity = ItemMonsterPlacer.spawnCreature(par1IBlockSource.getWorld(), par2ItemStack.getItemDamage(), d0, d1, d2);

        if (entity instanceof EntityLiving && par2ItemStack.hasDisplayName())
        {
            ((EntityLiving)entity).setCustomNameTag(par2ItemStack.getDisplayName());
        }

        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }
}
