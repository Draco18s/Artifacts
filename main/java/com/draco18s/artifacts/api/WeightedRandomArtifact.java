package com.draco18s.artifacts.api;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

/**
 * This class is exposed to aid mod developers adding artifacts to their loot chests in a more detailed manner.<br/>
 * This class insures that artifacts generated in this manner will already have their effects applied.<br/>
 * Use this class exactly as you would WeightedRandomChestContent
 * @author Draco18s
 *
 */
public class WeightedRandomArtifact extends WeightedRandomChestContent {

	public WeightedRandomArtifact(Item item, int metadata, int minNumberPerStack, int maxNumberPerStack, int probability) {
		super(item, metadata, minNumberPerStack, maxNumberPerStack, probability);
	}

	public WeightedRandomArtifact(ItemStack par1ItemStack, int minNumberPerStack, int maxNumberPerStack, int probability) {
		super(par1ItemStack, minNumberPerStack, maxNumberPerStack, probability);
	}
	
	@Override
	protected ItemStack[] generateChestContent(Random random, IInventory newInventory)
    {
        ItemStack[] a = ChestGenHooks.generateStacks(random, theItemId, theMinimumChanceToGenerateItem, theMaximumChanceToGenerateItem);
        for(int i=a.length-1; i>=0; --i) {
        	a[i] = ArtifactsAPI.artifacts.applyRandomEffects(a[i]);
        }
        return a;
    }
}
