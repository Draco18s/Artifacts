package com.draco18s.artifacts.api.interfaces;

import com.draco18s.artifacts.api.internals.BehaviorTrapItemProvider;
import com.draco18s.artifacts.api.internals.IBlockSource;
import net.minecraft.item.ItemStack;

/**
 * Duplicate of dispenser behaviors for the Trap block.  This interface
 * is the one that should be extended by plugin developers.
 * @see net.minecraft.dispenser
 * @author Draco18s
 *
 */
public interface IBehaviorTrapItem
{
    IBehaviorTrapItem itemDispenseBehaviorProvider = new BehaviorTrapItemProvider();

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    ItemStack dispense(IBlockSource iblocksource, ItemStack itemstack);
}
