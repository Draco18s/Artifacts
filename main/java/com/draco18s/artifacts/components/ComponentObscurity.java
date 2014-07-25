package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.ArtifactClientEventHandler;
import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
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

public class ComponentObscurity implements IArtifactComponent {
	public ComponentObscurity() {
	}
	
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
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		UtilsForComponents.sendPotionPacket(14, 600, 0, player);
		player.addPotionEffect(new PotionEffect(14, 600, 0));
		ArtifactClientEventHandler.cloaked = true;
		System.out.println("Cloaking player.");
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
			
			System.out.println("Cloaking player.");
			itemStack.damageItem(1, player);
//			UtilsForComponents.sendItemDamagePacket(entityAttacker, entityAttacker.inventory.currentItem, 1); //itemStack.damageItem(1, player);
//			itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 200);
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
		return 7935;
	}

	@Override
	public int getNegTextureBitflags() {
		return 0;
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
		
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	
		if(isWornArmor) {
			hitEntity(itemStack, event.entityLiving, event.entityLiving);
		}
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return -1;
	}
}
