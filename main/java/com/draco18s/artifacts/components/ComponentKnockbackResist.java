package com.draco18s.artifacts.components;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentKnockbackResist implements IArtifactComponent {

	private static double knockbackAmount = 1.0;
	
	public ComponentKnockbackResist() {
		
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onArmorTickUpdate";//onUpdate?
		else
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
	public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta) {
		
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack,
			EntityLivingBase par2EntityLivingBase,
			EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
			Block block, int par4, int par5, int par6,
			EntityLivingBase par7EntityLivingBase) {
		
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
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean held) {
//		String uu = itemStack.stackTagCompound.getString("KnockResUUID");
//		UUID knockbackID;
//		if(uu.equals("")) {
//			knockbackID = UUID.randomUUID();
//			itemStack.stackTagCompound.setString("KnockResUUID", knockbackID.toString());
//		}
//		else {
//			knockbackID = UUID.fromString(uu);
//		}
//		
//		if(entity instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer)entity;
//			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
//			AttributeModifier mod;
//			mod = new AttributeModifier(knockbackID,"KnockbackResComponent", knockbackAmount, 2);
//			if(player.openContainer != null && player.openContainer != player.inventoryContainer || player.capabilities.isCreativeMode) {
//				if(atinst.getModifier(knockbackID) != null)
//				{
//					atinst.removeModifier(mod);
//				}
//			}
//			else {
//				if(atinst.getModifier(knockbackID) == null)
//				{
//					atinst.applyModifier(mod);
//				}
//				itemStack.stackTagCompound.setInteger("KnockbackResist", player.getEntityId());
//			}
//		}
//		else {
//			int eid = itemStack.stackTagCompound.getInteger("KnockbackResist");
//			EntityPlayer player = (EntityPlayer) world.getEntityByID(eid);
//			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
//			AttributeModifier mod;
//			mod = new AttributeModifier(knockbackID,"KnockbackResComponent", knockbackAmount, 2);
//			if(atinst.getModifier(knockbackID) != null)
//			{
//				atinst.removeModifier(mod);
//			}	
//		}
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Knockback Resistance"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Knockback Resistance") + " " + StatCollector.translateToLocal("tool." + trigger));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Stable";
				break;
			case 1:
				str = "Heavy";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		
		return "of Stability";
	}

	@Override
	public int getTextureBitflags() {
		return 4864;
	}

	@Override
	public int getNegTextureBitflags() {
		return 255;
	}


	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
//		if(worn)
//			onUpdate(itemStack, world, player, 0, true);
//		else {
//			String uu = itemStack.stackTagCompound.getString("KnockResUUID");
//			if(uu.equals("")) {
//				return;
//			}
//			UUID knockbackID = UUID.fromString(uu);
//			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
//			AttributeModifier mod;
//			mod = new AttributeModifier(knockbackID,"KnockbackResComponent", knockbackAmount, 2);
//			if(player.capabilities.isCreativeMode && player.openContainer != null && player.openContainer == player.inventoryContainer) {
//				if(atinst.getModifier(knockbackID) != null)
//				{
//					atinst.removeModifier(mod);
//				}
//			}
//		}
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
