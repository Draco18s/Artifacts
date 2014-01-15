package draco18s.artifacts;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import draco18s.artifacts.api.ArtifactsAPI;

public class WeightedRandomArtifact extends WeightedRandomChestContent {

	public WeightedRandomArtifact(int itemID, int metadata, int minNumberPerStack, int maxNumberPerStack, int probability) {
		super(itemID, metadata, minNumberPerStack, maxNumberPerStack, probability);
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
