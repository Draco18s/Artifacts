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
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentMassWeb extends BaseComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onDropped";
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Slows down all mobs") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Slows down all mobs"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Webbing";
				break;
			case 1:
				str = "Mob Slowing";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Trapping";
				break;
			case 1:
				str = "of Quicksand";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.FIGURINE | Flags.TRINKET;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.ARMOR | Flags.BELT;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		if(type == "onDropped") {
			//int del = entityItem.getEntityItem().stackTagCompound.getInteger("dropDelay");
			//System.out.println("Delay: " + del);
			//if(del > 0) {
			//	--del;
			//}
			//else {
				double par1 = entityItem.posX-10;
				double par2 = entityItem.posY-10;
				double par3 = entityItem.posZ-10;
				double par4 = entityItem.posX+10;
				double par5 = entityItem.posY+10;
				double par6 = entityItem.posZ+10;
				AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(par1, par2, par3, par4, par5, par6);
				List<EntityLivingBase> ents = entityItem.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
				EntityPlayer player = null;
				EntityLivingBase someEnt = null;
				if(ents.size() > 0) {
		        	for(int l=ents.size() - 1; l >= 0; l--) {
		        		EntityLivingBase ent = ents.get(l);
		        		someEnt = ent;
		        		if(!(ent instanceof EntityPlayer)) {
		        			ent.addPotionEffect(new PotionEffect(2, 20, 2));
		        		}
		        		else if(player == null) {
		        			player = (EntityPlayer) ent;
		        		}
		        	}
		        }
				//del = 20;
				if(player != null) {
					entityItem.getEntityItem().damageItem(1, player);
					//entityItem.worldObj.newExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 0, false, true);
				}
				else if(someEnt != null) {
					entityItem.getEntityItem().damageItem(1, someEnt);
					//entityItem.worldObj.newExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 0, false, true);
				}
			//}
			//entityItem.getEntityItem().stackTagCompound.setInteger("dropDelay", del);
		}
		return false;
	}
}
