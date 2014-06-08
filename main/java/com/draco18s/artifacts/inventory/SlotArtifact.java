package com.draco18s.artifacts.inventory;

import com.draco18s.artifacts.item.ItemArtifact;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class SlotArtifact extends Slot {

	public SlotArtifact(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == ItemArtifact.instance || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor;
    }
}
