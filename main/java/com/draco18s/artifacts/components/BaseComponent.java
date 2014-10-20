package com.draco18s.artifacts.components;

import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/*
		if(DragonArtifacts.baublesLoaded && itemStack.stackTagCompound != null) {
			NBTTagCompound tag = itemStack.stackTagCompound;
			if(trigger.equals("passively.") && UtilsForComponents.equipableByBaubles(tag.getString("iconName"))) {
				trigger = "when equipped.";
			}
		}
*/

public abstract class BaseComponent implements IArtifactComponent {
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		
		return "";
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
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		return false;
	}

	@Override
	public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
		
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack,
			EntityLivingBase par2EntityLivingBase,
			EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world,
			Block block, int x, int y, int z,
			EntityLivingBase entityLivingBase) {
		
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {
		
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return -1;
	}
	
	@Override
	public abstract void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip);

	@Override
	public abstract void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip);

	@Override
	public abstract String getPreAdj(Random rand);

	@Override
	public abstract String getPostAdj(Random rand);

	@Override
	public abstract int getTextureBitflags();

	@Override
	public abstract int getNegTextureBitflags();

}
