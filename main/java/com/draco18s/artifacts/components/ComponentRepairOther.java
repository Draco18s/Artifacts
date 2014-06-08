package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.ArtifactTickHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.network.CPacketArtifactComponent;
import com.draco18s.artifacts.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentRepairOther implements IArtifactComponent {

	public ComponentRepairOther() {
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		String str = "";
		if(isArmor) {
			str = "onArmorTickUpdate";
		}
		else {
			switch(rand.nextInt(2)) {
			case 0:
				str = "onHeld";
				break;
			case 1:
				str = "onItemRightClick";
				break;
			}
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
		int i = -1;
		int c = 0;
		ItemStack stack;
		do {
			c++;
			i = world.rand.nextInt(40);
			stack = player.inventory.getStackInSlot(i);
		} while((stack == null || stack == itemStack || !stack.isItemDamaged()) && c < 100);
		if(stack != null && stack.isItemDamaged()) {
			//stack.setItemDamage(stack.getItemDamage()-5);
			//par1ItemStack.damageItem(5, (EntityLivingBase) par3EntityPlayer);
			PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
			out.writeInt(PacketHandlerServer.REPAIRING);
			out.writeInt(i);
			out.writeInt(player.inventory.currentItem);
			CPacketArtifactComponent packet = new CPacketArtifactComponent(player.getCommandSenderName(), out);
			DragonArtifacts.packetPipeline.sendToServer(packet);
		
			//par1ItemStack.setItemDamage(par1ItemStack.getItemDamage()+1);
		}
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
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
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Repairs other items") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Repairs other items"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
		case 0:
			str = "Recharging";
			break;
		case 1:
			str = "Repairing";
			break;
		case 2:
			str = "Refreshing";
			break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
		case 0:
			str = "of Repair";
			break;
		case 1:
			str = "of Renew";
			break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 4173;//77;
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
	public void onHeld(ItemStack itemStack, World world, Entity entity, int slot, boolean isWorn) {
		//onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
//		int del = par1ItemStack.stackTagCompound.getInteger("repairDelay");
//		if(del > 0) {
//			--del;
//		}
//		else 
		if(entity instanceof EntityPlayer){
			if(ArtifactTickHandler.repairCount % 200 == 0) {
				//par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
				EntityPlayer player = (EntityPlayer)entity;
				int i = -1;
				int c = 0;
				ItemStack stack;
				do {
					c++;
					i = world.rand.nextInt(player.inventory.getSizeInventory());
					stack = player.inventory.getStackInSlot(i);
				} while((stack == null || stack == itemStack || !stack.isItemDamaged()) && c < 100);
				if(stack != null && stack.isItemDamaged()) {
					stack.setItemDamage(stack.getItemDamage()-1);
					itemStack.damageItem(1, player);
					//par1ItemStack.setItemDamage(par1ItemStack.getItemDamage()+1);
				}
//				del = 200;
			}
		}
//		par1ItemStack.stackTagCompound.setInteger("repairDelay", del);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn)
			onHeld(itemStack, world, player, 0, true);
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
