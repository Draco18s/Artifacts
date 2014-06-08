package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;

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

public class ComponentMining implements IArtifactComponent {

	public ComponentMining() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onDig";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		return i;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,	EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		return false;
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

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	//works great
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
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
		return 156;
	}

	@Override
	public int getNegTextureBitflags() {
		return 261;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
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
		
		if(toolClass.equals("pickaxe")) {
			return toolMaterial.getHarvestLevel();
		}
		else {
			return -1;
		}
	}
}
