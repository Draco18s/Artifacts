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
import com.draco18s.artifacts.components.UtilsForComponents.Flags;

public class ComponentKnockbackResist extends BaseComponent {

	private static double knockbackAmount = 1.0;
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onArmorTickUpdate";//onUpdate?
		else
			return "";
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
		return Flags.ARMOR | Flags.BOOTS | Flags.LEGGINGS;
	}

	@Override
	public int getNegTextureBitflags() {
		return ~(Flags.ARMOR | Flags.BOOTS | Flags.LEGGINGS | Flags.CHESTPLATE | Flags.HELM);
	}
}
