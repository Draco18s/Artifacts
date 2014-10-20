package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import net.minecraft.entity.ai.attributes.IAttributeInstance;
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

public class ComponentSpeed extends BaseComponent {
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		return "onHeld";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		int q = i.stackTagCompound.getInteger("onHeld");
		if(q > 0) {
			IArtifactComponent a = ArtifactsAPI.artifacts.getComponent(q);
			if(a == this) {
				NBTTagCompound inbt = i.stackTagCompound;
				NBTTagCompound nnbt = new NBTTagCompound();
				NBTTagList nnbtl = new NBTTagList();
				double amount = 0.05D + rand.nextInt(5)/200D + rand.nextInt(5)/200D;
				i.stackTagCompound.setDouble("boostAmount",amount);
				AttributeModifier att = new AttributeModifier("generic.movementSpeed", amount, 2);
				nnbt.setLong("UUIDMost", att.getID().getMostSignificantBits());
				nnbt.setLong("UUIDLeast", att.getID().getLeastSignificantBits());
				nnbt.setString("Name", att.getName());
				nnbt.setDouble("Amount", att.getAmount());
				nnbt.setInteger("Operation", att.getOperation());
				nnbt.setString("AttributeName", att.getName());
				nnbtl.appendTag(nnbt);
				inbt.setTag("AttributeModifiers", nnbtl);
			}
		}
		return i;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		//NBTTagCompound nbt = (NBTTagCompound) par1ItemStack.stackTagCompound.getTag("AttributeModifiers");
		//System.out.println(nbt.getDouble("Amount"));
		if(!trigger.equals("when held.")) {
			/*double amount = par1ItemStack.stackTagCompound.getDouble("boostAmount");
			DecimalFormat myFormatter = new DecimalFormat("#.#");
			String output = myFormatter.format(amount*100);*/
			par3List.add(StatCollector.translateToLocal("effect.Speed Boost") + " " + StatCollector.translateToLocal("tool."+trigger) + " " + EnumChatFormatting.BLUE + "+5% Speed");
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Speed Boost"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
		case 0:
			str = "Swift";
			break;
		case 1:
			str = "Quick";
			break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
		case 0:
			str = "of Swiftness";
			break;
		case 1:
			str = "of Movement";
			break;
		case 2:
			str = "of Speed";
			break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.FIGURINE | Flags.RING | Flags.STAFF | Flags.TRINKET | Flags.ARMOR | Flags.BOOTS | Flags.LEGGINGS;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.DAGGER | Flags.SWORD | Flags.HELM | Flags.BELT;
	}
}
