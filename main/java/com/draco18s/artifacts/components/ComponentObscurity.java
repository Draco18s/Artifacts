package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.ArtifactClientEventHandler;
import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.SToCMessage;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentObscurity extends BaseComponent {
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onTakeDamage";
		}
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "onItemRightClick";
				break;
			case 1:
				str = "hitEntity";
				break;
		}
		return str;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		UtilsForComponents.sendPotionPacket(14, 600, 0, player);
		player.addPotionEffect(new PotionEffect(14, 600, 0));
		ArtifactClientEventHandler.cloaked = true;
		//System.out.println("Cloaking player.");
		UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 1); //itemStack.damageItem(1, player);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 200);
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityVictim, EntityLivingBase entityAttacker) {
		EntityPlayerMP player = UtilsForComponents.getPlayerFromUsername(entityAttacker.getCommandSenderName());
		
		if(player != null) {
			player.addPotionEffect(new PotionEffect(14, 600, 0));
			
			PacketBuffer out = new PacketBuffer(Unpooled.buffer());
			
			out.writeInt(PacketHandlerClient.OBSCURITY);
			SToCMessage packet = new SToCMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendTo(packet, player);
			
			//System.out.println("Cloaking player.");
			itemStack.damageItem(1, player);
//			UtilsForComponents.sendItemDamagePacket(entityAttacker, entityAttacker.inventory.currentItem, 1); //itemStack.damageItem(1, player);
//			itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 200);
		}
		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, String trigger, boolean advTooltip) {
		list.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Invisibility"));
		list.add("  " + EnumChatFormatting.AQUA + StatCollector.translateToLocal("tool."+trigger) + " (30 "  + StatCollector.translateToLocal("time.seconds")+ ")");
		list.add("  " + EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Reduced vision while invisible."));

	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("effect.Invisibility"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Obscured";
				break;
			case 1:
				str = "Hidden";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Obscurity";
				break;
			case 1:
				str = "of Cloaking";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.ARMOR | Flags.BELT | Flags.BOOTS | Flags.CHESTPLATE | Flags.DAGGER | Flags.FIGURINE | Flags.HELM | Flags.LEGGINGS | Flags.RING | Flags.STAFF | Flags.SWORD | Flags.TRINKET | Flags.WAND;
	}

	@Override
	public int getNegTextureBitflags() {
		return 0;
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	
		if(isWornArmor) {
			hitEntity(itemStack, event.entityLiving, event.entityLiving);
		}
	}
}
