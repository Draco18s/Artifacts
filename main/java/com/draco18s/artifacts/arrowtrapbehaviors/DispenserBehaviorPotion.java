package com.draco18s.artifacts.arrowtrapbehaviors;

import com.draco18s.artifacts.api.interfaces.IBehaviorTrapItem;
import com.draco18s.artifacts.api.internals.IBlockSource;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

final class DispenserBehaviorPotion implements IBehaviorTrapItem
{
    private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return ItemPotion.isSplash(par2ItemStack.getItemDamage()) ? (new DispenserBehaviorPotionProjectile(this, par2ItemStack)).dispense(par1IBlockSource, par2ItemStack) : this.defaultDispenserItemBehavior.dispense(par1IBlockSource, par2ItemStack);
    }
}
