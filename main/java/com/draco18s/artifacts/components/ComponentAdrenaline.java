package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

public class ComponentAdrenaline extends BaseComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onTakeDamage";
		return "";
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		int delay = itemStack.stackTagCompound.getInteger("adrenDelay_armor");
		if(!world.isRemote && delay > 0) {
			if(delay == 198) {
				//System.out.println("Crashing!");
				player.addPotionEffect(new PotionEffect(4, 200, 1));
				player.addPotionEffect(new PotionEffect(17, 200, 0));
			}
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Activates several potion effects after taking damage");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Activates several potion effects"));
		par3List.add("   " + StatCollector.translateToLocal("tool."+trigger));
	}
	
	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		//System.out.println("Player has been damaged!");
		if(isWornArmor && !event.isCanceled()) {
			if(itemStack.stackTagCompound.getInteger("adrenDelay_armor") <= 0 && event.entity instanceof EntityPlayer) {
				//System.out.println("Attempting to apply potion effects to player.");
				EntityPlayer p = (EntityPlayer)event.entity;
				//if(p.getHealth() <= 4) {
					//System.out.println("Applying Potion Effects.");
					itemStack.stackTagCompound.setInteger("adrenDelay_armor", 300);
					
					p.addPotionEffect(new PotionEffect(1, 100, 1));
					p.addPotionEffect(new PotionEffect(5, 100, 1));
					p.addPotionEffect(new PotionEffect(11, 100, 2));
					
				//}
			}
		}
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Brawling";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Rage";
	}

	@Override
	public int getTextureBitflags() {
		return Flags.CHESTPLATE;//1024
	}

	@Override
	public int getNegTextureBitflags() {
		
		return ~(Flags.CHESTPLATE | Flags.LEGGINGS);//2815;//6911;
	}
}
