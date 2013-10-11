package draco18s.artifacts.arrowtrapbehaviors;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

final class DispenserBehaviorPotion implements IBehaviorDispenseItem
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
