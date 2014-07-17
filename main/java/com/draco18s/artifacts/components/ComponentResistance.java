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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentResistance implements IArtifactComponent {

	public ComponentResistance() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "onItemRightClick";
				break;
			case 1:
				str = "hitEntity";
				break;
			case 2:
				str = "onUpdate";
				break;
		}
		return str;
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
	public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta) {
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		UtilsForComponents.sendPotionPacket(11, 600, 0, player);
		UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 1);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 200);
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingHit, EntityLivingBase entityLivingPlayer) {
		if(!entityLivingPlayer.worldObj.isRemote) {
			entityLivingPlayer.addPotionEffect(new PotionEffect(11, 100, 0));
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block block, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	//works great
	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
		if(!world.isRemote) {
			if(par3Entity instanceof EntityLivingBase) {
				EntityLivingBase ent = (EntityLivingBase) par3Entity;
				ent.addPotionEffect(new PotionEffect(11, 10, 0));
			}
		}
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		if(trigger == "passively.") {
			par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Damage Resistance"));
		}
		else {
			int time = 0;
			if(trigger == "when inflicting damage.") {
				time = 5;
			}
			else if(trigger == "when used.") {
				time = 30;
			}
			par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Damage Resistance"));
			par3List.add("  " + EnumChatFormatting.AQUA + StatCollector.translateToLocal("tool."+trigger) + " (" + time + " "  + StatCollector.translateToLocal("time.seconds") + ")");
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Damage Resistance"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Armored";
				break;
			case 1:
				str = "Shielding";
				break;
			case 2:
				str = "Guarding";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Protection";
				break;
			case 1:
				str = "of Armor";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 1373;
	}

	@Override
	public int getNegTextureBitflags() {
		return 130;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn)
			onUpdate(itemStack, world, player, 0, true);
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return -1;
	}
}
