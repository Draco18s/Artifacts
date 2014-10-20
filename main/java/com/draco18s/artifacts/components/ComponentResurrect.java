package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.api.ArtifactsAPI;
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
import net.minecraft.entity.ai.attributes.RangedAttribute;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentResurrect extends BaseComponent {
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onDeath";
		}
		return "";
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int c = data.getInteger("resCooldown_armor");
		if(c < 1) {
			par3List.add(StatCollector.translateToLocal("effect.Restores health") + " " + StatCollector.translateToLocal("tool."+trigger));
			par3List.add("   (5 " + StatCollector.translateToLocal("time.minute") + " " + StatCollector.translateToLocal("time.cooldown") + ")");
		}
		else {
			String m = "";
			if(c >= 1200) {
				m = ((c+30)/1200) + " "+StatCollector.translateToLocal("time.minutes");
			}
			else {
				m = (c/20) + " "+StatCollector.translateToLocal("time.seconds");
			}
			par3List.add(StatCollector.translateToLocal("effect.Restores health") + " " + StatCollector.translateToLocal("tool."+trigger));
			par3List.add("   (" + StatCollector.translateToLocal("time.on") + " " + StatCollector.translateToLocal("time.cooldown") + ": " + m + ")");
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Restores health when the player takes");
		par3List.add("leathal damage. (5 minute cooldown)");
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Regenerative";
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Regeneration";
				break;
			case 1:
				str = "of Lazarus";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.ARMOR | Flags.CHESTPLATE;
	}

	@Override
	public int getNegTextureBitflags() {
		return ~(getTextureBitflags() | Flags.LEGGINGS | Flags.HELM);
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {
		System.out.println("ABORTING DEATH");
		if(isWornArmor && !event.isCanceled()) {
			EntityPlayer player = (EntityPlayer)event.entity;
			NBTTagCompound data = itemStack.getTagCompound();
			//System.out.println("Cooldown: " + data.getInteger("resCooldown"));
			if(data.getInteger("resCooldown_armor") <= 0) {
				event.setCanceled(true);
				player.setHealth(20);
				data.setInteger("resCooldown_armor", 6000);
				itemStack.damageItem(5, player);
				return;
			}
		}
	}
}
