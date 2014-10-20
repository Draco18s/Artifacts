package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentMining extends BaseComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onDig";
	}

	@Override
	public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
		ToolMaterial toolMaterial = ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")];
		if(toolMaterial == toolMaterial.WOOD) {
			return (Items.wooden_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
		}
		else if(toolMaterial == toolMaterial.STONE) {
			return (Items.stone_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
		}
		else if(toolMaterial == toolMaterial.GOLD) {
			return (Items.golden_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
		}
		else if(toolMaterial == toolMaterial.IRON) {
			return (Items.iron_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
		}
		else if(toolMaterial == toolMaterial.EMERALD) {
			return (Items.diamond_pickaxe.getDigSpeed(itemStack, block, meta) / 2 * ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
		}
		return (toolMaterial.values()[itemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial());
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block block, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		par1ItemStack.damageItem(1, par7EntityLivingBase);
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
		ToolMaterial toolMaterial = ToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")];
		
		if(toolMaterial == toolMaterial.WOOD) {
			return Items.wooden_pickaxe.func_150897_b/*canHarvestBlock*/(block);
		}
		else if(toolMaterial == toolMaterial.STONE) {
			return Items.stone_pickaxe.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.EMERALD) {
			return Items.diamond_pickaxe.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.IRON) {
			return Items.iron_pickaxe.func_150897_b(block);
		}
		else if(toolMaterial == ToolMaterial.GOLD) {
			return Items.golden_pickaxe.func_150897_b(block);
		}
		return block == Blocks.obsidian ? toolMaterial.getHarvestLevel() == 3 : (block != Blocks.diamond_block && block != Blocks.diamond_ore ? (block != Blocks.emerald_ore && block != Blocks.emerald_block ? (block != Blocks.gold_block && block != Blocks.gold_ore ? (block != Blocks.iron_block && block != Blocks.iron_ore ? (block != Blocks.lapis_block && block != Blocks.lapis_ore ? (block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore ? (block.getMaterial() == Material.rock ? true : (block.getMaterial() == Material.iron ? true : block.getMaterial() == Material.anvil)) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Effective Pickaxe"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Effective Pickaxe"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Miner's";
				break;
			case 1:
				str = "Digging";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Mining";
				break;
			case 1:
				str = "of Quarrying";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.FIGURINE | Flags.STAFF | Flags.TRINKET | Flags.WAND;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.AMULET | Flags.RING | Flags.ARMOR | Flags.BELT;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		ToolMaterial toolMaterial = ToolMaterial.values()[stack.stackTagCompound.getInteger("material")];
		
		if(toolClass.equals("pickaxe")) {
			return toolMaterial.getHarvestLevel();
		}
		else {
			return -1;
		}
	}
}
