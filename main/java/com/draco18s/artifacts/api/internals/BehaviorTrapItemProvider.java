package com.draco18s.artifacts.api.internals;

import com.draco18s.artifacts.api.interfaces.IBehaviorTrapItem;
import net.minecraft.item.ItemStack;

public final class BehaviorTrapItemProvider implements IBehaviorTrapItem
{
    public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return par2ItemStack;
    }
}
