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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentBreathing extends BaseComponent {

	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			switch(rand.nextInt(2)) {
				case 0:
					return "onArmorTickUpdate";
				case 1:
					return "onTakeDamage";
			}
			return "";
		}
		String str = "";
		switch(rand.nextInt(5)) {
			case 0:
			case 1:
				str = "onItemRightClick";
				break;
			case 2:
			case 3:
				str = "hitEntity";
				break;
			case 4:
				str = "onHeld";
				break;
		}
		return str;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		UtilsForComponents.sendPotionPacket(13, 1200, 0, player);
		UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 1);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 200);
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingHit, EntityLivingBase player) {
		if(!player.worldObj.isRemote) {
			
			player.addPotionEffect(new PotionEffect(13, 1200, 0));
			
		}
		return false;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		int time = 0;
		if(trigger.equals("when inflicting damage.")) {
			time = 15;
		}
		else if(trigger.equals("when used.")) {
			time = 60;
		}
		else if(trigger.equals("after taking damage.")) {
			trigger = "after taking drowning damage.";
			time = 2;
		}
		par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Water Breathing"));
		par3List.add("  " + EnumChatFormatting.AQUA + StatCollector.translateToLocal("tool."+trigger) + " (" + time + StatCollector.translateToLocal("time.seconds") + ")");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(EnumChatFormatting.AQUA + "Water Breathing");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Aerated";
				break;
			case 1:
				str = "Breathy";
				break;
			case 2:
				str = "Oxygenated";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Breathing";
				break;
			case 1:
				str = "of Fresh Air";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.FIGURINE | Flags.RING | Flags.STAFF | Flags.TRINKET | Flags.ARMOR | Flags.HELM;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.DAGGER | Flags.WAND | Flags.BOOTS | Flags.LEGGINGS;
	}

	@Override
	public void onHeld(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
		//onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(!world.isRemote) {
			if(entity instanceof EntityLivingBase) {
				EntityLivingBase ent = (EntityLivingBase) entity;
				ent.addPotionEffect(new PotionEffect(13, 10, 0));
			}
		}
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn)
			onHeld(itemStack, world, player, 0, true);
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		if(isWornArmor && event.source == DamageSource.drown) {
			event.entityLiving.addPotionEffect(new PotionEffect(13, 40, 0));
		}
	}
}
