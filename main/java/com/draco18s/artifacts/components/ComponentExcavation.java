package com.draco18s.artifacts.components;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentExcavation implements IArtifactComponent {

	public ComponentExcavation() {
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onBlockDestroyed";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
		ToolMaterial toolMaterial = ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")];
		if(toolMaterial == ToolMaterial.WOOD) {
			//System.out.println("Wood " + Item.pickaxeWood.getStrVsBlock(par1ItemStack, par2Block));
			return Math.max((Items.wooden_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10,
					        (Items.wooden_shovel.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10);
		}
		else if(toolMaterial == ToolMaterial.STONE) {
			//System.out.println("Stone " + Item.pickaxeStone.getStrVsBlock(par1ItemStack, par2Block));
			return Math.max((Items.stone_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10,
					        (Items.stone_shovel.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10);
		}
		else if(toolMaterial == ToolMaterial.EMERALD) {
			//System.out.println("Diamond " + Item.pickaxeDiamond.getStrVsBlock(par1ItemStack, par2Block));
			return Math.max((Items.diamond_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10,
			                (Items.diamond_shovel.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10);
			}
		else if(toolMaterial == ToolMaterial.IRON) {
			//System.out.println("Iron " + Item.pickaxeIron.getStrVsBlock(par1ItemStack, par2Block));
			return Math.max((Items.iron_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10,
			                (Items.iron_shovel.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10);
			}
		else if(toolMaterial == ToolMaterial.GOLD) {
			//System.out.println("Gold " + Item.pickaxeGold.getStrVsBlock(par1ItemStack, par2Block));
			return Math.max((Items.golden_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10,
			                (Items.golden_shovel.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10);
			}
		return (ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) { 
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block destroyedBlock, int x, int y, int z, EntityLivingBase player) {
		//System.out.println("Test (" + x + "," + y + "," + z + ")");
		//player.getLookVec();
		int numBlocks = 0;
		for(int i=-1;i<=1;i++) {
			for(int j=-1;j<=1;j++) {
				for(int k=-1;k<=1;k++) {
					Block block = world.getBlock(x+i, y+j, z+k);
					if(block != null) {
						System.out.println("block: " + block.getUnlocalizedName());
						System.out.println("can:  " + itemStack.getItem().canHarvestBlock(block, itemStack));
						if(this.canHarvestBlock(block, itemStack)) {
							int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack);
							block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x+i, y+j, z+k), fortuneLevel);
							world.setBlockToAir(x+i, y+j, z+k);
							numBlocks++;
						}
					}
				}
			}
		}
		itemStack.damageItem(numBlocks/3, player);
		//Block block = Block.blocksList[l]
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
		ToolMaterial toolMaterial = ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")];
		
		try {
			if(block.getBlockHardness(null, 0, 0, 0) == -1) {
				return false;
			}
		}
		catch(NullPointerException e) {
			return false;
		}
		
		if(block.getMaterial().isToolNotRequired()) {
			return true;
		}
		
		if(toolMaterial == ToolMaterial.WOOD) {
			return Items.wooden_pickaxe.func_150897_b/*canHarvestBlock*/(block) || Items.wooden_shovel.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.STONE) {
			return Items.stone_pickaxe.func_150897_b(block) || Items.stone_shovel.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.EMERALD) {
			return Items.diamond_pickaxe.func_150897_b(block) || Items.diamond_shovel.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.IRON) {
			return Items.iron_pickaxe.func_150897_b(block) || Items.iron_shovel.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.GOLD) {
			return Items.golden_pickaxe.func_150897_b(block) || Items.golden_shovel.func_150897_b(block);
		}

		return toolMaterial.getHarvestLevel() >= block.getHarvestLevel(0);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Digs big holes"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Digs big holes"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Excavator's";
				break;
			case 1:
				str = "Digger's";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Excavation";
	}

	@Override
	public int getTextureBitflags() {
		return 156;
	}

	@Override
	public int getNegTextureBitflags() {
		return 261;
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) { }

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		ToolMaterial toolMaterial = ToolMaterial.values()[stack.stackTagCompound.getInteger("material")];
		
		if(toolClass.equals("pickaxe") || toolClass.equals("shovel")) {
			return toolMaterial.getHarvestLevel();
		}
		else {
			return -1;
		}
	}
}
