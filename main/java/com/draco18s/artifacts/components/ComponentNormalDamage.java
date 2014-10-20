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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentNormalDamage extends BaseComponent {

	public String getName() {
		return "Damage";
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onHeld";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		NBTTagCompound inbt = i.stackTagCompound;
		NBTTagCompound nnbt = new NBTTagCompound();
		NBTTagList nnbtl = new NBTTagList();
		int base = i.stackTagCompound.getInteger("material");
		switch(base) {
			case 0:
			case 3:
				base = 0;
				break;
		}
		base += 4;
		AttributeModifier att = new AttributeModifier("generic.attackDamage", base, 0);
		nnbt.setLong("UUIDMost", att.getID().getMostSignificantBits());
		nnbt.setLong("UUIDLeast", att.getID().getLeastSignificantBits());
		nnbt.setString("Name", att.getName());
		nnbt.setDouble("Amount", att.getAmount());
		nnbt.setInteger("Operation", att.getOperation());
		nnbt.setString("AttributeName", att.getName());
		nnbtl.appendTag(nnbt);
		inbt.setTag("AttributeModifiers", nnbtl);
		//i.addEnchantment(Enchantment.sharpness, rand.nextInt(5)+1);
		return i;
	}

	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
		if (block == Blocks.web)
        {
            return true;
        }
        else
        {
            Material material = block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? false : true;
        }
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		//par3List.add("Weapon damage");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		//par3List.add("Weapon damage");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Deadly";
				break;
			case 1:
				str = "Painful";
				break;
			case 2:
				str = "Killing";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Rending";
				break;
			case 1:
				str = "of Pain";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.DAGGER | Flags.SWORD;
	}

	@Override
	public int getNegTextureBitflags() {
		return ~getTextureBitflags();
	}
}
