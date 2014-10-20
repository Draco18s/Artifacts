package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.ArtifactTickHandler;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentRepair extends BaseComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		String str = "";
		if(isArmor) {
			switch(rand.nextInt(2)) {
			case 0:
				str = "onArmorTickUpdate";
				break;
			case 1:
				str = "onHeld";
				break;
			}
			return str;
		}
		switch(rand.nextInt(2)) {
		case 0:
			str = "onUpdate";
			break;
		case 1:
			str = "onHeld";
			break;
		}
		return str;
	}

	//works great
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isWorn) {
		
//		int del = itemStack.stackTagCompound.getInteger("repairDelay");
//		if(del > 0) {
//			--del;
//		}
//		else 
		if(itemStack.getItemDamage() > 0) {
			if((isWorn && ArtifactTickHandler.repairCount % 200 == 0) || (!isWorn && ArtifactTickHandler.repairCount == 0)) {
				itemStack.setItemDamage(itemStack.getItemDamage() - 1);
			}
//			if(flag)
//				del = 200;
//			else
//				del = 1200;
		}
//		itemStack.stackTagCompound.setInteger("repairDelay", del);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		if(trigger.equals("when equipped.")) {
			trigger = "when not equipped.";
		}
		par3List.add(StatCollector.translateToLocal("effect.Slowly repairs itself") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Slowly repairs itself"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
		case 0:
			str = "Recharging";
			break;
		case 1:
			str = "Repairing";
			break;
		case 2:
			str = "Refreshing";
			break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
		case 0:
			str = "of Repair";
			break;
		case 1:
			str = "of Renew";
			break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.FIGURINE | Flags.RING | Flags.TRINKET | Flags.LEGGINGS;
	}

	@Override
	public int getNegTextureBitflags() {
		return 0;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(!worn)
			onUpdate(itemStack, world, player, 0, false);
	}
}
